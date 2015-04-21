package xk.xact.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xk.xact.XActMod;
import xk.xact.core.Machines;

/**
 *
 *
 */
public class ItemMachine extends ItemBlock {

	public ItemMachine(Block block) {
		super(block);
		this.setHasSubtypes( true );
		this.setCreativeTab( XActMod.xactTab );
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return "tile.xact.machine." + Machines.getMachineName( itemStack );
	}

}
