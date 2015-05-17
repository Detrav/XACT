package xk.xact.core.items;

import xk.xact.XactMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class XactBaseItem extends Item {
	private String itemName;
	
	public XactBaseItem(String itemName) {
		this.itemName = itemName;
		setUnlocalizedName(itemName);
		this.setCreativeTab(XactMod.xactTab);
	}
	
	public void init() {
		ModelResourceLocation mdlResource = new ModelResourceLocation(itemName, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, mdlResource);
	}
}
