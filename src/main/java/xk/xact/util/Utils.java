package xk.xact.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import xk.xact.XactMod;
import xk.xact.config.ConfigurationManager;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.inventory.InventoryUtils;
import xk.xact.network.ClientProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

	public static void notifyPlayer(EntityPlayer player, String message) {
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	public static void writeCraftPadInfo(EntityPlayer player, Minecraft mc) {
		if (ClientProxy.MESSAGE_COUNT == 0) {
			ClientProxy.MESSAGE_COUNT++;
			ClientProxy.LAST_MESSAGE = System.currentTimeMillis();
			mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Woah there, watch where you're going!"), 0);
		} else if (ClientProxy.MESSAGE_COUNT > 0 && (System.currentTimeMillis() - ClientProxy.LAST_MESSAGE) < 10000) {
			mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Woah there, watch where you're going! (" + ClientProxy.MESSAGE_COUNT + "x)"), 1);
			ClientProxy.MESSAGE_COUNT++;
			ClientProxy.LAST_MESSAGE = System.currentTimeMillis();
		} else if (ClientProxy.MESSAGE_COUNT > 0 && (System.currentTimeMillis() - ClientProxy.LAST_MESSAGE) > 10000)
			ClientProxy.MESSAGE_COUNT = 0;
	}

	public static void debug(String message, Object... data) {
		if (ConfigurationManager.DEBUG_MODE)
			XactMod.logger.fine(String.format(message, data));

	}

	public static void log(String message, Object... data) {
		FMLLog.log(References.MOD_NAME, Level.INFO, message, data);
	}

	public static void logError(String message, Object... data) {
		FMLLog.log(References.MOD_NAME, Level.ERROR, message, data);
	}

	public static void logException(String string, Exception exception, boolean stopGame) {
		FMLLog.log(References.MOD_NAME, Level.FATAL, string, exception);
		if (stopGame)
			FMLCommonHandler.instance().getSidedDelegate().haltGame(string, exception);
	}

	/**
	 * The description of the ItemStack passed. Includes the stack size and the
	 * 'display' name.
	 * <p/>
	 * Example: 64x Redstone Dust
	 *
	 * @param stack
	 *            the item stack.
	 * @return the description of the stack's contents. Or "null" if the stack
	 *         is null.
	 */
	public static String stackDescription(ItemStack stack) {
		if (stack == null)
			return "null";

		return stack.stackSize + "x " + stack.getItem().getItemStackDisplayName(stack);
	}

	public static ItemStack copyOf(ItemStack stack) {
		if (stack == null)
			return null;
		return stack.copy();
	}

	public static boolean equalsStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem()
				&& (!stack1.getHasSubtypes() || stack1.getItemDamage() == stack2.getItemDamage())
				&& ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	public static String arrayToString(Object[] obj) {
		String out = "";

//		if (obj instanceof ItemStack[])
//			for (int i = 0; i < obj.length; i++)
//				out += (i == obj.length - 1 ? "" : ", " + obj.toString());
//		else
			for (int i = 0; i < obj.length; i++)
				if (obj[i] != null)
					out += (i == obj.length - 1 ? "" : ", " + obj[i].toString());
		return out;
	}

	/**
	 * Drops an item on the world as an EntityItem.
	 *
	 * @param itemStack
	 *            the ItemStack to drop. Shall not be null.
	 */
	public static void dropItemAsEntity(World world, int x, int y, int z, ItemStack itemStack) {
		Random random = new Random();
		float var10 = random.nextFloat() * 0.8F + 0.1F;
		float var11 = random.nextFloat() * 0.8F + 0.1F;
		EntityItem item;

		for (float var12 = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld(item)) {
			int var13 = random.nextInt(21) + 10;

			if (var13 > itemStack.stackSize) {
				var13 = itemStack.stackSize;
			}

			itemStack.stackSize -= var13;
			item = new EntityItem(world, (double) (x + var10), (double) (y + var11), (double) (z + var12),
					new ItemStack(itemStack.getItem(), var13, itemStack.getItemDamage()));
			float var15 = 0.05F;
			item.motionX = (random.nextGaussian() * var15);
			item.motionY = (random.nextGaussian() * var15 + 0.2F);
			item.motionZ = (random.nextGaussian() * var15);

			if (itemStack.hasTagCompound()) {
				item.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
			}
		}
	}

	public static ItemStack[] copyArray(ItemStack... oldArray) {
		int length = oldArray.length;
		ItemStack[] newArray = new ItemStack[length];
		for (int i = 0; i < length; i++) {
			newArray[i] = oldArray[i] == null ? null : oldArray[i].copy();
		}
		return newArray;
	}

	public static void writeItemStackToNBT(NBTTagCompound compound, ItemStack item, String tagName) {
		NBTTagCompound itemTag = new NBTTagCompound();
		item.writeToNBT(itemTag);
		compound.setTag(tagName, itemTag);
	}

	public static ItemStack readStackFromNBT(NBTTagCompound nbt) {
		try {
			return ItemStack.loadItemStackFromNBT(nbt);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public static List<TileEntity> getAdjacentTileEntities(World world, int x, int y, int z) {
		List<TileEntity> tileEntities = new ArrayList<TileEntity>();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int _x = x + dir.offsetX;
			int _y = y + dir.offsetY;
			int _z = z + dir.offsetZ;

			TileEntity te = world.getTileEntity(_x, _y, _z);
			Block block = world.getBlock(_x, _y, _z);
			int blockMeta = world.getBlockMetadata(_x, _y, _z);

			if (te != null && !block.isAir(world, _x, _y, _z) && !InventoryUtils.isBlockDisabled(block, blockMeta))
				tileEntities.add(te);
		}
		return tileEntities;
	}

	public static List<IInventory> getAdjacentInventories(World world, int x, int y, int z) {
		List<IInventory> list = new ArrayList<IInventory>();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int _x = x + dir.offsetX;
			int _y = y + dir.offsetY;
			int _z = z + dir.offsetZ;
			TileEntity te = world.getTileEntity(_x, _y, _z);
			Block block = world.getBlock(_x, _y, _z);
			int blockMeta = world.getBlockMetadata(_x, _y, _z);
			if (te != null && InventoryUtils.isValidInventory(te) && !InventoryUtils.isBlockDisabled(block, blockMeta)) {
				IInventory inv = InventoryUtils.getInventoryFrom(te, dir.getOpposite());
				if (inv != null)
					list.add(inv);
			}
		}
		return list;
	}



	public static boolean[] decodeInt(int source, int length) {
		boolean[] retValue = new boolean[length];
		for (int i = 0; i < length; i++) {
			retValue[i] = ((source >> i) & 1) == 1;
		}
		return retValue;
	}

	public static int encodeInt(boolean[] b) {
		int retValue = 0;
		for (int i = 0; i < b.length; i++) {
			int foo = b[i] ? 1 : 0;
			retValue = retValue | (foo << i);
		}
		return retValue;
	}

	public static boolean anyOf(boolean[] array) {
		for (boolean b : array) {
			if (b)
				return true;
		}
		return false;
	}

	public static boolean allOf(boolean[] array) {
		for (boolean b : array) {
			if (b)
				return false;
		}
		return true;
	}

	public static EntityPlayer getFakePlayerFor(TileEntity tile) {
		return XactMod.proxy.getFakePlayer(
				MinecraftServer.getServer().worldServerForDimension(tile.getWorldObj().provider.dimensionId),
				tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public static void removeAnyRecipe(Item resultItem) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++) {
			IRecipe tmpRecipe = recipes.get(i);
			ItemStack recipeResult = tmpRecipe.getRecipeOutput();
			if (recipeResult != null && recipeResult.getItem() == resultItem) {
				recipes.remove(i--);
			}
		}
	}

	/**
	 * Gets adjacent Crafters Maximum is 3 Crafters
	 */
	public static List<TileCrafter> getAdjacentCrafters(int x, int y, int z, World world) {
		List<TileCrafter> crafters = new ArrayList<TileCrafter>();
		for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) { // Will search through all directions
			for (int range = 1; range <= 3; range++) { // Will search 3 blocks into current direction
				int currentX = x + face.offsetX * range;
				int currentY = y + face.offsetY * range;
				int currentZ = z + face.offsetZ * range; // since all offsets exept one will be 0, multiplying won't do anything

				TileEntity te = world.getTileEntity(currentX, currentY, currentZ);
				if (te != null && te instanceof TileCrafter)
					crafters.add((TileCrafter) te);
				
				if (crafters.size() == 3)
					return crafters;
			}
		}
		
		return crafters;
	}


	public static boolean shareSameOreDictionary(ItemStack stack1, ItemStack stack2, boolean strict) {
		if (stack1 == null || stack2 == null)
			return false;

		int[] ids = OreDictionary.getOreIDs(stack1);
		int[] ids2 = OreDictionary.getOreIDs(stack2);

		if (strict && ids.length != ids2.length)
			return false;
		if (ids.length <= 0 || ids2.length <= 0) // if one of the items doesn't
													// have an entry
			return false; // return false

		int matchCount = 0;

		for (int id : ids) {
			for (int id2 : ids2) {
				if (id2 == id) {
					if (!strict)
						return true;
					else
						matchCount++;
				}

			}
		}

		if (matchCount == ids.length)
			return true;
		return false;
	}
	
	public static boolean compareEnchantments(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null)
			return false;

		if (!stack1.hasTagCompound() && !stack2.hasTagCompound())
			return false; // If both items don't have any nbt it'll just return
							// true

		if (!stack1.isItemEnchanted() || !stack2.isItemEnchanted())
			return false; // If only one of the items has nbt it'll return false

		if (stack1.getEnchantmentTagList().tagCount() > 0 && stack2.getEnchantmentTagList().tagCount() > 0) {
			// We are comparing two weapons with enchantments
			return stack1.getEnchantmentTagList().equals(stack2.getEnchantmentTagList());
		} else if (stack1.getTagCompound().getTag("StoredEnchantments") != null
				&& stack2.getTagCompound().getTag("StoredEnchantments") != null) {
			// We are comparing two books with enchantments
			return stack1.getTagCompound().getTag("StoredEnchantments")
					.equals(stack2.getTagCompound().getTag("StoredEnchantments"));
		}

		return false;
	}
	
    public static int argbToInt(int A, int R, int G, int B){     
        byte[] colorByteArr = { (byte) A, (byte) R, (byte) G, (byte) B };
        return byteArrToInt(colorByteArr);
    }
    
    public static int byteArrToInt(byte[] colorByteArr) {
        return (colorByteArr[0] << 24) + ((colorByteArr[1] & 0xFF) << 16) + ((colorByteArr[2] & 0xFF) << 8) + (colorByteArr[3] & 0xFF);
    }
}
