package xk.xact.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import xk.xact.api.CraftingHandler;
import xk.xact.api.ICraftingDevice;
import xk.xact.config.ConfigurationManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.util.Utils;

/**
 * The slot used to display the recipe's output on TileCrafter.
 *
 * @author Xhamolk_
 */
public class SlotCraft extends SlotCrafting {

	private CraftingHandler handler;
	private ICraftingDevice device;
	private EntityPlayer player;
	private IInventory matrix;
	public SlotCraft(ICraftingDevice device, IInventory displayInventory,
			EntityPlayer player, int index, int x, int y) {
		super(player, displayInventory, displayInventory, index, x, y);
		this.player = player;
		this.device = device;
		this.handler = device.getHandler();
		this.matrix = displayInventory;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}

	@Override
	public ItemStack getStack() {

		return getCraftedStack();
	}

	public ItemStack getCraftedStack() {
		CraftRecipe recipe = getRecipe();
		if (recipe == null)
			return null;
//		if (device.getWorld().isRemote)
//			return getStack(); // Client-side, only show.

		InventoryCrafting grid = handler.generateTemporaryCraftingGridFor(
				recipe, player, false);
		ItemStack craftedItem = handler.getRecipeResult(recipe, grid);

		return Utils.copyOf(craftedItem);
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		return this.getStack();
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		if (player != null && player.capabilities.isCreativeMode && ConfigurationManager.ENABLE_FREECRAFTING)
			return getHasStack();

		return device.canCraft(getSlotIndex());
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack craftedItem) {
		if (player.capabilities.isCreativeMode && ConfigurationManager.ENABLE_FREECRAFTING || craftedItem == null) {
			FMLCommonHandler.instance().firePlayerCraftingEvent(player, craftedItem, matrix);
			super.onCrafting(craftedItem);
			return;
		}

		CraftRecipe recipe = getRecipe();
		if (recipe == null)
			return;


		handler.doCraft(recipe, player, craftedItem);
		FMLCommonHandler.instance().firePlayerCraftingEvent(player, craftedItem, matrix);
		super.onCrafting(craftedItem);
	}

	public CraftRecipe getRecipe() {
		try {
			return device.getRecipe(getSlotIndex());
		} catch (Exception e) {
			return null;
		}
	}

}
