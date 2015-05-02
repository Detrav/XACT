package xk.xact.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xk.xact.api.InteractiveCraftingContainer;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncIngredients implements IMessage, IMessageHandler<MessageSyncIngredients, IMessage> {
	/**
	 * The ingredients that shall be saved onto a chip
	 */
	public ItemStack[] ingredients = new ItemStack[9];
	
	/**
	 * The slotid the chip is located in
	 */
	public byte chipSlot;

	public MessageSyncIngredients() {
	}

	public MessageSyncIngredients(ItemStack[] ingredients, int chipSlotID) {
		this.ingredients = ingredients;
		this.chipSlot = (byte) chipSlotID;
	}

	@Override
	public IMessage onMessage(MessageSyncIngredients message, MessageContext ctx) {
		//Get the player
		EntityPlayer thePlayer = ctx.getServerHandler().playerEntity;
		
		//Get the conatiner
		InteractiveCraftingContainer container = (InteractiveCraftingContainer) thePlayer.openContainer;
		
		CraftRecipe recipe = CraftManager.generateRecipe(message.ingredients, thePlayer.worldObj);
		ItemStack encodedChip = CraftManager.encodeRecipe(recipe);
		
		container.setStack(message.chipSlot, encodedChip); // Replace the old chip with the new encoded one
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.chipSlot = buf.readByte();
		for (int i = 0; i < 9; i++) {
			this.ingredients[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(chipSlot);
		for (int i = 0; i < 9; i++) {
			ByteBufUtils.writeItemStack(buf, ingredients[i]);
		}
	}
}
