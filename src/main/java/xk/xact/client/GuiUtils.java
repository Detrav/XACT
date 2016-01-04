package xk.xact.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import xk.xact.XactMod;
import xk.xact.gui.ContainerCrafter;
import xk.xact.gui.ContainerPad;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageSyncIngredients;
import xk.xact.network.message.MessageSyncRecipeChip;
import xk.xact.recipes.CraftManager;

@SideOnly(Side.CLIENT)
public class GuiUtils {

	private static int grayTone = 139;

	public static final int COLOR_RED = 180 << 16;
	public static final int COLOR_GREEN = 180 << 8;
	public static final int COLOR_BLUE = 220;
	public static final int COLOR_GRAY = (grayTone << 16) | (grayTone << 8)
			| grayTone;

	public static final RenderItem itemRender = new RenderItem();

	public static void paintSlotOverlay(Slot slot, int size, int color,
			int xOffset, int yOffset) {
		if (slot == null)
			return;

		int off = (size - 16) / 2;
		int minX = slot.xDisplayPosition - off + xOffset;
		int minY = slot.yDisplayPosition - off + yOffset;

		paintOverlay(minX, minY, size, color);
	}

	public static void paintIcon(Gui gui, IIcon icon, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
		gui.drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
	}

	@SideOnly(Side.CLIENT)
	public static void paintOverlay(int x, int y, int size, int color) {
		RenderHelper.enableGUIStandardItemLighting();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Gui.drawRect(x, y, x + size, y + size, color);
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void paintItem(ItemStack itemStack, int x, int y,
			Minecraft mc, RenderItem itemRenderer, float zLevel) {
		if (itemStack == null)
			return; // I might want to have a "null" image, like background
					// image.
		GL11.glPushMatrix();
		itemRenderer.zLevel = zLevel;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting(); // Fixes funny lighting

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer,
				mc.renderEngine, itemStack, x, y);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine,
				itemStack, x, y);
		//RenderHelper.disableStandardItemLighting();
		// GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		// GL11.glDisable(GL11.GL_DEPTH_TEST);
		itemRenderer.zLevel = 0.0F;
		GL11.glPopMatrix();
	}

	private static final ResourceLocation GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");

	public static void paintEffectOverlay(int x, int y,
			RenderItem itemRenderer, float red, float green, float blue,
			float alpha, float zLevel) {
		GL11.glPushMatrix();
		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		bindTexture(GLINT); // do I want to change this to something else?

		itemRenderer.zLevel += zLevel;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
		GL11.glColor4f(red, green, blue, alpha);
		effect(itemRenderer.zLevel, x - 21, y - 21, 18, 18);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		itemRenderer.zLevel += 50.0F;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glPopMatrix();
	}

	private static void effect(float zLevel, int x, int y, int width, int height) {

		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);

		for (int i = 0; i < 2; i++) {
			float var7 = 0.00390625F;
			float var8 = 0.00390625F;
			float var9 = (float) (Minecraft.getSystemTime() % (long) (3000 + i * 1873))
					/ (3000.0F + (float) (i * 1873)) * 256.0F;
			float var10 = 0.0F;
			Tessellator var11 = Tessellator.instance;
			float var12 = 4.0F;

			if (i == 1)
				var12 = -1.0F;

			var11.startDrawingQuads();
			var11.addVertexWithUV((double) x, (double) (y + height),
					(double) zLevel,
					(double) ((var9 + (float) height * var12) * var7),
					(double) ((var10 + (float) height) * var8));
			var11.addVertexWithUV(
					(double) (x + width),
					(double) (y + height),
					(double) zLevel,
					(double) ((var9 + (float) width + (float) height * var12) * var7),
					(double) ((var10 + (float) height) * var8));
			var11.addVertexWithUV((double) (x + width), (double) y,
					(double) zLevel, (double) ((var9 + (float) width) * var7),
					(double) ((var10 + 0.0F) * var8));
			var11.addVertexWithUV((double) x, (double) y, (double) zLevel,
					(double) ((var9 + 0.0F) * var7),
					(double) ((var10 + 0.0F) * var8));
			var11.draw();
		}
	}

	@SideOnly(Side.CLIENT)
	public static boolean isShiftKeyPressed() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	@SideOnly(Side.CLIENT)
	public static boolean isRevealKeyPressed() {
		return Keyboard
				.isKeyDown(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak
						.getKeyCode());
	}

	@SideOnly(Side.CLIENT)
	// item = the chip, slotID
	public static void sendItemToServer(byte slotID, ItemStack item) {
		PacketHandler.INSTANCE.sendToServer(new MessageSyncRecipeChip(item, slotID));
	}

	@SideOnly(Side.CLIENT)
	public static void sendItemsToServer(ItemStack[] items, int chipSlot) {
		if (items == null) {
			sendItemToServer((byte) -1, null);
			return;
		}
		PacketHandler.INSTANCE.sendToServer(new MessageSyncIngredients(items,
				chipSlot));
	}

	@SideOnly(Side.CLIENT)
	public static int getMouseX(Minecraft minecraft) {
		ScaledResolution resolution = new ScaledResolution(
				minecraft.getMinecraft(), minecraft.displayWidth,
				minecraft.displayHeight);
		int width = resolution.getScaledWidth();
		return Mouse.getX() * width / minecraft.displayWidth;
	}

	@SideOnly(Side.CLIENT)
	public static int getMouseY(Minecraft minecraft) {
		ScaledResolution resolution = new ScaledResolution(
				minecraft.getMinecraft(), minecraft.displayWidth,
				minecraft.displayHeight);
		int height = resolution.getScaledHeight();
		return height - Mouse.getY() * height / minecraft.displayHeight - 1;
	}

	@SideOnly(Side.CLIENT)
	public static Slot getHoveredSlot(int guiLeft, int guiTop) {
		GuiContainer gui = (GuiContainer) Minecraft.getMinecraft().currentScreen;
		if (gui == null)
			return null;

		Container container = Minecraft.getMinecraft().thePlayer.openContainer;
		int mouseX = getMouseX(Minecraft.getMinecraft()) - guiLeft;
		int mouseY = getMouseY(Minecraft.getMinecraft()) - guiTop;

		return getHoveredSlot(container, mouseX, mouseY, guiLeft, guiTop);
	}

	public static Slot getHoveredSlot(Container container, int mouseX,
			int mouseY, int guiLeft, int guiTop) {
		// Utils.debug("Getting slot at: (%s, %s)", mouseX, mouseY);
		for (int i = 0; i < container.inventorySlots.size(); i++) {
			Slot slot = container.getSlot(i);
			if (slot != null) {
				if (isMouseOverSlot(slot, mouseX - guiLeft, mouseY - guiTop)) {
					return slot;
				}
			}
		}
		return null;
	}

	public static boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		if (slot == null)
			return false;
		int xMin = slot.xDisplayPosition;
		int yMin = slot.yDisplayPosition;
		return mouseX >= xMin - 1 && mouseX < xMin + 16 + 1
				&& mouseY >= yMin - 1 && mouseY < yMin + 16 + 1;
	}

	@SideOnly(Side.CLIENT)
	public static World getWorld() {
		return Minecraft.getMinecraft().thePlayer.worldObj;
	}

	public static void bindTexture(String texture) {
		bindTexture(new ResourceLocation(texture));
	}

	public static void bindTexture(ResourceLocation resource) {
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
	}

	/**
	 * Draws the green status lights (Only on alternate textures)
	 */
	public static void drawLights(Container container,
			ResourceLocation guiTexture, GuiContainer gui, int guiLeft,
			int guiTop) {
		bindTexture(guiTexture);
		if (container instanceof ContainerCrafter) {
			for (int i = 0; i < 4; i++) {
				Slot chipSlot = container.getSlot(4 + i); // 8 Offset

				if (chipSlot.getHasStack()
						&& CraftManager.decodeRecipe(chipSlot.getStack()) != null)
					gui.drawTexturedModalRect(guiLeft
							+ chipSlot.xDisplayPosition, guiTop
							+ chipSlot.yDisplayPosition + 9, 176, 0, 3, 3);
			}
		} else if (container instanceof ContainerPad) {
			Slot chipSlot = container.getSlot(10);
			if (chipSlot.getHasStack()
					&& CraftManager.decodeRecipe(chipSlot.getStack()) != null)
				gui.drawTexturedModalRect(guiLeft + chipSlot.xDisplayPosition,
						guiTop + chipSlot.yDisplayPosition + 9, 190, 0, 3, 3);
		}

	}

	/**
	 * Sends a packet to the server to open the specified GUI.
	 *
	 * @param guiID
	 *            the gui id
	 */
	@SideOnly(Side.CLIENT)
	public static void openGui(int guiID) {
		Minecraft.getMinecraft().thePlayer.openGui(XactMod.instance, guiID,
				Minecraft.getMinecraft().thePlayer.worldObj, 0, 0, 0);
	}

}
