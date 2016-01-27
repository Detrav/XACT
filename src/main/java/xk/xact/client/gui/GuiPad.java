package xk.xact.client.gui;

import javafx.scene.input.MouseButton;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import xk.xact.XactMod;
import xk.xact.client.GuiUtils;
import xk.xact.client.button.CustomButtons;
import xk.xact.client.button.GuiButtonCustom;
import xk.xact.client.button.ICustomButtonMode;
import xk.xact.config.ConfigurationManager;
import xk.xact.core.CraftPad;
import xk.xact.core.items.ItemChip;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageSwitchItems;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.util.References;
import xk.xact.util.Textures;
import xk.xact.util.Utils;

public class GuiPad extends GuiCrafting {

	private static final ResourceLocation guiTexture = new ResourceLocation(
			Textures.GUI_PAD);

	private CraftPad craftPad;
	private Container container;
	private boolean[] missingIngredients = new boolean[9];

	public GuiPad(CraftPad pad, Container container) {
		super(container);
		this.container = container;
		this.ySize = 180;
		this.craftPad = pad;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		super.buttonList.clear();
		this.button = CustomButtons.createdDeviceButton(this.guiLeft + 97,
				this.guiTop + 63);
		button.id = 0;
		buttonList.add(button);
	}

	@Override
	protected ResourceLocation getBaseTexture() {
		return guiTexture;
	}

	@Override
	protected void drawTitle() {
		ItemStack pad = craftPad.getPlayerOwner().getCurrentEquippedItem();
		String guiTitle = I18n
				.format(References.Localization.CRAFTPAD_GUITITLE);
		if (pad != null && pad.hasDisplayName())
			guiTitle = pad.getDisplayName();

		int xPos = 11 + (112 - fontRendererObj.getStringWidth(guiTitle)) / 2;

		if (fontRendererObj.getStringWidth(guiTitle) >= 103) // If the custom
																// name is too
																// long split it
			this.fontRendererObj.drawSplitString(guiTitle, 18, 5, 105, 4210752);
		else
			this.fontRendererObj.drawString(guiTitle, xPos, 8, 4210752);

		xPos = 126 + (40 - fontRendererObj.getStringWidth(I18n
				.format(References.Localization.CRAFTPAD_CHIPTITLE))) / 2;
		this.fontRendererObj.drawString(
				I18n.format(References.Localization.CRAFTPAD_CHIPTITLE), xPos,
				23, 4210752);
	}

	@Override
	public void drawScreen(int x, int y, float partialTick) {
		// Update the missing ingredients
		if ((int) (partialTick * 10) == 1)
			missingIngredients = craftPad.getMissingIngredients();
		super.drawScreen(x, y, partialTick);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		// Draw either red highlight or gray
		super.drawGuiContainerForegroundLayer(x, y);
		for (int i = 1; i < 10; i++) {
			Slot slot = container.getSlot(i);
			int slotIndex = slot.slotNumber;
			if (0 < slotIndex && slotIndex <= 9) { // grid slots
				int color = missingIngredients[slotIndex - 1] ? GuiUtils.COLOR_RED
						: GuiUtils.COLOR_GRAY;
				color |= 128 << 24; // transparency
				GuiUtils.paintSlotOverlay(slot, 16, color, 0, 0);
			}
			RenderHelper.enableGUIStandardItemLighting();
		}

		mouseX = x;
		mouseY = y;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partial, int x, int y) {
		super.drawGuiContainerBackgroundLayer(partial, x, y);
		if (ConfigurationManager.ENABLE_ALT_TEXTURES)
			GuiUtils.drawLights(container, guiTexture, this, guiLeft, guiTop);
	}

	// title: (43,8) size: 88x12

	// button position: 97, 63. size: 14x14
	// button texture: (14*i +0, 176)

