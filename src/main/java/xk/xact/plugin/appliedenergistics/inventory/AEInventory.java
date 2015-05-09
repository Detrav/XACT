package xk.xact.plugin.appliedenergistics.inventory;

//import appeng.api.config.Actionable;
//import appeng.api.config.FuzzyMode;
//import appeng.api.networking.security.BaseActionSource;
//import appeng.api.storage.IMEInventory;
//import appeng.api.storage.StorageChannel;
//import appeng.api.storage.data.IAEItemStack;
//import appeng.api.storage.data.IAEStack;
//import appeng.api.storage.data.IAETagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xk.xact.api.IInventoryAdapter;
import xk.xact.api.IInventoryAdapterProvider;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Iterator;

/**
 * Used to handle IMEInventory from Applied Energistics.
 *
 * @author Xhamolk_
 */
public class AEInventory { //implements IInventoryAdapter {

//	private IMEInventory inventory;
//
//	public AEInventory(IMEInventory inventory) {
//		this.inventory = inventory;
//	}
//
//	@Override
//	public ItemStack placeItem(ItemStack item) {
//		if( item != null ) {
//			return convert( inventory.injectItems(convert( item ), Actionable.MODULATE, new BaseActionSource()));
//		}
//		return null;
//	}
//
//	@Override
//	public ItemStack takeItem(ItemStack item, int quantity) {
//		ItemStack temp = item.copy();
//		temp.stackSize = quantity;
//
//		return convert( inventory.extractItems( convert( temp ), null, null ) );
//	}
//
//	@Override
//	public Iterator<ItemStack> iterator() {
//		return new AEIterator();
//	}
//
//	private ItemStack convert(IAEStack iaeStack) {
//		if( iaeStack != null ) {
//			if (iaeStack.isItem())
//				return new ItemStack( ((IAEItemStack) iaeStack).getItem(), (int) iaeStack.getStackSize(), ((IAEItemStack) iaeStack).getItemDamage() );
//			
//		}
//		return null;
//	}
//
//	private IAEStack convert(ItemStack item) {
//		if( item != null ) {
//			
//			return null;
//		}
//		return null;
//	}
//
//
//	private class AEIterator implements Iterator<ItemStack> {
//
//		private Iterator<IAEItemStack> iterator = inventory.getAvailableItems(null).iterator();
//
//		public boolean hasNext() {
//			return iterator.hasNext();
//		}
//
//		@Override
//		public ItemStack next() {
//			return convert( iterator.next() );
//		}
//
//		@Override
//		public void remove() {
//		}
//
//	}
//
//	public static class Provider implements IInventoryAdapterProvider {
//
//		@Override
//		public IInventoryAdapter createInventoryAdapter(Object inventory) {
//			return null;
//		}
//	}

}
