package xk.xact.network;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import xk.xact.core.ChipCase;
import xk.xact.core.CraftPad;
import xk.xact.core.tileentities.TileMachine;
import xk.xact.core.tileentities.TileWorkbench;
import xk.xact.gui.ContainerCase;
import xk.xact.gui.ContainerPad;
import xk.xact.gui.ContainerRecipe;
import xk.xact.gui.ContainerVanillaWorkbench;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	/**
	 * Register client-side rendering stuff.
	 */
	public void registerRenderInformation() {
	}

	/**
	 * Register keybinds, duh
	 */
	public void registerKeybindings() {
		// Only on client side
	}

	public void registerHandlers() {
		// nothin
	}

	@Override
	public Object getServerGuiElement(int GuiID, EntityPlayer player,
			World world, int x, int y, int z) {
		int ID = (GuiID & 0xFF);
		int meta = (GuiID >> 8) & 0xFFFF;

		// ID:
		// 0: machines
		// 1: library
		// 2: vanilla workbench
		// 3: craft pad
		// 4: <none> (client only)
		// 5: recipe

		if (ID == 0) { // Machines
			TileMachine machine = (TileMachine) world.getTileEntity(x, y, z);
			if (machine == null)
				return null;

			return machine.getContainerFor(player);
		}

		if (ID == 2) {
			TileWorkbench workbench = (TileWorkbench) world.getTileEntity(x, y,
					z);
			if (workbench == null)
				return null;

			return new ContainerVanillaWorkbench(workbench, player);
		}

		if (ID == 1) { // Chip Case
			ChipCase chipCase = new ChipCase(player.inventory.getCurrentItem());
			return new ContainerCase(chipCase, player);
		}

		if (ID == 3) { // Craft Pad
			int invSlot = meta == 0 ? player.inventory.currentItem : meta - 1;
			ItemStack item = player.inventory.mainInventory[invSlot];
			item.setItemDamage(1);
			CraftPad craftPad = new CraftPad(item, player);
			return new ContainerPad(craftPad, player, invSlot);
		}

		// no ID == 4. GuiPlan, client-side only.

		if (ID == 5) { // Set a recipe
			return new ContainerRecipe(player);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	private EntityPlayer fakePlayer;

	public EntityPlayer getFakePlayer(WorldServer world, int x, int y, int z) {
		if (fakePlayer == null) {
			fakePlayer = createFakePlayer(world);
		}
		fakePlayer.worldObj = world;
		fakePlayer.posX = x;
		fakePlayer.posY = y;
		fakePlayer.posZ = z;
		return fakePlayer;
	}

	public static boolean isFakePlayer(EntityPlayer player) {
		return player.getDisplayName().equals("[XACT]");
	}

	private EntityPlayer createFakePlayer(WorldServer world) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), "[XACT]");
		return new FakePlayer(world, profile);
	}

}
