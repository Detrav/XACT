package xk.xact.plugin.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import xk.xact.client.gui.GuiVanillaWorkbench;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class WorkbenchRecipeHandler extends ShapedRecipeHandler {
	
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
    
        return GuiVanillaWorkbench.class;
    }
}
