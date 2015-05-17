package xk.xact.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xk.xact.XactMod;

public class CreativeTabXACT extends CreativeTabs {

	public CreativeTabXACT() {
		super("xact");
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(XactMod.blockMachine, 1, 0);
	}

	@Override
	public Item getTabIconItem() {

		return Item.getItemFromBlock(XactMod.blockMachine);
	}

}
