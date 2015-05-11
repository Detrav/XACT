package xk.xact.config;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xk.xact.XActMod;
import xk.xact.core.blocks.BlockMachine;
import xk.xact.core.blocks.BlockVanillaWorkbench;
import xk.xact.core.items.ItemCase;
import xk.xact.core.items.ItemChip;
import xk.xact.core.items.ItemPad;
import xk.xact.util.References;
import xk.xact.util.Utils;

/**
 * @author Xhamolk_
 */
public class ConfigurationManager extends Configuration {
	public static ConfigurationManager instance;
	public static Configuration config;
	
	public static String CATEGORY_MISC = "miscellaneous";
	public static String CATEGORY_PLUGINS = "plug-ins";
	
	public static void loadConfiguration(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadValues();
		}
	}
	
	public static void loadValues() {
		//Comments
		config.getCategory(CATEGORY_MISC).setComment("Change alternate textures and the alternate crafting table");
		config.getCategory(CATEGORY_PLUGINS).setComment("Enable/Disable Plug-ins which add Mod support to X.A.C.T.");
        
		//Plugins
		{
			ENABLE_MPS_PLUGIN = config.get(CATEGORY_PLUGINS, "enableModularPowerSuitsPlugin", true, "If true, XACT will try to initialize the plug-in for Modular PowerSuits. \n"
					+ "This plug-in let's you install the Craft Pad into the MPS Power Fist.").getBoolean(true);

			ENABLE_BETTER_STORAGE_PLUGIN = config.get(CATEGORY_PLUGINS, "enableBetterStoragePlugin", true,
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
		
		//Misc
		{
			REPLACE_WORKBENCH = config
					.get(CATEGORY_MISC,
							"addWorkbenchTileEntity",
							true,
							"If true, XACT will add a crafting table which keeps it's inventory. (This used to replace the vanilla one) \n"
									+ "Make sure you clear the workbench's grid before setting this to false, or you will lose your items.")
					.getBoolean(true);

			ENABLE_ALT_TEXTURES = config
					.get(CATEGORY_MISC,
							"useAltTextures",
							false,
							"If true XACT will use atlernate textures for Items/Guis. They're not really any better.")
					.getBoolean(true);
		}
        if (config.hasChanged()) {
            config.save();
        }
	}
	public static void initItems() {
		XActMod.itemRecipeBlank = new ItemChip(false);
		XActMod.itemRecipeEncoded = new ItemChip(true);
		XActMod.itemChipCase = new ItemCase();
		XActMod.itemCraftPad = new ItemPad();
	}

	public static void initBlocks() {
		XActMod.blockMachine = new BlockMachine();
		if (REPLACE_WORKBENCH) {
			XActMod.blockWorkbench = new BlockVanillaWorkbench();
		}
	}

	public static boolean REPLACE_WORKBENCH;

	public static boolean ENABLE_MPS_PLUGIN;

	public static boolean ENABLE_BETTER_STORAGE_PLUGIN;

	public static boolean ENABLE_AE_PLUGIN;

	public static boolean ENABLE_ALT_TEXTURES;
	// debugging information.
	public static boolean DEBUG_MODE = false;

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals(References.MOD_ID))
			loadValues();
	}

	
}
