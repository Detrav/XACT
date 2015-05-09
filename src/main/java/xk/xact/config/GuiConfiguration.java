package xk.xact.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.ConfigElement;
import xk.xact.util.References;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class GuiConfiguration extends GuiConfig {

	public GuiConfiguration(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(
				ConfigurationManager.getAllCategories())
				.getChildElements(), References.MOD_ID, false, false, GuiConfig
				.getAbridgedConfigPath(ConfigurationManager.config.toString()));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateElements();
	}

	public void updateElements() {
		for (IConfigElement element : this.configElements) {
			if (element.getName() == "enableBetterStoragePlugin") {
				if (!element.get().equals(ConfigurationManager.ENABLE_BETTER_STORAGE_PLUGIN)) {
					entryList.saveConfigElements();
				}
			} else if (element.getName() == "addWorkbenchTileEntity") {
				if (!element.get().equals(ConfigurationManager.REPLACE_WORKBENCH)) {
					entryList.saveConfigElements();		
				}
			} else if (element.getName() == "useAltTextures") {
				if (!element.get().equals(ConfigurationManager.ENABLE_ALT_TEXTURES)) {
					entryList.saveConfigElements();
				}
			}
				
							
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			boolean flag = true;
			try {
				if ((configID != null || parentScreen == null || !(parentScreen instanceof GuiConfig))
						&& (entryList.hasChangedEntry(true))) {
					// DEBUG
					System.out.println("Saving config elements");
					boolean requiresMcRestart = entryList.saveConfigElements();

					if (Loader.isModLoaded(modID)) {
						ConfigChangedEvent event = new OnConfigChangedEvent(
								modID, configID, isWorldRunning,
								requiresMcRestart);
						FMLCommonHandler.instance().bus().post(event);
						if (!event.getResult().equals(Result.DENY))
							FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(modID,
											configID, isWorldRunning,
											requiresMcRestart));

						if (requiresMcRestart) {
							flag = false;
							mc.displayGuiScreen(new GuiMessageDialog(
									parentScreen,
									"fml.configgui.gameRestartTitle",
									new ChatComponentText(
											I18n.format("fml.configgui.gameRestartRequired")),
									"fml.configgui.confirmRestartMessage"));
						}

						if (parentScreen instanceof GuiConfig)
							((GuiConfig) parentScreen).needsRefresh = true;
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (flag)
				mc.displayGuiScreen(parentScreen);
		}
	}
}
