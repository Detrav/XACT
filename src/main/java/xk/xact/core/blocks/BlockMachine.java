package xk.xact.core.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xk.xact.XActMod;
import xk.xact.core.Machines;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.core.tileentities.TileMachine;
import xk.xact.util.Utils;

/**
 * @author Xhamolk_
 */
public class BlockMachine extends BlockContainer {

	public BlockMachine() {
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setHardness(2.0f);
		this.setResistance(1.5f);
		this.setCreativeTab(XActMod.xactTab);
	}
	

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EntityPlayer player = (EntityPlayer) placer;
		int side = sideByAngles(player, pos.getX(), pos.getZ());
		int frontSide = side / 2 - 1;
		int metadata = ((stack.getItemDamage() & 0x7) << 1)
				| (frontSide & 1);
		//worldIn.setBlockMetadataWithNotify(x, y, z, metadata, 3);
		//TODO: blockstates
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}
		if (!worldIn.isRemote)
			// player.openGui( XActMod.instance, 0, world, x, y, z );
			FMLNetworkHandler.openGui(playerIn, XActMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode)
			return;

		TileMachine entity = (TileMachine) worldIn.getTileEntity(pos);
		if (entity != null) {
			for (ItemStack stack : entity.getDropItems()) {
				if (stack != null)
					Utils.dropItemAsEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}
	

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileMachine) {
			((TileMachine) tileEntity).onBlockUpdate(0); // Trigger update on
															// next update tick.
		}
		if (tileEntity != null && tileEntity instanceof TileMachine) {
			((TileMachine) tileEntity).onBlockUpdate(1); // Tile E
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
			IBlockState state, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(this, 1, Machines
				.getMachineFromMetadata(state)));
		return list;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileCrafter();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < Machines.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	private int sideByAngles(EntityPlayer player, int x, int z) {
		double Dx = player.posX - x;
		double Dz = player.posZ - z;
		double angle = Math.atan2(Dz, Dx) / Math.PI * 180 + 180;

		if (angle < 45 || angle > 315)
			return 4;
		else if (angle < 135)
			return 2;
		else if (angle < 225)
			return 5;
		else
			return 3;
	}

	// /////////////
	// /// Textures
//	@SideOnly(Side.CLIENT)
//	private static IIcon[][] textures;
//
//	@Override
//	public IIcon getIcon(int side, int metadata) {
//		int machine = Machines.getMachineFromMetadata(metadata);
//		if (side >= 2) {
//			side = isFrontSide(side, metadata) ? 2 : 3;
//		}
//		return textures[machine][side];
//	}
//
//	private boolean isFrontSide(int side, int metadata) {
//		return side / 2 - 1 == (metadata & 1);
//	}
//
//	@Override
//	public void registerBlockIcons(IIconRegister iconRegister) {
//		textures = new IIcon[Machines.values().length][4];
//
//		// For each machine: bottom, top, front side, other side.
//		for (int machine = 0; machine < Machines.values().length; machine++) {
//			String[] textureFiles = Machines.getTextureFiles(machine);
//			for (int side = 0; side < 4; side++) {
//				textures[machine][side] = iconRegister
//						.registerIcon(textureFiles[side]);
//			}
//		}
//	}

}
