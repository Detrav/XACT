package xk.xact.plugin.betterstorage.inventory;

import net.mcft.copy.betterstorage.api.crate.ICrateStorage;
import net.minecraft.item.ItemStack;
import xk.xact.api.IInventoryAdapter;
import xk.xact.api.IInventoryAdapterProvider;
import xk.xact.config.ConfigurationManager;
import xk.xact.util.EmptyIterator;
import xk.xact.util.Utils;

import java.util.Iterator;

/**
 * Used to handler ICrateStorage inventories.
 *
 * @author Xhamolk_
 */
public class CrateInventory implements IInventoryAdapter {

	private final ICrateStorage crate;

	public CrateInventory(ICrateStorage crate) {
		this.crate = crate;
	}

	@Override
	public ItemStack placeItem(ItemStack item) {
		return crate.insertItems(item);
	}

	@Override
	public ItemStack takeItem(ItemStack item, int quantity) {
		item = item.copy();
		item.stackSize = Math.min(item.stackSize, quantity);
		return crate.extractItems(item, quantity);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		try {
			Iterable<ItemStack> contents = crate.getContents();
			if (contents != null)
				return contents.iterator();
		} catch (Exception e) {
			if (e.getLocalizedMessage().equals("Can't be called client-side.")
					&& ConfigurationManager.DEBUG_MODE)
				Utils.logException(
						"CrateInventory.class: " + e.getLocalizedMessage(), e,
						false);
			else if (!e.getLocalizedMessage().equals(
					"Can't be called client-side.")
					&& !ConfigurationManager.DEBUG_MODE)
				Utils.logException(
						"Exception in CrateInventory.class: "
								+ e.getLocalizedMessage(), e, false);
		}
		return new EmptyIterator<ItemStack>();
	}

	public static class Provider implements IInventoryAdapterProvider {

		@Override
		public IInventoryAdapter createInventoryAdapter(Object inventory) {
			if (inventory instanceof ICrateStorage) {
				return new CrateInventory((ICrateStorage) inventory);
			}
			return null;
		}
	}

}
