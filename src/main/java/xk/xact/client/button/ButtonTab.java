package xk.xact.client.button;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import xk.xact.XactMod;
import xk.xact.api.INameable;
import xk.xact.client.GuiUtils;
import xk.xact.gui.ContainerCrafter;
import xk.xact.util.Utils;
import xk.xact.util.References.Localization;

public class ButtonTab extends ButtonAction {

	private ContainerCrafter crafter;
	private RenderItem itemRender;
	private Gui parent;
	private String tooltip;
	
	public ButtonTab(int id, int posX, int posY, int textureindex, int size, ContainerCrafter crafter, RenderItem itemRender, Gui parent) {
		super(id, posX, posY, textureindex, size);
		this.crafter = crafter;
		this.itemRender = itemRender;
		this.parent = parent;
	}
	
	@Override
	public boolean isVisible() {
		List<INameable> crafters = Utils.getAdjacentCrafters(crafter.crafter.xCoord, crafter.crafter.yCoord, crafter.crafter.zCoord, crafter.crafter.getWorld(), Minecraft.getMinecraft().thePlayer);
		
		if (6 + crafters.size() >= this.id) {
			if (crafters.get(id - 7).hasName())
				tooltip = crafters.get(id - 7).getName();
			
			return true;
		}
			
		return false;
	}

	
	@Override
	protected void drawForegroundLayer(Minecraft mc, int mouseX, int mouseY) {
		super.drawForegroundLayer(mc, mouseX, mouseY);
		GuiUtils.paintItem(new ItemStack(XactMod.blockMachine), xPosition + 3, yPosition + 3, Minecraft.getMinecraft(), itemRender, 0.0F);	
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		
		super.drawButton(mc, mouseX, mouseY);
	}
	
	public void setToolTip(String text) {
		tooltip = text;
	}
	
	public String getToolTip() {
		return tooltip != null ? tooltip : I18n.format(Localization.TOOLTIP_ADJACENTCRAFTER);
	}
}
