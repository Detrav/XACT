package xk.xact.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

/**
 * Adapter used to hide all the inaccessible slots from the original
 * ISidedInventory.
 *
 * @author Xhamolk_
 */
public class SidedInventory implements IInventory, ISidedInventory {

	private ISidedInventory inv;
	private int[] slots;
	private EnumFacing side; // as expected by vanilla ISided.

	public SidedInventory(ISidedInventory inventory, EnumFacing side) {
		this.inv = inventory;
		this.side = side;
		this.slots = inventory.getSlotsForFace(side);
	}

	// ----- IInventory -----

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv.getStackInSlot(slots[i]);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return inv.decrStackSize(slots[i], j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return inv.getStackInSlotOnClosing(slots[i]);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inv.setInventorySlotContents(slots[i], itemstack);
	}

	@Override
	public int getInventoryStackLimit() {
		return inv.getInventoryStackLimit();
	}

	@Override
	public void markDirty() {
		inv.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return inv.isUseableByPlayer(player);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return inv.isItemValidForSlot(slots[i], itemstack);
	}

	// ----- ISided Inventory -----

	// The "available" slots for this inventory.
	// Should be an array of integers from 0 to slots.length-1
	private int[] availableSlots = null;
	

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (availableSlots == null) {
			availableSlots = new int[slots.length];
			for (int i = 0; i < slots.length; i++) {
				availableSlots[i] = i;
			}
		}
		return availableSlots;
	}
	

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
		return inv.canInsertItem(slots[index], itemStackIn, side);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) {
		return inv.canExtractItem(slots[index], stack, side);
	}

	@Override
	public boolean hasCustomName() {
		return inv.hasCustomName();
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public String getName() {
		return inv.getName();
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return new ChatComponentText(getName());
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}

}
