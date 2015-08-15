package xk.xact.client.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import xk.xact.client.GuiUtils;

public class ButtonAction extends GuiButtonCustom {
	private int index;
	
	private int xTexture = 14;
	private int yTexture = 0;
	public ButtonAction(int id, int posX, int posY, int textureindex, int size) {
		super(id, posX, posY, size, size);
		this.index = textureindex;
		this.mode = ICustomButtonMode.ItemModes.NORMAL;
	}

	@Override
	protected void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY) {
		if (isVisible()) {
			GuiUtils.bindTexture(TEXTURE_BUTTONS);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					index * xTexture, yTexture, this.width, this.height);
			

		}
	}
	
	public void setTexturePos(int x, int y) {
		xTexture = x;
		yTexture = y;
	}
	
	@Override
	public boolean isVisible() {
		return mode.equals(ICustomButtonMode.ItemModes.NORMAL);
	}
	
	@Override
	protected boolean isModeValid(ICustomButtonMode mode) {
		return mode.equals(ICustomButtonMode.DeviceModes.INACTIVE) || mode.equals(ICustomButtonMode.ItemModes.NORMAL);
	}
	
	@Override
	public int getAction() {
		return id;
	}

}