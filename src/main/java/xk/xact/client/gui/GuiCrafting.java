package xk.xact.client.gui;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import xk.xact.api.InteractiveCraftingGui;
import xk.xact.client.GuiUtils;
import xk.xact.client.KeyBindingHandler;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.recipes.RecipeUtils;
import xk.xact.util.RecipeDeque;

public abstract class GuiCrafting extends GuiXACT implements
		InteractiveCraftingGui {

	public GuiCrafting(Container container) {
		super(container);
	}

	public void setRecipe(CraftRecipe recipe) {
		ItemStack[] ingredients = (recipe == null || !recipe.isValid()) ? null
				: recipe.getIngredients();
		sendGridIngredients(ingredients, 0);
	}

	// /////////////
	// /// InteractiveCraftingGui
	@Override
	public abstract void sendGridIngredients(ItemStack[] ingredients,
			int buttonID);

	@Override
	public void handleKeyBinding(Container container) {
		KeyBinding keybind = KeyBindingHandler.getPressedKeybinding();
		CraftRecipe recipe;
		
		if (keybind != null) {
			System.out.println(keybind.getKeyDescription());
			if (keybind.getKeyDescription().equals("xact.key.clear")) {
				setRecipe(null);

			} else if (keybind.getKeyDescription().equals("xact.key.load")) {
				
				Slot hoveredSlot = GuiUtils.getHoveredSlot(container, mouseX, mouseY, guiLeft, guiTop);

				if (hoveredSlot != null && hoveredSlot.getHasStack()) {
					ItemStack stackInSlot = hoveredSlot.getStack();
					if (CraftManager.isEncoded(stackInSlot)) {
						recipe = RecipeUtils.getRecipe(stackInSlot,
								GuiUtils.getWorld());
						if (recipe != null && recipe.isValid()) {
							setRecipe(recipe);
						}
					}
				}

			} else if (keybind.getKeyDescription().equals("xact.key.prev")) {
				recipe = getPreviousRecipe();
				if (recipe != null) {
					setRecipe(recipe);
				}

			} else if (keybind.getKeyDescription().equals("xact.key.next")) {
				recipe = getNextRecipe();
				if (recipe != null) {
					setRecipe(recipe);
				}

			} else if (keybind.getKeyDescription().equals("xact.key.delete")) {
				clearRecipeDeque();
			}
		}
	}

	// /////////////
	// /// Recipe Deque

	protected RecipeDeque recipeDeque = new RecipeDeque();

	public void pushRecipe(CraftRecipe recipe) {
		recipeDeque.pushRecipe(recipe);
	}

	protected CraftRecipe getPreviousRecipe() {
		return recipeDeque.getPrevious();
	}

	protected CraftRecipe getNextRecipe() {
		return recipeDeque.getNext();
	}

	protected void clearRecipeDeque() {
		recipeDeque.clear();
	}

}
