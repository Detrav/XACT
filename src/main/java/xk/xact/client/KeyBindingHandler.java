package xk.xact.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import xk.xact.XactMod;
import xk.xact.inventory.InventoryUtils;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageSwitchItems;

@SideOnly(Side.CLIENT)
public class KeyBindingHandler {// extends KeyBindingRegistry.KeyHandler {

	public static KeyBinding getPressedKeybinding() {
		if (Keyboard.isKeyDown(Keybinds.clear.getKeyCode())) {
			return Keybinds.clear;
		} else if (Keyboard.isKeyDown(Keybinds.delete.getKeyCode())) {
			return Keybinds.delete;
		} else if (Keyboard.isKeyDown(Keybinds.load.getKeyCode())) {
			return Keybinds.load;
		} else if (Keyboard.isKeyDown(Keybinds.next.getKeyCode())) {
			return Keybinds.next;
		} else if (Keyboard.isKeyDown(Keybinds.openGrid.getKeyCode())) {
			return Keybinds.openGrid;
		} else if (Keyboard.isKeyDown(Keybinds.prev.getKeyCode())) {
			return Keybinds.prev;
		}

		return null;
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
		if (getPressedKeybinding() != null) {
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
								if (craftPad.getTagCompound() == null)
									craftPad.setTagCompound(new NBTTagCompound());

								craftPad.getTagCompound().setByte("originalSlot", (byte) (slot + 1));
								//Now switch them
								PacketHandler.INSTANCE.sendToServer(new MessageSwitchItems(heldItem, slot, craftPad, entityPlayer.inventory.currentItem));
							}	
						}
						return;
					}
				}
			}
		}
	}

	private int getCraftPadIndex() {
		return InventoryUtils.checkHotbar(Minecraft.getMinecraft().thePlayer,
				new ItemStack(XactMod.itemCraftPad));
	}
	
	private int getFirstCraftPad(EntityPlayer player)  {
		InventoryPlayer playerinv = player.inventory;
		for (int i = 0; i < playerinv.getSizeInventory(); i++) {
			if (playerinv.getStackInSlot(i) != null && playerinv.getStackInSlot(i).getItem().equals(XactMod.itemCraftPad))
				return i;
		}
		return -1;
	}
}
