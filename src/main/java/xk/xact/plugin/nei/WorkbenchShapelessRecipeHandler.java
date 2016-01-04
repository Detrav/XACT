package xk.xact.plugin.nei;

import codechicken.nei.recipe.ShapelessRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import xk.xact.client.gui.GuiVanillaWorkbench;

public class WorkbenchShapelessRecipeHandler extends ShapelessRecipeHandler{
	
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
    
        return GuiVanillaWorkbench.class;
    }
}
