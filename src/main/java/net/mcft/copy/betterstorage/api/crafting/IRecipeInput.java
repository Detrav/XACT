package net.mcft.copy.betterstorage.api.crafting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeInput {
	
	/** Returns the amount needed to craft. */
	int getAmount();
	
	/** Returns if the stack matches the input, ignores stack size. */
	boolean matches(ItemStack stack);
	
	/** Called when a recipe is crafted, allows modification (decrease stack size, damage item)
	 *  of the input stack. Empty stacks and damageable items with no durability left are removed
	 *  automatically. */
	void craft(ItemStack input, ContainerInfo containerInfo);
	
	// NEI related functions
	
	/** Returns a list of possible item stacks that will match this input.
	 *  Used to display and cycle items for the NEI addon. */
	@SideOnly(Side.CLIENT)
	List<ItemStack> getPossibleMatches();
	
}
