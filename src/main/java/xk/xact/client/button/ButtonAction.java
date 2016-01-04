package xk.xact.client.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import xk.xact.client.GuiUtils;
import xk.xact.client.gui.GuiCrafter;

public class ButtonAction extends GuiButtonCustom {
	private int index;
	
	private int xTexture = 14;
	private int yTexture = 0;
	
	public ButtonAction(int id, int posX, int posY, int textureindex, int size) {
		super(id, posX, posY, size, size);
		this.index = textureindex;
		this.mode = ICustomButtonMode.ItemModes.NORMAL;
	}
	
	
	public ButtonAction(int id, int posX, int posY, int size, int textureX, int textureY) {
		super(id, posX, posY, size, size);
		this.index = 1;
		this.xTexture = textureX;
		this.yTexture = textureY;
		this.mode = ICustomButtonMode.ItemModes.NORMAL;
	}
	
	@Override
	protected void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY) {
		if (isVisible()) {
			GuiUtils.bindTexture(TEXTURE_BUTTONS);
			RenderHelper.disableStandardItemLighting();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					index * xTexture, yTexture, this.width, this.height);
			RenderHelper.enableGUIStandardItemLighting();
			
			if (this.id == 10) {
				GuiScreen screen = mc.currentScreen;
				if (screen != null && screen instanceof GuiCrafter) {
					((GuiCrafter) screen).getItemRenderer().zLevel = 100.0F;
					((GuiCrafter) screen).getItemRenderer().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(Items.name_tag), this.xPosition + 3, this.yPosition + 2);
					((GuiCrafter) screen).getItemRenderer().zLevel = 0.0F;
				}
			}
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