	@Override
	protected void keyTyped(char chartyped, int keyCode) {
		InventoryPlayer invPlayer = Minecraft.getMinecraft().thePlayer.inventory;
		Slot hoverdSlot = GuiUtils.getHoveredSlot(container, mouseX, mouseY,
				guiLeft, guiTop);
		// Please don't ask me what this mess is
		// I just added if-statements until it stopped crashing
		if (keyCode >= 1 && keyCode < 11) { // 1 - 9
			if (hoverdSlot != null
					&& hoverdSlot.getSlotIndex() <= invPlayer
							.getSizeInventory() // Ignore keypress if the player
												// tries to switch items from
												// the hotbar with items in the
												// pad
					&& hoverdSlot.getHasStack() // Do not handle the number key
												// if the player tries to switch
												// the pad
					&& hoverdSlot.getStack().getItem()
							.equals(XactMod.itemCraftPad))
				return;
			if (keyCode - 2 >= 0
					&& invPlayer.getStackInSlot(keyCode - 2) != null)
				if (invPlayer.getStackInSlot(keyCode - 2).equals(
						invPlayer.getCurrentItem())) // - 2 because the keycode
														// is 2 ahead (e.g
														// pressing 1 returns 2
														// and corresponds to
														// inv slot 0)
					return; // Dont handle the number key when it would replace
							// the currently open pad
		}
		super.keyTyped(chartyped, keyCode);
	}

	@Override
	public void sendGridIngredients(ItemStack[] ingredients, int buttonID) {
		if (ingredients == null) {
			GuiUtils.sendItemToServer((byte) -1, null);
			return;
		}
		if (buttonID != -1)
			GuiUtils.sendItemsToServer(ingredients, 1);
		else
			GuiUtils.sendItemsToServer(ingredients, -1);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		// Preventing the player from using the pad when falling
		if (mc.thePlayer.fallDistance > 0 && !mc.thePlayer.capabilities.isCreativeMode) {
			super.onGuiClosed();
			mc.mouseHelper.grabMouseCursor();
			mc.setIngameFocus();
			Utils.writeCraftPadInfo(mc.thePlayer, mc);
		}

		if (craftPad.recentlyUpdated) {
			// Update the buttons for the chips
			ItemStack chip = craftPad.chipInv.getStackInSlot(0);
			if (chip == null) {
				button.setMode(ICustomButtonMode.DeviceModes.INACTIVE);

			} else if (chip.getItem() instanceof ItemChip) {
				if (!((ItemChip) chip.getItem()).encoded) {
					CraftRecipe mainRecipe = craftPad.getRecipe(0); // the
																	// recipe on
																	// the grid
					if (mainRecipe != null && mainRecipe.isValid()) {
						button.setMode(ICustomButtonMode.DeviceModes.SAVE);

					} else {
						button.setMode(ICustomButtonMode.DeviceModes.INACTIVE);
					}
				} else {
					button.setMode(ICustomButtonMode.DeviceModes.CLEAR);
				}
			}
			craftPad.recentlyUpdated = false;
		}

	}

	// /////////////
	// /// Buttons

	private GuiButtonCustom button;

	@Override
	protected void actionPerformed(GuiButton button) {
		
		if (button instanceof GuiButtonCustom) {
			int action = ((GuiButtonCustom) button).getAction();
			System.out.println(craftPad.getRecipe(0));
			if (action == 1) { // SAVE
				ItemStack stack = CraftManager.encodeRecipe(craftPad.getRecipe(0));
				GuiUtils.sendItemToServer((byte) (button.id + 10), stack);
				return;
			}
			if (action == 3) { // CLEAR
				GuiUtils.sendItemToServer((byte) (button.id + 10), new ItemStack(XactMod.itemRecipeBlank));
			}
		}
	}

	@Override
	public void onGuiClosed() {
		ItemStack pad = craftPad.getPlayerOwner().getHeldItem();
		if (pad == null) {
			Utils.logError("That CrafPad was null! Not saving changes to item. Player: %s", craftPad.getPlayerOwner());
			return;
		}

		byte slotIdPad = (byte) craftPad.getPlayerOwner().inventory.currentItem;
		pad.setItemDamage(0);
		if (pad.stackTagCompound != null) {
			byte slotToSwitch = (byte) (pad.stackTagCompound.getByte("originalSlot"));
			if (slotToSwitch != 0) {
				ItemStack stack = craftPad.getPlayerOwner().inventory.getStackInSlot(slotToSwitch - 1);
				pad.stackTagCompound.setByte("originalSlot", (byte) 0);
				PacketHandler.INSTANCE.sendToServer(new MessageSwitchItems(pad, slotToSwitch - 1, stack, slotIdPad));
			}
		}
		super.onGuiClosed();
	}
}
