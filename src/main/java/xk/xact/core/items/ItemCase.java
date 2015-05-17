package xk.xact.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xk.xact.XactMod;
import xk.xact.util.References;
import xk.xact.util.Textures;

public class ItemCase extends XactBaseItem {
	private String itemName;
	public ItemCase() {
		super(References.Unlocalized.ITEMCHIPCASE);
		this.setMaxStackSize(1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List list, boolean par4) {
		// Show how many chips are stored.
		if (itemStack == null || itemStack.getTagCompound() == null)
			return;

		Integer count = itemStack.getTagCompound().getInteger("chipCount");
		if (count != null && count > 0)
			list.add("Stored " + count + " chips.");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		itemStack.setItemDamage(1);
		if (!world.isRemote)
			player.openGui(XactMod.instance, 1, world, 0, 0, 0);
		return itemStack;
	}

}
