package xk.xact.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xk.xact.XActMod;
import xk.xact.client.GuiUtils;
import xk.xact.client.button.CustomButtons;
import xk.xact.client.button.GuiButtonCustom;
import xk.xact.client.button.ICustomButtonMode;
import xk.xact.core.items.ItemChip;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.gui.ContainerCrafter;
import xk.xact.gui.SlotCraft;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageUpdateMissingItems;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.util.NumberHelper;
import xk.xact.util.References;
import xk.xact.util.Textures;
import xk.xact.util.Utils;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiCrafter extends GuiCrafting {

	private static final ResourceLocation guiTexture = new ResourceLocation(
			Textures.GUI_CRAFTER);
	
	private TileCrafter crafter;
	private ContainerCrafter container;
	private boolean areSlotsHidden = false;
	public GuiCrafter(TileCrafter crafter, EntityPlayer player) {
		super(new ContainerCrafter(crafter, player));
		this.crafter = crafter;
		this.container = (ContainerCrafter) super.inventorySlots;
		this.ySize = 256;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		updateGhostContents(-1);
		/*
		 * Buttons: 42, 21. 120, 21 42, 65. 120, 65
		 */
		buttonList.clear();

		for (int i = 0; i < 4; i++) {
			int x = (i % 2 == 0 ? 42 : 120) + this.guiLeft;
			int y = (i / 2 == 0 ? 21 : 65) + this.guiTop;

			GuiButtonCustom button = CustomButtons.createdDeviceButton(x, y);
			button.id = i;
			buttonList.add(buttons[i] = button);
		}
		invalidated = true;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (invalidated || crafter.recentlyUpdated) {

			for (int i = 0; i < 4; i++) {
				ItemStack chip = crafter.circuits.getStackInSlot(i);
				if (chip == null) {
					buttons[i].setMode(ICustomButtonMode.DeviceModes.INACTIVE);
					continue;
				}

				if (chip.getItem() instanceof ItemChip) {
					if (!((ItemChip) chip.getItem()).encoded) {
						CraftRecipe mainRecipe = crafter.getRecipe(4); // the
																		// recipe
																		// on
																		// the
																		// grid
						if (mainRecipe != null && mainRecipe.isValid()) {
							buttons[i]
									.setMode(ICustomButtonMode.DeviceModes.SAVE);
							continue;
						}
						buttons[i]
								.setMode(ICustomButtonMode.DeviceModes.INACTIVE);
						continue;
					}
					buttons[i].setMode(ICustomButtonMode.DeviceModes.CLEAR);
				}
			}
			invalidated = false;
			crafter.recentlyUpdated = false;
		}
	}

	@Override
	protected void drawTitle() {
		String localizedTitle = I18n
				.format(References.Localization.CRAFTER_TITLE);
		int xPos = (this.xSize - fontRendererObj.getStringWidth(localizedTitle)) / 2;
		this.fontRendererObj.drawString(localizedTitle, xPos, 6, 4210752);
		this.fontRendererObj.drawString(
				I18n.format(References.Localization.CRAFTER_INVENTORY), 8,
				this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int mouseX,
			int mouseY) {
		super.drawGuiContainerBackgroundLayer(var1, mouseX, mouseY);
		// Draw crafting grid
		int currentRecipe = getHoveredRecipe(mouseX, mouseY);
		if (hoveredRecipe != currentRecipe) {
			updateGhostContents(currentRecipe);
		}
		//Draw higlight around current recipe
		int blue = NumberHelper.argb(128, 0, 0, 255);
		if (hoveredRecipe != -1) {
			Slot hovered   = container.getSlot(hoveredRecipe);
			int slotLeft   = guiLeft + hovered.xDisplayPosition;
			int slotTop    = guiTop + hovered.yDisplayPosition;
			int slotBottom = slotTop + 16;
			int slotRight  = slotLeft + 16;
			drawGradientRect(slotLeft - 5, slotTop - 5, slotLeft - 4, slotBottom + 5, blue, blue);
			drawGradientRect(slotLeft - 4, slotTop - 5, slotRight + 4, slotTop - 4, blue, blue);
			drawGradientRect(slotLeft - 4, slotBottom + 4, slotRight + 4, slotBottom + 5, blue, blue);
			drawGradientRect(slotRight + 5, slotBottom + 5, slotRight + 4, slotTop - 5, blue, blue);
		}
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		int gray = NumberHelper.argb(128, 139, 139, 139);
		if (hoveredRecipe == -1) {
			for (int i = 0; i < 9 ;i++) {
				Slot slot = container.getSlot(8 + i);
				if (slot.getHasStack()) {
					GuiUtils.paintOverlay(slot.xDisplayPosition, slot.yDisplayPosition, 16, gray);
				}
			}
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Override
	protected ResourceLocation getBaseTexture() {
		return guiTexture;
	}

	@Override
	public void drawScreen(int mousex, int mousey, float partialtick) {
		if (partialtick > 0.6) // only update less t
			PacketHandler.INSTANCE.sendToServer(new MessageUpdateMissingItems());
		if (hoveredRecipe < 0) {
			if (areSlotsHidden) {
				for (int i = 0; i < 9; i++) {
					Slot slot = container.getSlot(8 + i);
					slot.xDisplayPosition -= 9000; // Dirty way to hide a slot plz don't hit me
				}
				areSlotsHidden = false;
			}
			
			super.drawScreen(mousex, mousey, partialtick);
		} else {// If a recipe is being hovered, paint those ingredients
				// instead.
			int leftGridOffset = 62;
			int topGridOffset = 17;
			int index = 0;

			// Draw Ghost recipe & missing item red rectangle
			for (int yOffset = 0; yOffset < 3; yOffset++) {
				for (int xOffset = 0; xOffset < 3; xOffset++) {
					int x = guiLeft + leftGridOffset + (18 * xOffset);
					int y = guiTop + topGridOffset + (18 * yOffset);
					ItemStack itemToPaint = gridContents[index];
					GuiUtils.paintItem(itemToPaint, x, y, this.mc, itemRender, 500.0F);
					index++;
				}
			}
			for (int i = 0; i < 9; i++) {
				Slot slot = container.getSlot(8 + i);
				if (slot != null && slot instanceof SlotCraft) {
					((SlotCraft) slot).canTakeStack(mc.thePlayer);
				}
				int color = getColorForGridSlot(slot);
				if (color != -1)// Paint the "ghost item"
					GuiUtils.paintOverlay(guiLeft + slot.xDisplayPosition,
							guiTop + slot.yDisplayPosition, 16, color);
			}
			if (!areSlotsHidden) {
				for (int i = 0; i < 9; i++) {
					Slot slot = container.getSlot(8 + i);
					slot.xDisplayPosition += 9000; // Dirty way to hide a slot plz don't hit me
				}
				areSlotsHidden = true;
			}
			
			super.drawScreen(mousex, mousey, partialtick);
		}	
	}private ItemStack[] tempRecipeStorage; // Stores the recipe while a recipe from a chip is displayed
	

	private int getColorForOutputSlot(int recipeIndex) {
		int color;
		if (this.mc.thePlayer.capabilities.isCreativeMode) {
			color = GuiUtils.COLOR_BLUE;
		} else if (Utils.anyOf(container.recipeStates[recipeIndex])) {
			color = GuiUtils.COLOR_RED;
		} else {
			color = GuiUtils.COLOR_GREEN;
		}
		color |= TRANSPARENCY; // transparency layer.

		return color;
	}

	private int getColorForGridSlot(Slot slot) {
		// ItemStack itemInSlot = slot.getStack();
		// if( itemInSlot != null && itemInSlot.stackSize > 0 ) {
		// return -1; // no overlay when the slot contains "real" items.
		// }

		int index = slot.slotNumber - 8;
		boolean[] missingIngredients = container.recipeStates[hoveredRecipe == -1 ? 4
				: hoveredRecipe];
		int color = missingIngredients[index] ? GuiUtils.COLOR_RED
				: GuiUtils.COLOR_GRAY;
		return color | TRANSPARENCY;
	}

	private int getHoveredRecipe(int mouseX, int mouseY) {
		for (int i = 0; i < 4; i++) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);

			if (slot != null && slot.getHasStack()) {
				if (isPointInRegion(guiLeft + slot.xDisplayPosition - 1, guiTop
						+ slot.yDisplayPosition - 1, 17, 17, mouseX, mouseY)) {
					return i;
				}
			}
		}
		return -1;
	}

	private int hoveredRecipe = -1;
	private final ItemStack[] emptyGrid = new ItemStack[9];
	public ItemStack[] gridContents = emptyGrid;
	private static final int TRANSPARENCY = 128 << 24; // 50%

	private void updateGhostContents(int newIndex) {
		this.hoveredRecipe = newIndex == -1 ? -1 : newIndex % 4;
		if (hoveredRecipe != -1) {
			CraftRecipe recipe = crafter.getRecipe(hoveredRecipe);
			gridContents = recipe == null ? emptyGrid : recipe.getIngredients();
		}
	}

	// -------------------- InteractiveCraftingGui --------------------

	@Override
	public void sendGridIngredients(ItemStack[] ingredients, int buttonID) {
		if (ingredients == null) {
			GuiUtils.sendItemToServer((byte) -1, null);
			return;
		}
		GuiUtils.sendItemsToServer(ingredients, 4 + buttonID); // 4 because the
																// first
																// chipslot is 4
																// (and the
																// corresponding
																// buttonid is
																// 0)
	}

	// -------------------- Buttons --------------------

	private GuiButtonCustom[] buttons = new GuiButtonCustom[4];

	private boolean invalidated = true;

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button instanceof GuiButtonCustom) {
			int action = ((GuiButtonCustom) button).getAction();
			if (action == 1) { // SAVE
				ItemStack stack = CraftManager.encodeRecipe(crafter
						.getRecipe(4));
				sendGridIngredients(crafter.craftGrid.getContents(), button.id);
				container.setStack(-1, null); // clears the grid
				return;
			}
			if (action == 3) { // CLEAR
				GuiUtils.sendItemToServer((byte) (4 + button.id),
						new ItemStack(XActMod.itemRecipeBlank));
			}
		}
	}

}
