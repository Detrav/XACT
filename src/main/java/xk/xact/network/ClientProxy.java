package xk.xact.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xk.xact.XActMod;
import xk.xact.client.KeyBindingHandler;
import xk.xact.client.Keybinds;
import xk.xact.client.gui.GuiCase;
import xk.xact.client.gui.GuiPad;
import xk.xact.client.gui.GuiVanillaWorkbench;
import xk.xact.client.render.ChipRenderer;
import xk.xact.core.ChipCase;
import xk.xact.core.CraftPad;
import xk.xact.core.tileentities.TileMachine;
import xk.xact.core.tileentities.TileWorkbench;
import xk.xact.gui.ContainerCase;
import xk.xact.gui.ContainerPad;
import xk.xact.gui.ContainerVanillaWorkbench;

public class ClientProxy extends CommonProxy {

	@SideOnly(Side.CLIENT)
	public static GuiScreen getCurrentScreen() {
		return Minecraft.getMinecraft().currentScreen;
	}

	@Override
	public void registerRenderInformation() {
		// Custom IItemRenderer
		MinecraftForgeClient.registerItemRenderer(XActMod.itemRecipeEncoded,
				new ChipRenderer());
	}

	@Override
	public void registerHandlers() {
		FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
	}

	@Override
	public void registerKeybindings() {
		ClientRegistry.registerKeyBinding(Keybinds.clear);
		ClientRegistry.registerKeyBinding(Keybinds.load);
		ClientRegistry.registerKeyBinding(Keybinds.prev);
		ClientRegistry.registerKeyBinding(Keybinds.next);
		ClientRegistry.registerKeyBinding(Keybinds.delete);
		ClientRegistry.registerKeyBinding(Keybinds.openGrid);
	}

	@Override
	public Object getClientGuiElement(int GuiID, EntityPlayer player,
			World world, int x, int y, int z) {
		int ID = (GuiID & 0xFF);
		int meta = (GuiID >> 8) & 0xFFFF;

		// ID:
		// 0: machines
		// 1: library
		// 2: vanilla workbench
		// 3: craft pad
		switch (ID) {
		case 0: // Machines
			TileMachine machine = (TileMachine) world.getTileEntity(new BlockPos(x, y, z));
			if (machine == null)
				return null;
			return machine.getGuiContainerFor(player);

		case 1: // Chip Case
			ChipCase chipCase = new ChipCase(player.inventory.getCurrentItem());
			return new GuiCase(new ContainerCase(chipCase, player));

		case 2: // Vanilla Workbench
			TileWorkbench workbench = (TileWorkbench) world.getTileEntity(new BlockPos(x, y, z));
			if (workbench == null)
				return null;

			return new GuiVanillaWorkbench(new ContainerVanillaWorkbench(
					workbench, player));

		case 3: // Craft Pad
			int invSlot = meta == 0 ? player.inventory.currentItem : meta - 1;
			CraftPad craftPad = new CraftPad(
					player.inventory.mainInventory[invSlot], player);
			return new GuiPad(craftPad, new ContainerPad(craftPad, player,
					invSlot));
		default:
			return null;
		}
	}

	public static NetHandlerPlayClient getNetClientHandler() {
		return Minecraft.getMinecraft().getNetHandler();
	}
}
