package xk.xact.plugin.nei;

import xk.xact.client.gui.GuiVanillaWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.recipe.ShapelessRecipeHandler;

public class WorkbenchShapelessRecipeHandler extends ShapelessRecipeHandler{
	
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
    
        return GuiVanillaWorkbench.class;
    }
}
