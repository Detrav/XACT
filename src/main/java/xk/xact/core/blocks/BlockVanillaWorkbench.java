package xk.xact.core.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import xk.xact.XactMod;
import xk.xact.core.tileentities.TileWorkbench;
import xk.xact.util.Utils;

// the block that replaces the vanilla crafting table
public class BlockVanillaWorkbench extends BlockMachine {

	public BlockVanillaWorkbench(String guiName) {
		super(Material.wood, "xact.workbench", guiName);
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setUnlocalizedName("xact.workbench");
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileWorkbench();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(XactMod.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}


	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode)
			return;
		TileWorkbench workbench = (TileWorkbench) worldIn.getTileEntity(pos);
		if (workbench != null) {
			ItemStack[] inventoryContents = workbench.craftingGrid
					.getContents();
			for (ItemStack current : inventoryContents) {
				if (current == null)
					continue;
				Utils.dropItemAsEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), current);
			}
			inventoryContents = workbench.subGrid.getContents();
			for (ItemStack current : inventoryContents) {
				if (current == null)
					continue;
				Utils.dropItemAsEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), current);
			}
		}
	}

	@Override
	public String getUnlocalizedName() {
		return Blocks.crafting_table.getUnlocalizedName();
	}
}
