package xk.xact.client.gui;

import java.io.IOException;

import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xk.xact.XactMod;
import xk.xact.client.GuiUtils;
import xk.xact.client.button.ButtonAction;
import xk.xact.client.button.ButtonTab;
import xk.xact.client.button.CustomButtons;
import xk.xact.client.button.GuiButtonCustom;
import xk.xact.client.button.ICustomButtonMode;
import xk.xact.config.ConfigurationManager;
import xk.xact.core.items.ItemChip;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.gui.ContainerCrafter;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageUpdateMissingItems;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.util.NumberHelper;
import xk.xact.util.References;
import xk.xact.util.Textures;
import xk.xact.util.Utils;

public class GuiCrafter extends GuiCrafting {

	private static final ResourceLocation guiTexture = new ResourceLocation(References.MOD_ID,
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
		
		// Clear grid
		ButtonAction btncleargrid = new ButtonAction(4, guiLeft + 60, guiTop + 78, 3, 14);
		buttonList.add(buttons[4] = btncleargrid);
		
		// Last recipe
		ButtonAction btnloadlast = new ButtonAction(5, guiLeft + 103, guiTop + 76, 4, 9);
		buttonList.add(buttons[5] = btnloadlast);
		// Next recipe
		ButtonAction btnloadnext = new ButtonAction(6, guiLeft + 103, guiTop + 86, 5, 9);
		buttonList.add(buttons[6] = btnloadnext);
		
		// 3 Quick-Access-Crafter-button-tabs, man thats one heck of a word NYI
//		ButtonTab btnTab1 = new ButtonTab(7, guiLeft - 22, guiTop + 5,  0, 22, container);
//		btnTab1.setTexturePos(0, 40);
//		buttonList.add(buttons[7] = btnTab1);
//		
//		ButtonTab btnTab2 = new ButtonTab(8, guiLeft - 22, guiTop + 28, 0, 22, container);
//		btnTab2.setTexturePos(0, 40);
//		buttonList.add(buttons[8] = btnTab2);
//		
//		ButtonTab btnTab3 = new ButtonTab(9, guiLeft - 22, guiTop + 51, 0, 22, container);
//		btnTab3.setTexturePos(0, 40);
//		buttonList.add(buttons[9] = btnTab3);
		
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
		if (ConfigurationManager.ENABLE_ALT_TEXTURES)
			GuiUtils.drawLights(container, guiTexture, this, guiLeft, guiTop);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		mouseX = x;
		mouseY = y; // Used in guiCrafting
		int gray = NumberHelper.argb(255, 139, 139, 139);

		if (hoveredRecipe == -1) {
			for (int i = 0; i < 9; i++) {
				Slot slot = container.getSlot(8 + i);
				int color = getColorForGridSlot(slot);
				if (slot.getHasStack()) {
					GuiUtils.paintOverlay(slot.xDisplayPosition,
							slot.yDisplayPosition, 16, color);
				}
			}
			RenderHelper.enableGUIStandardItemLighting();
		} else {
			ItemStack hoveredChip = container.crafter.circuits
					.getStackInSlot(hoveredRecipe);
			CraftRecipe hoveredCraftRecipe = CraftManager.decodeRecipe(hoveredChip);

			for (int i = 0; i < 9; i++) {
				Slot slot = container.getSlot(8 + i);
				int color = getColorForGridSlot(slot);
				if (color != -1 && hoveredCraftRecipe != null) {
					// Paint the "ghost item"
					GuiUtils.paintOverlay(slot.xDisplayPosition,
							slot.yDisplayPosition, 16, gray); // Paint gray over
																// the slot so
																// you wont see
																// the old items
					GuiUtils.paintItem(hoveredCraftRecipe.getIngredients()[i],
							slot.xDisplayPosition, slot.yDisplayPosition, mc,
							itemRender, 200F);
					
					// Draw highlight around current recipe
			
					
					GuiUtils.paintOverlay(slot.xDisplayPosition,
							slot.yDisplayPosition, 16, color);
				}
			}
		}
		
		drawRecipeBorder(
				GuiUtils.getHoveredSlot(container, x, y, guiLeft, guiTop),
				getColorForOutputSlot(hoveredRecipe));
	}

