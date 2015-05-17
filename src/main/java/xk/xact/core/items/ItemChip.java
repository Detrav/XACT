package xk.xact.core.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xk.xact.XactMod;
import xk.xact.recipes.CraftManager;
import xk.xact.util.References;
import xk.xact.util.Textures;

/**
 * The item used for the encoding of recipes.
 *
 * @author Xhamolk_
 */
public class ItemChip extends XactBaseItem {

	/*
	 * Note: The actual encoding happens on the stack's NBT, and is performed by
	 * CraftManager.encodeRecipe
	 */
	public final boolean encoded;

	public ItemChip(boolean encoded) {
		super(encoded ? References.Unlocalized.ITEMRECIPEENCODED : References.Unlocalized.ITEMRECIPEBLANK);
		this.encoded = encoded;
		if (encoded)
			invalidChip = new ItemStack(this, 1, 1);
	}

	@Override
	public int getItemStackLimit() {
		// encoded items can't stack, but blank ones do.
		return encoded ? 1 : 16;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List list, boolean par4) {
		if (itemStack.getItem() instanceof ItemChip) {
			if (((ItemChip) itemStack.getItem()).encoded) {
				if (CraftManager.decodeRecipe(itemStack) != null) {
					ItemStack result = CraftManager.decodeRecipe(itemStack)
							.getResult();
					String itemName = result.getItem().getItemStackDisplayName(
							result);
					list.add("\u00a73"
							+ I18n.format(References.Localization.CHIP_RECIPE)
							+ ": " + itemName);
				} else {
					list.add("\u00a7c<"
							+ I18n.format(References.Localization.CHIP_INVALID)
							+ ">");
				}
			} else {
				// blank recipes.
				list.add("\u00a77<"
						+ I18n.format(References.Localization.CHIP_BLANK) + ">");
			}

		}
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	// Item Texture
//	public void registerIcons(IIconRegister iconRegister) {
//		if (encoded)
//			this.itemIcon = iconRegister
//					.registerIcon(Textures.ITEM_CHIP_ENCODED);
//		else
//			this.itemIcon = iconRegister.registerIcon(Textures.ITEM_CHIP_BLANK);
//
//		if (invalidChipIcon == null)
//			invalidChipIcon = iconRegister
//					.registerIcon(Textures.ITEM_CHIP_INVALID);
//	}
//
//	@SideOnly(Side.CLIENT)
//	private static IIcon invalidChipIcon;
//
//	@Override
//	@SideOnly(Side.CLIENT)
//	public IIcon getIconFromDamage(int damage) {
//		if (damage == 1)
//			return invalidChipIcon;
//		return super.getIconFromDamage(damage);
//	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.getItem() instanceof ItemChip) {
			if (((ItemChip) stack.getItem()).encoded) {
				return EnumChatFormatting.DARK_GREEN
						+ super.getItemStackDisplayName(stack)
						+ EnumChatFormatting.RESET;
			}
		}
		return super.getItemStackDisplayName(stack);
	}

	public static ItemStack invalidChip;

}
