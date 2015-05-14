package xk.xact.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xk.xact.api.InteractiveCraftingContainer;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;

/*
 * Replaces the old Packethandler
 */
public class MessageSyncRecipeChip implements IMessage,
		IMessageHandler<MessageSyncRecipeChip, IMessage> {

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

		InteractiveCraftingContainer container = (InteractiveCraftingContainer) ((EntityPlayer) ctx
				.getServerHandler().playerEntity).openContainer;
		if (message.SlotID == -1) {
			container.setStack(-1, null);
			return null;
		}

		ItemStack stack = message.chip;
		if (stack != null)
			if (stack.getTagCompound() != null) {
				CraftRecipe recipe = CraftManager.decodeRecipe(stack);
			}
		container.setStack(message.SlotID, stack);
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
