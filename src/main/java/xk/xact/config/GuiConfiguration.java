package xk.xact.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xk.xact.util.References;

public class GuiConfiguration extends GuiConfig {

	public GuiConfiguration(GuiScreen parentScreen) {
		super(parentScreen, GuiConfiguration.getConfigElements(),
				References.MOD_ID, false, true, GuiConfig
						.getAbridgedConfigPath(ConfigurationManager.config
								.toString()));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button != null && button.id == 2000) { // 2000 = done button
			ConfigurationManager.loadValues();
		}
		super.actionPerformed(button);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		List<IConfigElement> misc = new ConfigElement(ConfigurationManager.config.getCategory(ConfigurationManager.CATEGORY_MISC))
				.getChildElements();

		List<IConfigElement> plugins = new ConfigElement(ConfigurationManager.config.getCategory(ConfigurationManager.CATEGORY_PLUGINS))
				.getChildElements();
		
		List<IConfigElement> customization = new ConfigElement(ConfigurationManager.config.getCategory(ConfigurationManager.CATEGORY_CUSTOMIZATION))
				.getChildElements();
		
		list.add(new DummyConfigElement.DummyCategoryElement(
				ConfigurationManager.CATEGORY_MISC, References.MOD_ID + ".cfg."
						+ ConfigurationManager.CATEGORY_MISC, misc));
		list.add(new DummyConfigElement.DummyCategoryElement(
				ConfigurationManager.CATEGORY_PLUGINS, References.MOD_ID
						+ ".cfg." + ConfigurationManager.CATEGORY_PLUGINS,
				plugins));
		list.add(new DummyConfigElement.DummyCategoryElement(
				ConfigurationManager.CATEGORY_CUSTOMIZATION, References.MOD_ID
						+ ".cfg." + ConfigurationManager.CATEGORY_CUSTOMIZATION,
				plugins));
		return list;
	}

}
