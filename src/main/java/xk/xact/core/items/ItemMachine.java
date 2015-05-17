package xk.xact.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xk.xact.XactMod;
import xk.xact.core.Machines;
import xk.xact.util.References;

/**
 *
 *
 */
public class ItemMachine extends ItemBlock {

	public ItemMachine(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setCreativeTab(XactMod.xactTab);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return References.MOD_ID + ":tile.machine."
				+ Machines.getMachineName(itemStack);
	}

}
