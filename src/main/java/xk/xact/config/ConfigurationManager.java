package xk.xact.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import xk.xact.XactMod;
import xk.xact.core.blocks.BlockMachine;
import xk.xact.core.blocks.BlockVanillaWorkbench;
import xk.xact.core.items.ItemCase;
import xk.xact.core.items.ItemChip;
import xk.xact.core.items.ItemPad;
import xk.xact.util.References;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Xhamolk_
 */
public class ConfigurationManager extends Configuration {
	public static ConfigurationManager instance;
	public static Configuration config;

	public static String CATEGORY_MISC = "miscellaneous";
	public static String CATEGORY_PLUGINS = "plug-ins";
	public static String CATEGORY_CUSTOMIZATION = "customization";
	
	public static void loadConfiguration(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadValues();
		}
	}

	public static void loadValues() {
		// Comments
		config.getCategory(CATEGORY_MISC).setComment(
				"Change alternate textures and the alternate crafting table");
		config.getCategory(CATEGORY_PLUGINS).setComment(
				"Enable/Disable Plug-ins which add Mod support to X.A.C.T.");
		
		config.getCategory(CATEGORY_CUSTOMIZATION).setComment(
				"Set Blocks that should be ignored by the crafter (e.g minecraft:chest:meta (Normally 0 or -1 to ignore))");
		
		// Plugins
		{
			ENABLE_MPS_PLUGIN = config
					.get(CATEGORY_PLUGINS,
							"enableModularPowerSuitsPlugin",
							true,
							"If true, XACT will try to initialize the plug-in for Modular PowerSuits. \n"
									+ "This plug-in let's you install the Craft Pad into the MPS Power Fist.")
					.getBoolean(true);

			ENABLE_BETTER_STORAGE_PLUGIN = config
					.get(CATEGORY_PLUGINS,
							"enableBetterStoragePlugin",
							true,
							"If true, XACT will try to initialize the plug-in for the Better Storage mod. \n"
									+ "This plug-in enables the XACT Crafter to pull resources from adjacent crates (from Better Storage).")
					.getBoolean(true);

			ENABLE_AE_PLUGIN = config
					.get(CATEGORY_PLUGINS,
							"enableAppliedEnergisticsPlugin",
							true,
							"If true, XACT will try to initialize the plug-in for the Applied Energistics mod. \n"
									+ "This plug-in enables the XACT Crafter to pull resources from adjacent ME Interfaces, \n"
									+ "which acts as a access point to that particular ME Network.")
					.getBoolean(true);
		}

		// Misc
		{
			REPLACE_WORKBENCH = config.get(CATEGORY_MISC,
							"addWorkbenchTileEntity", true,"If true, XACT will add a crafting table which keeps it's inventory. (This used to replace the vanilla one) \n"
						    + "Make sure you clear the workbench's grid before setting this to false, or you will lose your items.").getBoolean(true);

			ENABLE_ALT_TEXTURES = config.get(CATEGORY_MISC, "useAltTextures", false,
								  "If true XACT will use atlernate textures for Items/Guis. They're not really any better.").getBoolean(true);
			
			ENABLE_KEYBINDS = config.get(CATEGORY_MISC, "enableKeybinds", false, "Since there's so many keyinds XACT disables"
								+ " them by default set to true to enable them").getBoolean(false);
			
			ENABLE_FREECRAFTING = config.get(CATEGORY_MISC, "enableFreeCrafting", true, "Allow free crafting when in creative").getBoolean(true);
		}
		
		// Customization
		{
			IGNORED_BLOCKS = Arrays.asList(config.getStringList("ignoredBlocks", CATEGORY_CUSTOMIZATION, DEFAULT_BLOCKS, "Make the crafter ignore blocks"));
		}
		if (config.hasChanged()) {
			config.save();
		}
	}

	public static void initItems() {
		XactMod.itemRecipeBlank = new ItemChip(false);
		XactMod.itemRecipeEncoded = new ItemChip(true);
		XactMod.itemChipCase = new ItemCase();
		XactMod.itemCraftPad = new ItemPad();
	}

	public static void initBlocks() {
		XactMod.blockMachine = new BlockMachine();
		if (REPLACE_WORKBENCH) {
			XactMod.blockWorkbench = new BlockVanillaWorkbench();
		}
	}

	public static boolean REPLACE_WORKBENCH;

	public static boolean ENABLE_MPS_PLUGIN;

	public static boolean ENABLE_BETTER_STORAGE_PLUGIN;

	public static boolean ENABLE_AE_PLUGIN;

	public static boolean ENABLE_ALT_TEXTURES;
	
	public static boolean ENABLE_KEYBINDS;
	
	public static boolean ENABLE_FREECRAFTING;
	
	public static List<String> IGNORED_BLOCKS;
	private static String[] DEFAULT_BLOCKS = { "" };
	// debugging information.
	public static boolean DEBUG_MODE = false;

	@SubscribeEvent
	public void onConfigChanged(
			ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals(References.MOD_ID))
			loadValues();
	}

}
