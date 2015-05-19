package xk.xact.client.button;

import org.lwjgl.opengl.GL11;

import xk.xact.client.GuiUtils;
import net.minecraft.client.Minecraft;

public class ButtonAction extends GuiButtonCustom{
	int index;
	
	public ButtonAction(int id, int posX, int posY, int textureindex, int size) {
		super(id, posX, posY, size, size);
		this.index = textureindex;
	}

	@Override
	protected void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY) {
		GuiUtils.bindTexture(TEXTURE_BUTTONS);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(this.xPosition, this.yPosition,
				index * 14, 0, this.width, this.height);
	}

	@Override
	protected boolean isModeValid(ICustomButtonMode mode) {
		return false;
	}
	
	@Override
	public int getAction() {
		return id;
	}

}
