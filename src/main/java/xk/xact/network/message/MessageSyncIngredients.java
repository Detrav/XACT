package xk.xact.network.message;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.item.ItemStack;

public class MessageSyncIngredients implements IMessage, IMessageHandler<MessageSyncRecipeChip, IMessage> {
	public ItemStack[] ingredients;
	public int offset;

	public MessageSyncIngredients() {
	}

	public MessageSyncIngredients(ItemStack[] ingredients, int offset) {
		this.ingredients = ingredients;
		this.offset = offset;
	}

	@Override
	public IMessage onMessage(MessageSyncRecipeChip message, MessageContext ctx) {
		
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		offset = buf.readByte();
		for (int i = 0; i < 9; ++i) {
			ingredients[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(offset);
		for (int i = 0; i < 9; ++i) {
			ByteBufUtils.writeItemStack(buf, ingredients[i]);
		}
	}	
}
