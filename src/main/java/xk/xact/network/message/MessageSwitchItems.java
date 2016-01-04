package xk.xact.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Used to properly switch items between hot bar and Inventory
 */
public class MessageSwitchItems implements IMessage, IMessageHandler<MessageSwitchItems, IMessage> {
	
	/**
	 * The slot id where the item from the inventory shall be moved
	 */
	public byte idHotBarsStack;
	
	/**
	 * The slot id where the item form the hotbar shall be moved
	 */
	public byte idInvStack;
	
	/**
	 * The stack that is currently in the hotbar and shall be moved to the
	 * inventory
	 */
	public ItemStack hotbarStack;
	
	/**
	 * The stack that is currently in the inventory and shall be moved to the
	 * hot bar
	 */
	public ItemStack invStack;

	public MessageSwitchItems() {
	}

	public MessageSwitchItems(ItemStack stackInHotBar, int slotIdStackInHotBar,
			ItemStack stackInInv, int slotIdStackInInv) {
		this.hotbarStack = stackInHotBar;
		this.idInvStack = (byte) slotIdStackInHotBar;
		this.invStack = stackInInv;
		this.idHotBarsStack = (byte) slotIdStackInInv;

	}

	@Override
	public IMessage onMessage(MessageSwitchItems message, MessageContext ctx) {
		EntityPlayer player = ((EntityPlayer) ctx.getServerHandler().playerEntity);
		player.inventory.setInventorySlotContents(message.idHotBarsStack,
				message.invStack);
		player.inventory.setInventorySlotContents(message.idInvStack,
				message.hotbarStack);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.hotbarStack = ByteBufUtils.readItemStack(buf);
		this.invStack = ByteBufUtils.readItemStack(buf);
		this.idHotBarsStack = buf.readByte();
		this.idInvStack = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, hotbarStack);
		ByteBufUtils.writeItemStack(buf, invStack);
		buf.writeByte(idHotBarsStack);
		buf.writeByte(idInvStack);
	}

}
