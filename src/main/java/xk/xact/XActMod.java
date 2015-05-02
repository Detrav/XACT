package xk.xact;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xk.xact.config.ConfigurationManager;
import xk.xact.core.Machines;
import xk.xact.core.items.ItemMachine;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.core.tileentities.TileWorkbench;
import xk.xact.gui.CreativeTabXACT;
import xk.xact.network.CommonProxy;
import xk.xact.network.PacketHandler;
import xk.xact.plugin.PluginManager;
import xk.xact.recipes.RecipeUtils;
import xk.xact.util.References;
import xk.xact.util.Utils;
import xk.xact.util.References.Registry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * XACT adds an electronic crafting table capable of reading recipes encoded into chips.
 */
@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION, useMetadata = true)
//@NetworkMod(clientSideRequired = true, serverSideRequired = true,
//		channels = { "xact_channel" }, packetHandler = PacketHandler.class)
public class XActMod {


	@Mod.Instance("xact")
	public static XActMod instance;

	@SidedProxy(clientSide = "xk.xact.network.ClientProxy", serverSide = "xk.xact.network.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	// Items
	public static Item itemRecipeBlank;
	public static Item itemRecipeEncoded;
	public static Item itemChipCase;
	public static Item itemCraftPad;

	// Blocks
	public static Block blockMachine;
	public static Block blockWorkbench;

	public static CreativeTabXACT xactTab;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Load Configurations
		ConfigurationManager.loadConfiguration( event.getSuggestedConfigurationFile() );
		
		PacketHandler.init();
		// Initialize the logger. (I think this is useless as of 1.7.10)
		logger = Logger.getLogger( "XACT-" + FMLCommonHandler.instance().getEffectiveSide() );
		
		//Load keybinds
		proxy.registerKeybindings();
	}

	@Mod.EventHandler
	public void initializeAll(FMLInitializationEvent ignoredEvent) {
		
		xactTab = new CreativeTabXACT();

		// Init Items
		ConfigurationManager.initItems();

		// Init Blocks
		ConfigurationManager.initBlocks();

		// Register side-sensitive Stuff
		proxy.registerRenderInformation();

		// Register Blocks
		GameRegistry.registerBlock( blockMachine, ItemMachine.class, "XACT Machine" );
		if( blockWorkbench != null ) {
			GameRegistry.registerBlock( blockWorkbench, "XACT Workbench" );
		}
		
		GameRegistry.registerItem(itemChipCase, References. Registry.ITEMCHIPCASE);
		GameRegistry.registerItem(itemCraftPad, References.Registry.ITEMCRAFTPAD);
		GameRegistry.registerItem(itemRecipeEncoded, References.Registry.ITEMRECIPEENCODED);
		GameRegistry.registerItem(itemRecipeBlank, References.Registry.ITEMRECIPEBLANK);
		
		// Register TileEntities
		GameRegistry.registerTileEntity( TileCrafter.class, "tile.xact.Crafter" );
		GameRegistry.registerTileEntity( TileWorkbench.class, "tile.xact.VanillaWorkbench" );


		// Register GUIs
		NetworkRegistry.INSTANCE.registerGuiHandler( XActMod.instance, proxy );

		// Add the recipes
		addRecipes();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PluginManager.checkEverything();
		PluginManager.initializePlugins();
	}

	private void addRecipes() {
		ItemStack[] ingredients;

		// Recipe Chip
		GameRegistry.addRecipe( new ItemStack( itemRecipeBlank, 16 ),
				new String[] { "ii", "ir", "gg" },
				'i', Items.iron_ingot,
				'r', Items.redstone,
				'g', Items.gold_nugget
		);

		// Chip Case
		ItemStack chip = new ItemStack( itemRecipeBlank );
		GameRegistry.addRecipe( new ShapedOreRecipe( itemChipCase,
				new String[] { "cgc", "c c", "wCw" },
				'c', chip,
				'g', Blocks.glass_pane,
				'w', "plankWood",
				'C', Blocks.chest
		) );

		// Craft Pad
		ingredients = RecipeUtils.ingredients(
				Items.iron_ingot, Items.iron_ingot, null,
				Items.iron_ingot, Blocks.crafting_table, chip,
				null, null, null
		);
		
		if (blockWorkbench != null)
			GameRegistry.addRecipe(new ShapedOreRecipe(XActMod.blockWorkbench, new String[] { "0w0", "wcw", "0w0"}, 'w', "plankWood", 'c', Blocks.crafting_table));	
		
		for( Machines machine : Machines.values() ) {
			GameRegistry.addRecipe( machine.getMachineRecipe() );
		}
	}

}
