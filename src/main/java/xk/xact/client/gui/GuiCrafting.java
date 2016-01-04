package xk.xact.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import xk.xact.api.InteractiveCraftingGui;
import xk.xact.client.GuiUtils;
import xk.xact.client.KeyBindingHandler;
import xk.xact.client.button.GuiButtonCustom;
import xk.xact.client.button.ICustomButtonMode;
import xk.xact.config.ConfigurationManager;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import xk.xact.recipes.RecipeUtils;
import xk.xact.util.RecipeDeque;
import xk.xact.util.References;

import java.util.Arrays;

public abstract class GuiCrafting extends GuiXACT implements
		InteractiveCraftingGui {

	public GuiCrafting(Container container) {
		super(container);
	}

	public void setRecipe(CraftRecipe recipe) {
		ItemStack[] ingredients = (recipe == null || !recipe.isValid()) ? null
				: recipe.getIngredients();
		sendGridIngredients(ingredients, -1);
	}
	
	@Override
	public void drawScreen(int mousex, int mousey, float partialtick) {
		// Draws the button tooltips
		super.drawScreen(mousex, mousey, partialtick);
		for (Object object : buttonList) {
			if (object != null && object instanceof GuiButtonCustom) {
				if (((GuiButtonCustom) object).isMouseHovering(mousex, mousey)) {
					
					GuiButtonCustom btn = (GuiButtonCustom) object;
					if (btn.id < 4 && btn.isVisible()) { // The clear/save buttons
						if (btn.getMode().equals(ICustomButtonMode.DeviceModes.CLEAR)) {
							drawHoveringText(Arrays.asList(I18n.format(References.Localization.TOOLTIP_CLEAR)), mousex, mousey, fontRendererObj);
						} else if (btn.getMode().equals(ICustomButtonMode.DeviceModes.SAVE)) {
							drawHoveringText(Arrays.asList(I18n.format(References.Localization.TOOLTIP_SAVE)), mousex, mousey, fontRendererObj);
						}
					} else {
						switch (btn.id) {
						case 4: // Clear grid
							drawHoveringText(Arrays.asList(I18n.format(References.Localization.TOOLTIP_CLEARGRID)), mousex, mousey, fontRendererObj);
							break;
						case 5: // Load Next
							drawHoveringText(Arrays.asList(I18n.format(References.Localization.TOOLTIP_NEXTRECIPE)), mousex, mousey, fontRendererObj);
							break;
						case 6: // Load Last
							drawHoveringText(Arrays.asList(I18n.format(References.Localization.TOOLTIP_LASTRECIPE)), mousex, mousey, fontRendererObj);
						default:
							break;
						}
					}
				}
			}
		}
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

		if (keybind != null && ConfigurationManager.ENABLE_KEYBINDS) {
			if (keybind.getKeyDescription().equals("xact.key.clear")) {
				setRecipe(null);

			} else if (keybind.getKeyDescription().equals("xact.key.load")) {
				Slot hoveredSlot = GuiUtils.getHoveredSlot(container, mouseX,
						mouseY, guiLeft, guiTop);

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
