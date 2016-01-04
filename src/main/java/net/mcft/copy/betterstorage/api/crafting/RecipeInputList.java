package net.mcft.copy.betterstorage.api.crafting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.mcft.copy.betterstorage.api.BetterStorageUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeInputList extends RecipeInputBase {
	
	private final List<ItemStack> items;
	
	public RecipeInputList(List<ItemStack> items) {
		this.items = items;
	}
	
	@Override
	public int getAmount() { return 1; }
	
	@Override
	public boolean matches(ItemStack stack) {
		for (ItemStack item : items)
			if (BetterStorageUtils.wildcardMatch(item, stack))
				return true;
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<ItemStack> getPossibleMatches() { return items; }
	
}
