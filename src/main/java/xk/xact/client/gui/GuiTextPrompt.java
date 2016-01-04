package xk.xact.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import xk.xact.network.PacketHandler;
import xk.xact.network.message.MessageNameCrafter;
import xk.xact.network.message.MessageSwitchGui;
import xk.xact.util.References;
import xk.xact.util.References.Localization;
import xk.xact.util.Textures;
import xk.xact.util.Utils;

/**
 * Client-Side Message window prompting for text input
 * Does not have a container
 */
public class GuiTextPrompt extends GuiScreen {
	private static final ResourceLocation promptTexture = new ResourceLocation(Textures.GUI_PROMPT);
	
	private static final int HEIGHT = 58;
	private static final int WIDTH = 176;
	private GuiTextField textBox;
	
	/**
	 * The position of the crafter which this dialog is connected to
	 */
	private int xCrafter, yCrafter, zCrafter; 
	
	public GuiTextPrompt(int xCrafter, int yCrafter, int zCrafter) {
		this.xCrafter = xCrafter; 
		this.yCrafter = yCrafter; 
		this.zCrafter = zCrafter;
	}
	
	@Override
	public void initGui() {
		// Init textfield
		this.textBox = new GuiTextField(this.fontRendererObj, getCenterX() - 78, getCenterY() - 9, 153, 12);
        this.textBox.setTextColor(-1);
        this.textBox.setDisabledTextColour(-1);
        this.textBox.setEnableBackgroundDrawing(false);
        this.textBox.setMaxStringLength(27);
        
        // Buttons
        GuiButton accept = new GuiButton(0, getCenterX() + 20, getCenterY() + 4, 60, 20, I18n.format(Localization.BUTTON_ACCEPT));
        GuiButton cancel = new GuiButton(1, getCenterX() - 43, getCenterY() + 4, 60, 20, I18n.format(Localization.BUTTON_CANCEL));
        
        buttonList.add(accept);
        buttonList.add(cancel);
        Keyboard.enableRepeatEvents(true);
		super.initGui();
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
        
        // Return to the gui of the crafter
        PacketHandler.INSTANCE.sendToServer(new MessageSwitchGui(xCrafter, yCrafter, zCrafter));
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawWorldBackground(0);
		mc.renderEngine.bindTexture(promptTexture);
		drawTexturedModalRect(getCenterX() - WIDTH / 2, getCenterY() - HEIGHT / 2, 0, 0, WIDTH, HEIGHT);
		fontRendererObj.drawString(I18n.format(References.Localization.PROMPT_TITLE),  getCenterX() - 50, getCenterY() - 22, Utils.argbToInt(255, 73, 73, 73));
		textBox.xPosition = getCenterX() - 78;
		textBox.yPosition = getCenterY() - 9;
		textBox.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTick);
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		 if (!this.textBox.textboxKeyTyped(c, i))
			 super.keyTyped(c, i);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textBox.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0: // Accept 
			if (textBox != null && !textBox.getText().equals("")
			    && !textBox.getText().equals(I18n.format(Localization.CRAFTER_TITLE))
			    && !textBox.getText().equals(I18n.format(Localization.TOOLTIP_ADJACENTCRAFTER))) {
				
				PacketHandler.INSTANCE.sendToServer(new MessageNameCrafter(textBox.getText(), xCrafter, yCrafter, zCrafter));
				onGuiClosed(); // Close and save the name	
			}
			break;
		case 1: // Cancel
			onGuiClosed(); // Just Close
			break;
		default:
			break;
		}
		super.actionPerformed(button);
	}
	
	private int getCenterX() {
		ScaledResolution res = new ScaledResolution(mc.getMinecraft(), mc.getMinecraft().displayWidth, mc.getMinecraft().displayHeight);
		return res.getScaledWidth() / 2;
	}
	
	private int getCenterY() {
		ScaledResolution res = new ScaledResolution(mc.getMinecraft(), mc.getMinecraft().displayWidth, mc.getMinecraft().displayHeight);
		return res.getScaledHeight() / 2;
	}
}
