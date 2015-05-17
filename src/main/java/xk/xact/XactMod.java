package xk.xact;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xk.xact.config.ConfigurationManager;
import xk.xact.core.Machines;
import xk.xact.core.blocks.BlockMachine;
import xk.xact.core.items.ItemMachine;
import xk.xact.core.items.XactBaseItem;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.core.tileentities.TileWorkbench;
import xk.xact.gui.CreativeTabXACT;
import xk.xact.network.CommonProxy;
import xk.xact.network.PacketHandler;
import xk.xact.plugin.PluginManager;
import xk.xact.util.References;
import xk.xact.util.References.Registry;

/**
 * XACT adds an electronic crafting table capable of reading recipes encoded
 * into chips.
 */
@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION, useMetadata = true, guiFactory = References.GUI_FACTORY_CLASS)
// @NetworkMod(clientSideRequired = true, serverSideRequired = true,
// channels = { "xact_channel" }, packetHandler = PacketHandler.class)
public class XactMod {

	@Mod.Instance("xact")
	public static XactMod instance;

	@SidedProxy(clientSide = "xk.xact.network.ClientProxy", serverSide = "xk.xact.network.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	// Items
	public static XactBaseItem itemRecipeBlank;
	public static XactBaseItem itemRecipeEncoded;
	public static XactBaseItem itemChipCase;
	public static XactBaseItem itemCraftPad;

	// Blocks
	public static BlockMachine blockMachine;
	public static BlockMachine blockWorkbench;

	public static CreativeTabXACT xactTab;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Load Configurations
		ConfigurationManager.loadConfiguration(event
				.getSuggestedConfigurationFile());

		PacketHandler.init();
		// Initialize the logger. (I think this is useless as of 1.7.10)
		logger = Logger.getLogger("XACT-"
				+ FMLCommonHandler.instance().getEffectiveSide());

		// Load keybinds
		proxy.registerKeybindings();
	}

	@EventHandler
	public void initializeAll(FMLInitializationEvent event) {

		xactTab = new CreativeTabXACT();

		// Init Items
		ConfigurationManager.initItems();

		// Init Blocks
		ConfigurationManager.initBlocks();

		// Register side-sensitive Stuff
		proxy.registerRenderInformation();

		// Register keybind handler
		proxy.registerHandlers();

		// Register Blocks
		GameRegistry.registerBlock(blockMachine, ItemMachine.class,
				Registry.BLOCKMACHINE);
		if (blockWorkbench != null) {
			GameRegistry.registerBlock(blockWorkbench, Registry.BLOCKCRAFTINGTABLE);
		}

		GameRegistry.registerItem(itemChipCase,
				References.Registry.ITEMCHIPCASE);
		
		GameRegistry.registerItem(itemCraftPad,
				References.Registry.ITEMCRAFTPAD);
		GameRegistry.registerItem(itemRecipeEncoded,
				References.Registry.ITEMRECIPEENCODED);
		GameRegistry.registerItem(itemRecipeBlank,
				References.Registry.ITEMRECIPEBLANK);
		
		//Register the models/textures. Only on client
		if (event.getSide() == Side.CLIENT) {
			itemChipCase.init();
			itemRecipeEncoded.init();
			itemCraftPad.init();
			itemRecipeBlank.init();
		}
		
		// Register TileEntities
		GameRegistry.registerTileEntity(TileCrafter.class, "tile.xact.Crafter");
		GameRegistry.registerTileEntity(TileWorkbench.class,
				"tile.xact.VanillaWorkbench");

		// Register GUIs
		NetworkRegistry.INSTANCE.registerGuiHandler(XactMod.instance, proxy);
		// FMLCommonHandler.instance().bus().register(ConfigurationManager.instance);

		// Add the recipes
		addRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PluginManager.checkEverything();
		PluginManager.initializePlugins();
		//loads textures
		blockMachine.postInit();
		if (blockWorkbench != null)
			blockWorkbench.postInit();
	}

	private void addRecipes() {
		ItemStack[] ingredients;

		// Recipe Chip
		GameRegistry.addRecipe(new ItemStack(itemRecipeBlank, 16),
				new String[] { "ii", "ir", "gg" }, 'i', Items.iron_ingot, 'r',
				Items.redstone, 'g', Items.gold_nugget);

		// Chip Case
		ItemStack chip = new ItemStack(itemRecipeBlank);
		GameRegistry.addRecipe(new ShapedOreRecipe(itemChipCase, new String[] {
				"cgc", "c c", "wCw" }, 'c', chip, 'g', Blocks.glass_pane, 'w',
				"plankWood", 'C', Blocks.chest));

		// Craft Pad
		GameRegistry.addRecipe(new ItemStack(itemCraftPad), "ii0", "icr",
				"000", 'i', new ItemStack(Items.iron_ingot), 'c',
				new ItemStack(Blocks.crafting_table), 'r', new ItemStack(
						itemRecipeBlank));

		if (blockWorkbench != null)
			GameRegistry.addRecipe(new ShapedOreRecipe(XactMod.blockWorkbench,
					new String[] { "0w0", "wcw", "0w0" }, 'w', "plankWood",
					'c', Blocks.crafting_table));

		for (Machines machine : Machines.values()) {
			GameRegistry.addRecipe(machine.getMachineRecipe());
		}
	}

}
