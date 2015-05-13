package xk.xact.util;

import static xk.xact.util.ItemsReference.wrap;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

public class ItemsMap<V> extends HashMap<ItemsReference, V> {

	public V put(ItemStack itemStack, V v) {
		return this.put(wrap(itemStack), v);
	}

	public boolean containsKey(ItemStack itemStack) {
		return this.containsKey(wrap(itemStack));
	}

	public V get(ItemStack itemStack) {
		return this.get(wrap(itemStack));
	}

	public V remove(ItemStack itemStack) {
		return this.remove(wrap(itemStack));
	}
}
