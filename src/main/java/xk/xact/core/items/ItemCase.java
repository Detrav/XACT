package xk.xact.core.items;


import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xk.xact.XActMod;
import xk.xact.util.References;
import xk.xact.util.Textures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemCase extends Item {

	public ItemCase() {
		super();
		this.setUnlocalizedName(References.Unlocalized.ITEMCHIPCASE);
		this.setMaxStackSize( 1 );
		this.setCreativeTab( XActMod.xactTab );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		// Show how many chips are stored.
		if( itemStack == null || itemStack.stackTagCompound == null )
			return;

		Integer count = itemStack.getTagCompound().getInteger( "chipCount" );
		if( count != null && count > 0 )
			list.add( "Stored " + count + " chips." );
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setItemDamage( 1 );
		if( ! world.isRemote )
			player.openGui( XActMod.instance, 1, world, 0, 0, 0 );
		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT) // Item Texture
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon( Textures.ITEM_CASE );
	}

}
