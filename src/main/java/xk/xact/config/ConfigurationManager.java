package xk.xact.config;

import java.io.File;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xk.xact.XActMod;
import xk.xact.core.blocks.BlockMachine;
import xk.xact.core.blocks.BlockVanillaWorkbench;
import xk.xact.core.items.ItemCase;
import xk.xact.core.items.ItemChip;
import xk.xact.core.items.ItemPad;
import xk.xact.util.Utils;

/**
 * @author Xhamolk_
 */
public class ConfigurationManager {

	private static Configuration config;

	public static void loadConfiguration(File configFile) {
		config = new Configuration( configFile );
		config.load();


		ENABLE_MPS_PLUGIN = config.get( "Plug-ins", "enableModularPowerSuitsPlugin", true,
				"If true, XACT will try to initialize the plug-in for Modular PowerSuits. \n" +
						"This plug-in let's you install the Craft Pad into the MPS Power Fist." )
				.getBoolean( true );

		ENABLE_BETTER_STORAGE_PLUGIN = config.get( "Plug-ins", "enableBetterStoragePlugin", true,
				"If true, XACT will try to initialize the plug-in for the Better Storage mod. \n" +
						"This plug-in enables the XACT Crafter to pull resources from adjacent crates (from Better Storage)." )
				.getBoolean( true );

		ENABLE_AE_PLUGIN = config.get( "Plug-ins", "enableAppliedEnergisticsPlugin", true,
				"If true, XACT will try to initialize the plug-in for the Applied Energistics mod. \n" +
						"This plug-in enables the XACT Crafter to pull resources from adjacent ME Interfaces, \n" +
						"which acts as a access point to that particular ME Network." )
				.getBoolean( true );


		REPLACE_WORKBENCH = config.get( "Miscellaneous", "addWorkbenchTileEntity", true,
				"If true, XACT will make the vanilla workbench able to keep it's contents on the grid after the GUI is closed. \n" +
						"Make sure you clear the workbench's grid before setting this to false, or you will lose your items." )
				.getBoolean( true );

		config.save();
	}

	public static void initItems() {
		XActMod.itemRecipeBlank = new ItemChip(false );
		XActMod.itemRecipeEncoded = new ItemChip(true );
		XActMod.itemChipCase = new ItemCase();
		XActMod.itemCraftPad = new ItemPad();
	}

	public static void initBlocks() {
		XActMod.blockMachine = new BlockMachine();
		if(REPLACE_WORKBENCH) {
			XActMod.blockWorkbench = new BlockVanillaWorkbench();
		}
	}


	public static boolean REPLACE_WORKBENCH;

	public static boolean ENABLE_MPS_PLUGIN;

	public static boolean ENABLE_BETTER_STORAGE_PLUGIN;

	public static boolean ENABLE_AE_PLUGIN;

	// debugging information.
	public static boolean DEBUG_MODE = false;

}
