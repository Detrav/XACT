package xk.xact.plugin;


import net.minecraft.inventory.IInventory;
import xk.xact.api.IInventoryAdapter;
import xk.xact.api.SpecialCasedRecipe;
import xk.xact.api.plugin.XACTPlugin;
import xk.xact.config.ConfigurationManager;
import xk.xact.util.ReflectionUtils;
import xk.xact.util.Utils;

import java.util.*;

public class PluginManager {

	private static List<SpecialCasedRecipe> specialCasedRecipes = new ArrayList<SpecialCasedRecipe>();
	private static List<XACTPlugin> plugins = new ArrayList<XACTPlugin>();
	private static Map<Class, IInventoryAdapter> inventoryAdapters = new HashMap<Class,IInventoryAdapter>();

	public static void checkEverything() {
	}

	public static void initializePlugins() {
		if( ConfigurationManager.ENABLE_MPS_PLUGIN ) {
			// Register ModularPowerSuits plug-in.
			Class mpsPlugin = ReflectionUtils.getClassByName( "xk.xact.plugin.mps.PluginForMPS" );
			if( mpsPlugin != null ) {
				Object instance = ReflectionUtils.newInstanceOf( mpsPlugin );
				if( instance != null ) {
					addPlugin( XACTPlugin.class.cast( instance ) );
				}
			}
		}

		// Load all other plugins.
		Utils.log( "Loading plug-ins..." );
		for( XACTPlugin plugin : plugins ) {
			plugin.initialize();
		}

		// Clear the list.
		plugins.clear();
		plugins = null;
	}

	public static void addPlugin(XACTPlugin plugin) {
		plugins.add( plugin );
	}

	public static void addSpecialCasedRecipe(SpecialCasedRecipe specialCase) {
		if( specialCase != null )
			specialCasedRecipes.add( specialCase );
	}

	public static List<SpecialCasedRecipe> getSpecialCasedRecipes() {
		return specialCasedRecipes;
	}

	public static Map<Class, IInventoryAdapter> getInventoryAdapters() {
		return Collections.unmodifiableMap( inventoryAdapters );
	}

	public static void registerInventoryAdapter(Class inventoryClass, IInventoryAdapter adapter) {
		if( inventoryClass != null && adapter != null ) {
			if( !inventoryClass.equals( IInventory.class ) ) {
				inventoryAdapters.put( inventoryClass, adapter );
			}
		}
	}
}
