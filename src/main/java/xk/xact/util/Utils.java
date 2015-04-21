package xk.xact.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import xk.xact.XActMod;
import xk.xact.config.ConfigurationManager;
import xk.xact.inventory.InventoryUtils;
import cpw.mods.fml.common.FMLCommonHandler;

public class Utils {

	public static void notifyPlayer(EntityPlayer player, String message) {
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	public static void debug(String message, Object... data) {
		if( ConfigurationManager.DEBUG_MODE )
			XActMod.logger.fine( String.format( message, data ) );

	}

	public static void log(String message, Object... data) {
		XActMod.logger.info( String.format( message, data ) );
	}

	public static void logError(String message, Object... data) {
		XActMod.logger.warning( String.format( message, data ) );
	}

	public static void logException(String string, Exception exception, boolean stopGame) {
		XActMod.logger.log( Level.SEVERE, string, exception );
		if( stopGame )
			FMLCommonHandler.instance().getSidedDelegate().haltGame( string, exception );
	}

	/**
	 * The description of the ItemStack passed.
	 * Includes the stack size and the 'display' name.
	 * <p/>
	 * Example: 64x Redstone Dust
	 *
	 * @param stack the item stack.
	 * @return the description of the stack's contents. Or "null" if the stack is null.
	 */
	public static String stackDescription(ItemStack stack) {
		if( stack == null )
			return "null";

		return stack.stackSize + "x " + stack.getItem().getItemStackDisplayName( stack );
	}

	public static ItemStack copyOf(ItemStack stack) {
		if( stack == null )
			return null;
		return stack.copy();
	}

	public static boolean equalsStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem()
				&& (!stack1.getHasSubtypes() || stack1.getItemDamage() == stack2.getItemDamage())
				&& ItemStack.areItemStackTagsEqual( stack1, stack2 );
	}

	/**
	 * Drops an item on the world as an EntityItem.
	 *
	 * @param itemStack the ItemStack to drop. Shall not be null.
	 */
	public static void dropItemAsEntity(World world, int x, int y, int z, ItemStack itemStack) {
		Random random = new Random();
		float var10 = random.nextFloat() * 0.8F + 0.1F;
		float var11 = random.nextFloat() * 0.8F + 0.1F;
		EntityItem item;

		for( float var12 = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld( item ) ) {
			int var13 = random.nextInt( 21 ) + 10;

			if( var13 > itemStack.stackSize ) {
				var13 = itemStack.stackSize;
			}

			itemStack.stackSize -= var13;
			item = new EntityItem( world, (double) (x + var10), (double) (y + var11), (double) (z + var12), new ItemStack( itemStack.getItem(), var13, itemStack.getItemDamage() ) );
			float var15 = 0.05F;
			item.motionX = (random.nextGaussian() * var15);
			item.motionY = (random.nextGaussian() * var15 + 0.2F);
			item.motionZ = (random.nextGaussian() * var15);

			if( itemStack.hasTagCompound() ) {
				item.getEntityItem().setTagCompound( (NBTTagCompound) itemStack.getTagCompound().copy() );
			}
		}
	}

	public static ItemStack[] copyArray(ItemStack... oldArray) {
		int length = oldArray.length;
		ItemStack[] newArray = new ItemStack[length];
		for( int i = 0; i < length; i++ ) {
			newArray[i] = oldArray[i] == null ? null : oldArray[i].copy();
		}
		return newArray;
	}

	public static void writeItemStackToNBT(NBTTagCompound compound, ItemStack item, String tagName) {
		NBTTagCompound itemTag = new NBTTagCompound();
		item.writeToNBT( itemTag );
		compound.setTag( tagName, itemTag );
	}

	public static ItemStack readStackFromNBT(NBTTagCompound nbt) {
		try {
			return ItemStack.loadItemStackFromNBT( nbt );
		} catch( NullPointerException npe ) {
			return null;
		}
	}

	public static List<TileEntity> getAdjacentTileEntities(World world, int x, int y, int z) {
		List<TileEntity> tileEntities = new ArrayList<TileEntity>();
		for( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS ) {
			int _x = x + dir.offsetX;
			int _y = y + dir.offsetY;
			int _z = z + dir.offsetZ;
			TileEntity te = world.getTileEntity( _x, _y, _z );
			if( te != null )
				tileEntities.add( te );
		}
		return tileEntities;
	}

