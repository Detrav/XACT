package xk.xact.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import xk.xact.client.GuiUtils;

/**
 * Base class for all the Gui components in XACT
 *
 * @author Xhamolk_
 */
public abstract class GuiXACT extends GuiContainer {

	protected int mouseX = 0;
	protected int mouseY = 0;

	public GuiXACT(Container container) {
		super(container);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
		super.mouseClicked(x, y, mouseButton);
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		drawTitle();
		drawPostForeground(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partial, int x, int y) {
		drawBaseTexture();
		drawToolTip();
	}

	public String getGuiTitle() {
		return null;
	}

	protected abstract ResourceLocation getBaseTexture();

	protected void drawBaseTexture() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtils.bindTexture(getBaseTexture());
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize,
				this.ySize);
	}

	protected void drawPostForeground(int x, int y) {
	}

	protected void drawTitle() {
		String title = getGuiTitle();
		if (title != null && !title.isEmpty()) {
			int xPos = (this.xSize - fontRendererObj.getStringWidth(title)) / 2;
			this.fontRendererObj.drawString(title, xPos, 8, 4210752);
		}
	}

	protected void drawToolTip() {
	}

	protected int getSlotDimensions(Slot slot) {
		return 16; // Friggin usefull
	}

	protected boolean isPointInRegion(int minx, int miny, int xDim, int yDim,
			int mouseX, int mouseY) {
		if (mouseX >= minx && mouseX <= minx + xDim && (mouseY) >= miny
				&& (mouseY) <= miny + yDim) {

			return true;
		}
		return false;
	}

	// ---------------------------- Util ----------------------------

	public boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		int dim = getSlotDimensions(slot);
		return this.isPointInRegion(slot.xDisplayPosition,
				slot.yDisplayPosition, dim, dim, mouseX - guiLeft, mouseY
						- guiTop);
	}

	public Slot getSlotAt(int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
			if (isMouseOverSlot(slot, x, y))
				return slot;
		}

		return null;
	}

}
