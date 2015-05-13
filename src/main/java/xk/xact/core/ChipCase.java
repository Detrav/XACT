package xk.xact.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import xk.xact.inventory.InvSlot;
import xk.xact.inventory.InvSlotIterator;
import xk.xact.inventory.Inventory;

public class ChipCase {

	private Inventory internalInventory;

	public boolean inventoryChanged = false;

	public ChipCase(ItemStack itemStack) {
		this.internalInventory = new Inventory(30, "libraryStorage") {
			@Override
			public void markDirty() {
				super.markDirty();
				inventoryChanged = true;
			}
		};

		// Load contents from NBT
		if (!itemStack.hasTagCompound())
			itemStack.stackTagCompound = new NBTTagCompound();

		NBTTagList chips = itemStack.stackTagCompound.getTagList("Chips", 10); // Load
																				// Chips...
																				// om
																				// nom
																				// nom
		for (int i = 0; i < chips.tagCount(); ++i) {
			NBTTagCompound tag = chips.getCompoundTagAt(i);
			byte slotIndex = tag.getByte("ChipSlot");
			if (slotIndex >= 0
					&& slotIndex < getInternalInventory().getSizeInventory()) {
				getInternalInventory().setInventorySlotContents(slotIndex,
						ItemStack.loadItemStackFromNBT(tag));
			}
		}
		// readFromNBT( itemStack.getTagCompound() );
	}

	public IInventory getInternalInventory() {
		return internalInventory;
	}

	public int getChipsCount() {
		int count = 0;
		for (InvSlot current : InvSlotIterator.createNewFor(internalInventory)) {
			if (current != null && !current.isEmpty()) {
				count += current.stack.stackSize;
			}
		}
		return count;
	}

	// //////////
	// / NBT

	public void readFromNBT(NBTTagCompound compound) {
		if (compound == null)
			return;

		internalInventory.readFromNBT(compound);
	}

	public void writeToNBT(NBTTagCompound compound) {
		if (compound == null)
			return;

		internalInventory.writeToNBT(compound);
		compound.setInteger("chipCount", getChipsCount());
	}

}
