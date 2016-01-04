package xk.xact.plugin.nei;

import codechicken.nei.recipe.ShapedRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import xk.xact.client.gui.GuiVanillaWorkbench;

public class WorkbenchRecipeHandler extends ShapedRecipeHandler {
	
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
    
        return GuiVanillaWorkbench.class;
    }
}
