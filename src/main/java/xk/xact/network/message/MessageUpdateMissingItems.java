package xk.xact.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import xk.xact.api.InteractiveCraftingContainer;
import xk.xact.gui.ContainerCrafter;

/**
 * Updates the missing ingredients So that the grid highlights missing items
 * correctly I guess this replaces the old GuiTickHandler
 */
public class MessageUpdateMissingItems implements IMessage,
		IMessageHandler<MessageUpdateMissingItems, IMessage> {

	public MessageUpdateMissingItems() {
		// No vars needed
	}

	@Override
	public IMessage onMessage(MessageUpdateMissingItems message,
			MessageContext ctx) {
		if (ctx.getServerHandler().playerEntity.openContainer instanceof InteractiveCraftingContainer) {
			InteractiveCraftingContainer container = (InteractiveCraftingContainer) ctx.getServerHandler().playerEntity.openContainer;
			if (container != null && container instanceof ContainerCrafter) {
				((ContainerCrafter) container).crafter.updateState();
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

}
