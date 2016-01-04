package xk.xact.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import xk.xact.XactMod;
import xk.xact.util.References;
import xk.xact.util.Textures;

import java.util.List;

public class ItemPad extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon inUseIcon;

	public ItemPad() {
		super();
		this.setUnlocalizedName(References.Unlocalized.ITEMCRAFTPAD);
		this.setMaxStackSize(1);
		this.setCreativeTab(XactMod.xactTab);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List list, boolean par4) {

		if (itemStack == null || itemStack.stackTagCompound == null)
			return;

		// Tell which is recipe is loaded on the grid.
		int id = itemStack.stackTagCompound.getInteger("currentRecipe");
		if (id != -1) {
			String resultName = new ItemStack(Item.getItemById(id))
					.getDisplayName();
			if (resultName != null && !resultName.equals(""))
				list.add(I18n.format(References.Localization.CHIP_RECIPE)
						+ ": " + resultName);
			if (itemStack.getDisplayName() == "Unnamed")
				itemStack.setStackDisplayName(getUnlocalizedName());
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		itemStack.setItemDamage(1);

		if (!world.isRemote)
			player.openGui(XactMod.instance, 3, world, 0, 0, 0);
		return itemStack;
	}

	@Override
	public IIcon getIconFromDamage(int itemDamage) {
		if (itemDamage == 1)
			return inUseIcon;
		return itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	// Item Texture
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Textures.ITEM_PAD_OFF);
		this.inUseIcon = iconRegister.registerIcon(Textures.ITEM_PAD_ON);
	}
}
