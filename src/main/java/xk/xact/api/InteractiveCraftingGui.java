package xk.xact.api;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public interface InteractiveCraftingGui {

	public void sendGridIngredients(ItemStack[] ingredients, int buttonID);

	public void handleKeyBinding(Container container);

}
