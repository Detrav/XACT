package xk.xact.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import xk.xact.core.tileentities.TileCrafter;
import xk.xact.network.PacketHandler;

public class MessageNameCrafter  implements IMessage, IMessageHandler<MessageNameCrafter, IMessage> {

	private String name;
	
	/**
	 * The position of the crafter whose name to change
	 */
	private int x, y, z;
	
	public MessageNameCrafter() { }
	
	public MessageNameCrafter(String name, int x, int y, int z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(MessageNameCrafter message, MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
		
		if (te != null && te instanceof TileCrafter) {
			// Server notifies all clients in 128 range of the name change
			PacketHandler.INSTANCE.sendToAllAround(new MessageNameCrafterClient(message.name, message.x, message.y, message.z),
					new TargetPoint(te.getWorldObj().provider.dimensionId, message.x, message.y, message.z, 128D));

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
