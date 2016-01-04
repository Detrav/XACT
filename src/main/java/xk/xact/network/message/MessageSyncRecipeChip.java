package xk.xact.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xk.xact.api.InteractiveCraftingContainer;
import xk.xact.gui.ContainerCrafter;
import xk.xact.gui.ContainerPad;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;

/*
 * Replaces the old Packethandler
 */
public class MessageSyncRecipeChip implements IMessage, IMessageHandler<MessageSyncRecipeChip, IMessage> {

	public ItemStack chip;
	public byte SlotID;

	public MessageSyncRecipeChip() {
	}

	public MessageSyncRecipeChip(ItemStack chip, byte slotid) {
		this.SlotID = slotid;
		this.chip = chip;
	}

	@Override
	public IMessage onMessage(MessageSyncRecipeChip message, MessageContext ctx) {
		InteractiveCraftingContainer container = (InteractiveCraftingContainer) ((EntityPlayer) ctx.getServerHandler().playerEntity).openContainer;
		
		if (container != null) {
			if (container instanceof ContainerCrafter) {
				if (message.SlotID == -1) {
					container.setStack(-1, null);
					return null;
				}

				ItemStack stack = message.chip;
				if (stack != null)
					if (stack.stackTagCompound != null) {
						CraftRecipe recipe = CraftManager.decodeRecipe(stack);
					}
				container.setStack(message.SlotID, stack);
				((ContainerCrafter) container).crafter.updateState();				
			} else if (container instanceof ContainerPad) {
				if (message.SlotID == -1) {
					container.setStack(-1, null);
					return null;
				}
				ItemStack stack = message.chip;
				container.setStack(message.SlotID, stack);
			}
		}
		
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.SlotID = buf.readByte();
		this.chip = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(SlotID);
		ByteBufUtils.writeItemStack(buf, chip);
	}
}
