package xk.xact.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import xk.xact.api.CraftingHandler;
import xk.xact.api.ICraftingDevice;
import xk.xact.client.gui.GuiCrafting;
import xk.xact.inventory.Inventory;
import xk.xact.recipes.CraftRecipe;
import xk.xact.recipes.RecipeUtils;

import java.util.Arrays;
import java.util.List;

// Used by the GUI
public class CraftPad implements ICraftingDevice {

	private CraftRecipe lastRecipe = null;
	/**
	 * This stores the slot the pad was originally in Only used if the player
	 * used the hotkey to open the gui
	 */
	public byte craftPadOriginalSlot = 0;
	private CraftingHandler handler;
	private EntityPlayer player;

	public final Inventory chipInv;
	public final Inventory gridInv;
	public final Inventory outputInv;

	// If true, the CraftPad's inventory has changed and it must be saved to
	// NBT.
	public boolean inventoryChanged = false;

	// Used by GuiPad to update it's internal state.
	// Should only be accessed client-side for rendering purposes.
	public boolean recentlyUpdated = true;

	public CraftPad(ItemStack stack, EntityPlayer player) {
		this.player = player;
		this.outputInv = new Inventory(1, "outputInv") {
			@Override
			public void markDirty() {
				super.markDirty();
				updateState();
			}
		};
		this.gridInv = new Inventory(9, "gridInv") {
			@Override
			public void markDirty() {
				super.markDirty();
				inventoryChanged = true;
				updateRecipe();
				updateState();
			}
		};
		this.chipInv = new Inventory(1, "chipInv") {
			@Override
			public void markDirty() {
				super.markDirty();
				inventoryChanged = true;
				updateRecipe();
				updateState();
			}
		};

		this.handler = CraftingHandler.createCraftingHandler(this);

		readFromNBT(stack.stackTagCompound);
	}

	// //////////
	// / Current State

	public boolean[] getMissingIngredients() {
		return getHandler().getMissingIngredientsArray(lastRecipe);
	}

	public void updateRecipe() {
		lastRecipe = RecipeUtils.getRecipe(gridInv.getContents(),
				player.worldObj);
		if (getWorld().isRemote)
			notifyClient();

		ItemStack output = lastRecipe == null ? null : lastRecipe.getResult();
		outputInv.setInventorySlotContents(0, output);
	}

	public void updateState() {
		recentlyUpdated = true;

	}

	// //////////
	// / ICraftingDevice

	@Override
	public final List getAvailableInventories() {
		return Arrays.asList(player.inventory);
	}

	@Override
	public int getRecipeCount() {
		return 1;
	}

	@Override
	public boolean canCraft(int index) {
		return handler.canCraft(lastRecipe, null);
	}

	@Override
	public CraftRecipe getRecipe(int index) {
		return lastRecipe;
	}

	@Override
	public CraftingHandler getHandler() {
		return handler;
	}

	@Override
	public World getWorld() {
		return player.worldObj;
	}

	@Override
	public int getX() {
		return player.chunkCoordX;
	}

	@Override
	public int getY() {
		return player.chunkCoordY;
	}

	@Override
	public int getZ() {
		return player.chunkCoordZ;
	}

	private void notifyClient() { // client-only
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen != null && screen instanceof GuiCrafting) {
			((GuiCrafting) screen).pushRecipe(lastRecipe);
		}
	}

	// //////////
	// / NBT

	public void readFromNBT(NBTTagCompound compound) {
		if (compound == null)
			compound = new NBTTagCompound();

		NBTTagList content = compound.getTagList("Contents", 10);
		for (int i = 0; i < content.tagCount() - 1; ++i) { // - 1 because the
															// last entry is the
															// chip
			NBTTagCompound tag = content.getCompoundTagAt(i);
			byte slotIndex = tag.getByte("ContentSlot");
			if (slotIndex >= 0 && slotIndex < gridInv.getSizeInventory()) {
				gridInv.setInventorySlotContents(slotIndex,
						ItemStack.loadItemStackFromNBT(tag));
			}
		}

		NBTTagCompound chipTag = content
				.getCompoundTagAt(content.tagCount() - 1);
		chipInv.setInventorySlotContents(0,
				ItemStack.loadItemStackFromNBT(chipTag));
	}

	public void writeToNBT(ItemStack pad) {
		if (!pad.hasTagCompound())
			pad.setTagCompound(new NBTTagCompound());

		NBTTagCompound oldCompounds = pad.getTagCompound();

		NBTTagList ingredients = new NBTTagList();
		for (int i = 0; i < gridInv.getSizeInventory(); ++i) {
			if (gridInv.getStackInSlot(i) != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("ContentSlot", (byte) i);
				gridInv.getStackInSlot(i).writeToNBT(tag);
				ingredients.appendTag(tag);
			}
		}

		// Save the chip onto the same list
		NBTTagCompound chipTag = new NBTTagCompound();
		chipTag.setByte("chip", (byte) 0);
		if (chipInv.getStackInSlot(0) != null)
			chipInv.getStackInSlot(0).writeToNBT(chipTag);
		ingredients.appendTag(chipTag);

		// Also save the current recipe to the list
		NBTTagCompound recipeTag = new NBTTagCompound();
		if (lastRecipe != null)
			pad.stackTagCompound.setInteger("currentRecipe",
					Item.getIdFromItem(lastRecipe.getResult().getItem()));
		else
			pad.stackTagCompound.setInteger("currentRecipe", -1);

		pad.setTagInfo("Contents", ingredients);
		pad.setTagCompound(oldCompounds);
	}

	/**
	 * The player that currently has the craft opened
	 */
	public EntityPlayer getPlayerOwner() {
		return player;
	}
	/*
	 * NBT Structure:
	 * 
	 * main tag:p_74774_2_ "craftPad": chipInv gridInv outputInv "loadedRecipe"
	 */

}