	public static List<IInventory> getAdjacentInventories(World world, int x, int y, int z) {
		List<IInventory> list = new ArrayList<IInventory>();
		for( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS ) {
			int _x = x + dir.offsetX;
			int _y = y + dir.offsetY;
			int _z = z + dir.offsetZ;
			TileEntity te = world.getTileEntity( _x, _y, _z );
			if( te != null && InventoryUtils.isValidInventory( te ) ) {
				IInventory inv = InventoryUtils.getInventoryFrom( te, dir.getOpposite() );
				if( inv != null )
					list.add( inv );
			}
		}
		return list;
	}

	public static boolean[] decodeInt(int source, int length) {
		boolean[] retValue = new boolean[length];
		for( int i = 0; i < length; i++ ) {
			retValue[i] = ((source >> i) & 1) == 1;
		}
		return retValue;
	}

	public static int encodeInt(boolean[] b) {
		int retValue = 0;
		for( int i = 0; i < b.length; i++ ) {
			int foo = b[i] ? 1 : 0;
			retValue = retValue | (foo << i);
		}
		return retValue;
	}

	public static boolean anyOf(boolean[] array) {
		for( boolean b : array ) {
			if( b ) return true;
		}
		return false;
	}

	public static boolean allOf(boolean[] array) {
		for( boolean b : array ) {
			if( b ) return false;
		}
		return true;
	}

	public static EntityPlayer getFakePlayerFor(TileEntity tile) {
		return XActMod.proxy.getFakePlayer( MinecraftServer.getServer().worldServerForDimension(tile.getWorldObj().provider.dimensionId), tile.xCoord, tile.yCoord, tile.zCoord );
	}


	public static void appendFieldToNBTList(NBTTagList list, String name, Object field) {
//		NBTBase element = null;
//		if( field instanceof Boolean ) {
//			element = new NBTTagByte( "_BOOL_" + name, (byte) ((Boolean) field ? 1 : 0) );
//		} else if( field instanceof Byte ) {
//			element = new NBTTagByte( name, (Byte) field );
//		} else if( field instanceof byte[] ) {
//			element = new NBTTagByteArray( name, (byte[]) field );
//		} else if( field instanceof Integer ) {
//			element = new NBTTagInt( name, (Integer) field );
//		} else if( field instanceof int[] ) {
//			element = new NBTTagIntArray( name, (int[]) field );
//		} else if( field instanceof Short ) {
//			element = new NBTTagShort( name, (Short) field );
//		} else if( field instanceof Long ) {
//			element = new NBTTagLong( name, (Long) field );
//		} else if( field instanceof Float ) {
//			element = new NBTTagFloat( name, (Float) field );
//		} else if( field instanceof Double ) {
//			element = new NBTTagDouble( name, (Double) field );
//		} else if( field instanceof String ) {
//			element = new NBTTagString( name, (String) field );
//		} else if( field instanceof NBTBase ) {
//			element = ((NBTBase) field).setName( name );
//		}
//
//		if( element != null ) {
//			list.appendTag( element );
//		} else {
//			String extra = field == null ? " (NULL)" : "Class: " + field.getClass();
//			Utils.logError( "Unable to save field \"%s\" to NBT. Value: %s %s", name, field, extra );
//		}
	}

	public static Object readFieldFromNBT(NBTBase tag) {
		return tag;
//		if( tag instanceof NBTTagByte ) {
//			String name = tag.getName();
//			byte b = ((NBTTagByte) tag).data;
//			if( name.indexOf( "_BOOL_" ) == 0 ) {
//				tag.setName( tag.getName().substring( "_BOOL_".length() ) );
//				return b == 10;
//			}
//			return b;
//		}
//		if( tag instanceof NBTTagShort ) {
//			return ((NBTTagShort) tag).data;
//		}
//		if( tag instanceof NBTTagInt ) {
//			return ((NBTTagInt) tag).data;
//		}
//		if( tag instanceof NBTTagLong ) {
//			return ((NBTTagLong) tag).data;
//		}
//		if( tag instanceof NBTTagFloat ) {
//			return ((NBTTagFloat) tag).data;
//		}
//		if( tag instanceof NBTTagDouble ) {
//			return ((NBTTagDouble) tag).data;
//		}
//		if( tag instanceof NBTTagByteArray ) {
//			return ((NBTTagByteArray) tag).byteArray;
//		}
//		if( tag instanceof NBTTagString ) {
//			return ((NBTTagString) tag).data;
//		}
//		if( tag instanceof NBTTagIntArray ) {
//			return ((NBTTagIntArray) tag).intArray;
//		}
//		return tag;
	}

}
