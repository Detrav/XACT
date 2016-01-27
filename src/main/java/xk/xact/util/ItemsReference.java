package xk.xact.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemsReference {

	public final int itemDamage;
	private NBTTagCompound tag;
	private ItemStack stack;
	public int amount = 0;

	private ItemsReference(ItemStack itemStack) {
		this.itemDamage = itemStack.getItemDamage();
		this.stack = itemStack;
		this.tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : null;
	}

	@Override
	public int hashCode() {
		// return itemDamage ^ (tag == null ? 1 : tag.hashCode()) ^ itemID);
		int hash = 17;
		hash = 31 * hash;
		hash = 31 * hash + itemDamage;
		hash = 31 * hash + (tag == null ? 0 : tag.hashCode());
		return hash;
	}

	/**
	 * This compares two ItemsReferences
	 * use compare() to see if their itemstack
	 * is equal (in terms of damage/ore dictionary)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof ItemsReference)
			return this.isEqualTo((ItemsReference) o);
		return o instanceof ItemStack
				&& this.isEqualTo(new ItemsReference((ItemStack) o));
	}

	public boolean isEqualTo(ItemsReference reference) {
		// Compare Item ID.
		if (this.stack.getItem() != reference.stack.getItem())
			return false;
		// Compare Item's damage value.
		if (this.itemDamage != reference.itemDamage)
			return false;

		// Compare stacks tags.
		if (this.tag == null)
			return reference.tag == null;
		return this.tag.equals(reference.tag);
	}
	
	
	/**
	 * Remade isEqual method
	 * Compares only item, damage and ore dictionary
	 * @return
	 */
	public boolean compare(ItemsReference reference) {
		if (Utils.shareSameOreDictionary(this.stack, reference.stack, true))
			return true; // Ore dictionary is top priority
		
		// Compare Item ID.
		if (this.stack.getItem() != reference.stack.getItem())
			return false;
		
		// Compare Item's damage value.
		if (this.itemDamage != reference.itemDamage)
			return false;
		
		/* just checking enchantments is not good enough for some type of items
		if (!Utils.compareEnchantments(this.stack, reference.stack))
			return false;
		*/
		
		// Compare stacks tags.
		if (this.tag == null)
			return reference.tag == null;
		return this.tag.equals(reference.tag);
	}
	
	public ItemStack toItemStack() {
		ItemStack itemStack = new ItemStack(stack.getItem(), amount, itemDamage);
		if (tag != null)
			itemStack.setTagCompound(tag);
		return itemStack;
	}

	public static ItemsReference wrap(ItemStack itemStack) {
		return new ItemsReference(itemStack);
	}
}
