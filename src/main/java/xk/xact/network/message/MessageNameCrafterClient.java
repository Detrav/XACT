package xk.xact.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import xk.xact.core.tileentities.TileCrafter;

public class MessageNameCrafterClient implements IMessage, IMessageHandler<MessageNameCrafterClient, IMessage> {

	private String name;
	
	/**
	 * The position of the crafter whose name to change
	 */
	private int x, y, z;
	
	public MessageNameCrafterClient() { }
	
	public MessageNameCrafterClient(String name, int x, int y, int z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(MessageNameCrafterClient message, MessageContext ctx) {
		TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x, message.y, message.z);
		
		if (te != null && te instanceof TileCrafter) {

			((TileCrafter) te).crafterName = message.name;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		
		this.name = ByteBufUtils.readUTF8String(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);

		ByteBufUtils.writeUTF8String(buf, name);
	}

}
