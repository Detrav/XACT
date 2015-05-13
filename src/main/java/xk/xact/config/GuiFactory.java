package xk.xact.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraft) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiConfiguration.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		return null;
	}

}