	@Override
	protected ResourceLocation getBaseTexture() {
		return guiTexture;
	}

	@Override
	public void drawScreen(int mousex, int mousey, float partialtick) {
		if (partialtick > 0.6) // Reduces updates by i dunno ... a bit
			PacketHandler.INSTANCE
					.sendToServer(new MessageUpdateMissingItems());
		
		super.drawScreen(mousex, mousey, partialtick);
	}

	private int getColorForOutputSlot(int recipeIndex) {
		int color;
		if (recipeIndex < 0)
			return 0;
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
		// This was commented out
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
				if (isPointInRegion(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 17, 17, mouseX, mouseY)) {
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

	private void drawRecipeBorder(Slot hoveredSlot, int color) {
		if (hoveredSlot == null)
			return;
		if (hoveredSlot.slotNumber > 3)
			return;
		int slotLeft = hoveredSlot.xDisplayPosition;
		int slotTop = hoveredSlot.yDisplayPosition;
		int slotBottom = slotTop + 16;
		int slotRight = slotLeft + 16;
	
		drawGradientRect(slotLeft - 5, slotTop - 5, slotLeft - 4,
				slotBottom + 5, color, color);
		drawGradientRect(slotLeft - 4, slotTop - 5, slotRight + 4, slotTop - 4,
				color, color);
		drawGradientRect(slotLeft - 4, slotBottom + 4, slotRight + 4,
				slotBottom + 5, color, color);
		drawGradientRect(slotRight + 5, slotBottom + 5, slotRight + 4,
				slotTop - 5, color, color);

	}

	@Override
	public void handleKeyboardInput() throws IOException {
		handleKeyBinding(container);
		super.handleKeyboardInput();
	}

	// -------------------- InteractiveCraftingGui --------------------

	@Override
	public void sendGridIngredients(ItemStack[] ingredients, int buttonID) {
		if (ingredients == null) {
			GuiUtils.sendItemToServer((byte) -1, null);
			return;
		}
		if (buttonID != -1)
			GuiUtils.sendItemsToServer(ingredients, 4 + buttonID); // 4 because the first chipslot is 4 (and the corresponding  buttonid is 0)
		else
			GuiUtils.sendItemsToServer(ingredients, buttonID);
	}

	@Override
	public void handleKeyBinding(Container container) {
		super.handleKeyBinding(container);
	}

	// -------------------- Buttons --------------------

	private GuiButtonCustom[] buttons = new GuiButtonCustom[10];

	private boolean invalidated = true;

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button instanceof GuiButtonCustom) {
			int action = ((GuiButtonCustom) button).getAction();
			switch (action) {
			case 1: // SAVE
				ItemStack stack = CraftManager.encodeRecipe(crafter
						.getRecipe(4));
				sendGridIngredients(crafter.craftGrid.getContents(), button.id);
				sendGridIngredients(null, 0); // clears the grid
				return;
			case 3: // CLEAR
				GuiUtils.sendItemToServer((byte) (4 + button.id),
						new ItemStack(XactMod.itemRecipeBlank));
				return;
			case 4: // CLEAR GRID
				sendGridIngredients(null, 0);
				return;
			case 5: // Last Recipe
				setRecipe(getPreviousRecipe());
				return;
			case 6: // Next Recipe
				setRecipe(getNextRecipe());
				return;
			case 7: // Tab 1
//				ContainerCrafter adjCrafter = Utils.getAdjacentCrafters(crafter.getPos(), crafter.getWorld(), Minecraft.getMinecraft().thePlayer).get(0);
				//Minecraft.getMinecraft().displayGuiScreen(null);
//				clickBlock(adjCrafter.crafter.getWorld(), Minecraft.getMinecraft().thePlayer, adjCrafter.crafter.getPos(), EnumFacing.UP); // Side doesn't matter
				break;
			}	
		}
	}

	
	private void clickBlock(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		IBlockState blockstate = world.getBlockState(pos);
		Block block = blockstate.getBlock();
		System.out.println(block);
		if (block != Blocks.air)
			block.onBlockActivated(world, pos, blockstate, player, side, 2F, 2F, 2F);
		}
	}

