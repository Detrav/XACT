package xk.xact.client.button;

import net.minecraft.client.Minecraft;
import xk.xact.gui.ContainerCrafter;
import xk.xact.util.Utils;

public class ButtonTab extends ButtonAction {

	private ContainerCrafter crafter;
	
	public ButtonTab(int id, int posX, int posY, int textureindex, int size, ContainerCrafter crafter) {
		super(id, posX, posY, textureindex, size);
		this.crafter = crafter;
	}
	
	@Override
	public boolean isVisible() {
		if (6 + Utils.getAdjacentCrafters(crafter.crafter.getPos(), crafter.crafter.getWorld(), Minecraft.getMinecraft().thePlayer).size() >= this.id)
			return true;
		return false;
	}
}
