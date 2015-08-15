package xk.xact.core.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
import xk.xact.XactMod;
import xk.xact.core.Machines;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.core.tileentities.TileMachine;
import xk.xact.util.References;
import xk.xact.util.Utils;

/**
 * @author Xhamolk_
 */
public class BlockMachine extends BlockContainer {
	String blockName;
	public int guiID = -1;
	
	public BlockMachine(Material materialIn, String unlocalizedName, String guiName) {
		super(materialIn);
		setCreativeTab(XactMod.xactTab);
		setHardness(0.5F);
		blockName = unlocalizedName;
		setUnlocalizedName(unlocalizedName);
		guiID = 0; // Only one machine
	}
	

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EntityPlayer player = (EntityPlayer) placer;
		int side = sideByAngles(player, pos.getX(), pos.getZ()); /*
																	2 = south
																	5 = west
																	3 = north
																	4 = east
		 														 */
		switch(side) {
			case 2:
				worldIn.setBlockState(pos, getDefaultState().withProperty(References.FACING, EnumFacing.NORTH), 2);
				break;
		}
		System.out.println(side);
		//TODO: blockstates
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return false;
		
		if (!worldIn.isRemote)
			FMLNetworkHandler.openGui(playerIn, XactMod.instance, guiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
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

	@Override
	public int getRenderType() {
		return 3;
	}
	
	// /////////////
	// /// Textures
	
	public void addRender() {
		if (XactMod.proxy.isClient()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(blockName, "inventory"));
		}
	}
	
	public void postInit() {
		if (XactMod.proxy.isClient()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(blockName, "inventory"));	
		}
	}

}
