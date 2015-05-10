package xk.xact.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xk.xact.XActMod;
import xk.xact.api.InteractiveCraftingGui;
import xk.xact.inventory.InventoryUtils;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageSwitchItems;
import xk.xact.network.message.MessageUpdateMissingItems;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBindingHandler {// extends KeyBindingRegistry.KeyHandler {

	// public KeyBindingHandler() {
	// super( keyBindings(), repeatings() );
	// }

	// @Override
	// public String getLabel() {
	// return "xact test bindings";
	// }

	private static KeyBinding getPressedKeybinding() {
		if (Keybinds.clear.getIsKeyPressed()) {
			
			return Keybinds.clear;
		} else if (Keybinds.delete.getIsKeyPressed()) {
			return Keybinds.delete;
		} else if (Keybinds.load.getIsKeyPressed()) {
			return Keybinds.load;
		} else if (Keybinds.next.getIsKeyPressed()) {
			return Keybinds.next;
		} else if (Keybinds.openGrid.getIsKeyPressed()) {
			return Keybinds.openGrid;
		} else if (Keybinds.prev.getIsKeyPressed()) {
			return Keybinds.prev;
		}

		return null;
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
		if (FMLClientHandler.instance().getClient().inGameHasFocus
				&& getPressedKeybinding() != null) {
			if (FMLClientHandler.instance().getClientPlayerEntity() != null) {
				EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
				{
					GuiScreen currentScreen = FMLClientHandler.instance().getClient().currentScreen;

					if (currentScreen == null) {
						if (getPressedKeybinding().equals(Keybinds.openGrid)) {
							int slot = getFirstCraftPad(entityPlayer);//getCraftPadIndex();
							if (slot != -1) {
								ItemStack heldItem = entityPlayer.getHeldItem();
								ItemStack craftPad = entityPlayer.inventory.getStackInSlot(slot);
								// Save the slot the pad was in, so it can be put back there
								if (craftPad.stackTagCompound == null)
									craftPad.stackTagCompound = new NBTTagCompound();
								
								craftPad.stackTagCompound.setByte("originalSlot", (byte) slot);
								//Now switch them
								PacketHandler.INSTANCE.sendToServer(new MessageSwitchItems(heldItem, slot, craftPad, entityPlayer.inventory.currentItem));
//								Minecraft.getMinecraft().playerController.sendUseItem(entityPlayer, entityPlayer.worldObj, entityPlayer.inventory.getCurrentItem());
							}	
						}
						return;
					}
					if (currentScreen instanceof InteractiveCraftingGui) {
						((InteractiveCraftingGui) currentScreen)
								.handleKeyBinding(getPressedKeybinding()
										.getKeyCode(), getPressedKeybinding()
										.getKeyDescription());
					}

				}
			}
		}
	}

	private static KeyBinding[] keyBindings() {
		return new KeyBinding[] { Keybinds.clear, Keybinds.load, Keybinds.prev,
				Keybinds.next, Keybinds.delete, Keybinds.openGrid };
	}

	private static boolean[] repeatings() {
		return new boolean[] { false, false, false, false, false, false };
	}

	private int getCraftPadIndex() {
		return InventoryUtils.checkHotbar(Minecraft.getMinecraft().thePlayer,
				new ItemStack(XActMod.itemCraftPad));
	}
	
	private int getFirstCraftPad(EntityPlayer player)  {
		InventoryPlayer playerinv = player.inventory;
		for (int i = 9; i < playerinv.getSizeInventory(); i++) {
			if (playerinv.getStackInSlot(i) != null && playerinv.getStackInSlot(i).getItem().equals(XActMod.itemCraftPad))
				return i;
		}
		return -1;
	}
}
