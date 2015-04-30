package xk.xact.api;


import net.minecraft.item.ItemStack;

public interface InteractiveCraftingGui {

	public void sendGridIngredients(ItemStack[] ingredients, int buttonID);

	public void handleKeyBinding(int keyCode, String keyDescription);

}
