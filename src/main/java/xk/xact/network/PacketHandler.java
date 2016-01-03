package xk.xact.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import xk.xact.network.message.*;
import xk.xact.util.References;

public class PacketHandler {// implements IPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.MOD_ID.toLowerCase());
	private static int ID = 0;
	public static void init() {
		// Register Network Messages
		INSTANCE.registerMessage(MessageSyncRecipeChip.class, MessageSyncRecipeChip.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageSyncIngredients.class, MessageSyncIngredients.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateMissingItems.class, MessageUpdateMissingItems.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageSwitchItems.class, MessageSwitchItems.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageSwitchGui.class, MessageSwitchGui.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageNameCrafter.class, MessageNameCrafter.class, ID++, Side.SERVER);
		INSTANCE.registerMessage(MessageNameCrafterClient.class, MessageNameCrafterClient.class, ID++, Side.CLIENT);

	}
}
