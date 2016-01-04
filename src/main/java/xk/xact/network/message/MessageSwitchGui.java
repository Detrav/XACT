package xk.xact.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import xk.xact.XactMod;
import xk.xact.core.tileentities.TileCrafter;

public class MessageSwitchGui implements IMessage, IMessageHandler<MessageSwitchGui, IMessage>{
	
	public int x, y, z;
	
	public MessageSwitchGui() { }
	
	public MessageSwitchGui(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(MessageSwitchGui message, MessageContext ctx) {
		EntityPlayer player = ((EntityPlayer) ctx.getServerHandler().playerEntity);
		
	
		if(player != null && player.worldObj != null) {
			TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
			if (te != null && te instanceof TileCrafter)
				player.openGui(XactMod.instance, 0, player.worldObj, message.x, message.y, message.z);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

}
