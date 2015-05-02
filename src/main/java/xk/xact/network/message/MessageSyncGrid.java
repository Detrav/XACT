package xk.xact.network.message;

import net.minecraft.entity.player.EntityPlayer;
import xk.xact.api.InteractiveCraftingContainer;
import xk.xact.gui.ContainerCrafter;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncGrid implements IMessage, IMessageHandler<MessageSyncGrid, IMessage> {
	
	public MessageSyncGrid() {
		// No vars needed
	}
	@Override
	public IMessage onMessage(MessageSyncGrid message, MessageContext ctx) {
		InteractiveCraftingContainer container = (InteractiveCraftingContainer) ((EntityPlayer) ctx.getServerHandler().playerEntity).openContainer;
		if (container != null && container instanceof ContainerCrafter) {
			((ContainerCrafter) container).detectAndSendChanges();
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

}
