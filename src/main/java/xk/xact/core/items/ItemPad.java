package xk.xact.core.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import xk.xact.XActMod;
import xk.xact.util.References;
import xk.xact.util.Textures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPad extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon inUseIcon;

	public ItemPad() {
		super();
		this.setUnlocalizedName(References.Unlocalized.ITEMCRAFTPAD);
		this.setMaxStackSize(1);
		this.setCreativeTab(XActMod.xactTab);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List list, boolean par4) {
		// Tell which is recipe is loaded on the grid.
		if (itemStack == null || itemStack.stackTagCompound == null)
			return;
		
		ItemStack recipeResult = ItemStack.loadItemStackFromNBT(
				itemStack.stackTagCompound);
		if (recipeResult != null) {
			String loadedRecipe = recipeResult.getDisplayName();
			if (loadedRecipe != null && !loadedRecipe.equals(""))
				list.add(I18n.format(References.Localization.CHIP_RECIPE) + ": "
						+ I18n.format(loadedRecipe));
			if (itemStack.getDisplayName() == "Unnamed")
				itemStack.setStackDisplayName(getUnlocalizedName());
		}
	
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		itemStack.setItemDamage(1);
		if (!world.isRemote)
			player.openGui(XActMod.instance, 3, world, 0, 0, 0);
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
