package xk.xact.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xk.xact.XActMod;
import xk.xact.client.GuiUtils;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.recipes.RecipeUtils;
import xk.xact.util.Textures;

public class GuiCase extends GuiXACT {

	private static final ResourceLocation guiTexture = new ResourceLocation(
			Textures.GUI_CASE);

	public GuiCase(Container container) {
		super(container);
		this.xSize = 196;
		this.ySize = 191;
	}

	private Slot slot;

	@Override
	protected ResourceLocation getBaseTexture() {
		return guiTexture;
	}
	@Override
	protected void keyTyped(char chartyped, int keyCode) {
		InventoryPlayer invPlayer = Minecraft.getMinecraft().thePlayer.inventory;
		Slot hoverdSlot = GuiUtils.getHoveredSlot(guiLeft, guiTop);
		// Please don't ask me what this mess is
		// I just added if-statements until it stopped crashing
		if (keyCode >= 1 && keyCode < 11) { // 1 - 9
			if (hoverdSlot != null && hoverdSlot.getSlotIndex() <= invPlayer.getSizeInventory() //Ignore keypress if the player tries to switch items from the hotbar with items in the case
				&& hoverdSlot.getHasStack() // Do not handle the number key if the player tries to switch the Case
				&& hoverdSlot.getStack().getItem().equals(XActMod.itemChipCase))
				return;
			if (keyCode - 2 >= 0 && invPlayer.getStackInSlot(keyCode - 2) != null)
				if (invPlayer.getStackInSlot(keyCode - 2).equals(invPlayer.getCurrentItem())) // - 2 because the keycode is 2 ahead (e.g pressing 1 returns 2 and corresponds to inv slot 0)
					return; // Dont handle the number key when it would replace the currently open Case
		}
		super.keyTyped(chartyped, keyCode);
	}
	
	@Override
	protected void drawPostForeground(int x, int y) {
		if (slot != null) {
			drawRecipe(slot.getStack());
		}
	}

	@Override
	public void drawScreen(int x, int y, float p_73863_3_) {
		super.drawScreen(x, y, p_73863_3_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partial, int x, int y) {
		super.drawGuiContainerBackgroundLayer(partial, x, y);
		this.slot = findSlotAt(x, y);
		if (slot != null) {
			this.drawTexturedModalRect(guiLeft + 14, guiTop + 16, 197, 9, 52,
					77);
		}
	}

	// the painted grid:
	// position: (14, 16), size (52x77)
	// texture position: (197, 9)

	// painted slots:
	// main: 31, 16
	// first: 13, 40 (slot size 18x18)

	// to paint the items:
	// func_85044_b(ItemStack stack, int x, int y) // copy this method, as it's
	// private.
	// tooltip: func_74184_a(ItemStack stack, int x, int y) // this one is
	// protected, so don't copy.

	private Slot findSlotAt(int x, int y) {
		Slot slot = getSlotAt(x, y);
		if (slot != null && slot.getHasStack()) {
			if (CraftManager.isEncoded(slot.getStack())) {
				return slot;
			}
		}
		return null;
	}

	protected void drawRecipe(ItemStack chip) {
		if (chip == null)
			return;
		CraftRecipe recipe = CraftManager.decodeRecipe(chip);
		if (recipe == null)
			return;

		ItemStack result = recipe.getResult();
		GuiUtils.paintItem(result, 32, 17, this.mc, itemRender, 200.0F);
		ItemStack[] ingredients = recipe.getIngredients();
		for (int i = 0; i < 3; i++) {
			for (int e = 0; e < 3; e++) {
				int index = i * 3 + e;
				GuiUtils.paintItem(ingredients[index], e * 18 + 14,
						i * 18 + 41, this.mc, itemRender, 200.0F);
			}
		}
	}

}
