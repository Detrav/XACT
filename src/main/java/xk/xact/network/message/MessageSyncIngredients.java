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
import xk.xact.gui.ContainerVanillaWorkbench;
import xk.xact.recipes.CraftManager;
import xk.xact.recipes.CraftRecipe;

public class MessageSyncIngredients implements IMessage,
		IMessageHandler<MessageSyncIngredients, IMessage> {
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
		// Get the player
		EntityPlayer thePlayer = ctx.getServerHandler().playerEntity;

		// Get the conatiner
		InteractiveCraftingContainer container = (InteractiveCraftingContainer) thePlayer.openContainer;

		if (message.chipSlot == -1) { // If this is the case the player imports
										// a recipe from NEI
			if (container != null && container instanceof ContainerCrafter) { // Player tries  to import into crafter
				for (int i = 0; i < 9; i++) {
					container.setStack(8 + i, message.ingredients[i]);
				}
			} else if (container != null && container instanceof ContainerPad) { // Player tries to import into pad
				for (int i = 0; i < 9; i++) {
					container.setStack(1 + i, message.ingredients[i]);
				}
			} else if(container != null && container instanceof ContainerVanillaWorkbench) {
				for (int i = 0; i < 9; i++) {
					container.setStack(1 + i, message.ingredients[i]);
				}
			}
		} else {
			CraftRecipe recipe = CraftManager.generateRecipe(
					message.ingredients, thePlayer.worldObj);
			ItemStack encodedChip = CraftManager.encodeRecipe(recipe);

			container.setStack(message.chipSlot, encodedChip); // Replace the
																// old chip with
																// the new
																// encoded one
		}
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
