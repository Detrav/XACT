package xk.xact.plugin.ae2.inventory;

import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import xk.xact.api.IInventoryAdapter;
import xk.xact.api.IInventoryAdapterProvider;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by universallp on 03.01.2016 23:51.
 */
public class AEInventory implements IInventoryAdapter {

    IStorageMonitorable storageMonitorable;

    public AEInventory(IStorageMonitorable sM) {
        this.storageMonitorable = sM;
    }

    @Override
    public ItemStack placeItem(ItemStack item) {

        IAEItemStack result = storageMonitorable.getItemInventory().injectItems(AEItemStack.create(item), Actionable.MODULATE, new BaseActionSource());
        return result == null ? null : result.getItemStack();
    }

    @Override
    public ItemStack takeItem(ItemStack item, int quantity) {
        if (item == null || quantity < 0)
            return new ItemStack(Blocks.air);

        if (storageMonitorable == null || storageMonitorable.getItemInventory() == null)
            return new ItemStack(Blocks.air);
        AEItemStack stackToTake = AEItemStack.create(new ItemStack(item.getItem(), quantity, item.getItemDamage()));

        IAEItemStack result = storageMonitorable.getItemInventory().extractItems(stackToTake, Actionable.MODULATE, new BaseActionSource());
        return result == null ? new ItemStack(item.getItem(), quantity) : result.getItemStack();
    }

    @Override
    public Iterator<ItemStack> iterator() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (IAEItemStack stack : storageMonitorable.getItemInventory().getStorageList())
            items.add(stack.getItemStack());
        return items.iterator();
    }


    public static class Provider implements IInventoryAdapterProvider {

        @Override
        public IInventoryAdapter createInventoryAdapter(Object inventory) {
            if (inventory instanceof IStorageMonitorable) {
                return new AEInventory((IStorageMonitorable) inventory);
            }
            return null;
        }
    }
}
