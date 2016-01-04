package xk.xact.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import xk.xact.config.ConfigurationManager;

@SideOnly(Side.CLIENT)
public final class Textures {

	public static final String TEXTURES_ROOT = References.MOD_ID + ":textures/";

	// GUI Textures
	public static final String GUI_CRAFTER = TEXTURES_ROOT + "gui/GuiCrafter" + (ConfigurationManager.ENABLE_ALT_TEXTURES ? "_alt.png" : ".png");
	public static final String GUI_PAD = TEXTURES_ROOT + "gui/GuiPad" + (ConfigurationManager.ENABLE_ALT_TEXTURES ? "_alt.png" : ".png");
	public static final String GUI_CASE = TEXTURES_ROOT + "gui/GuiCase.png";
	public static final String GUI_RECIPE = TEXTURES_ROOT + "gui/GuiRecipe.png";
	public static final String GUI_WORKBENCH = "textures/gui/container/crafting_table.png";
	public static final String GUI_PROMPT = TEXTURES_ROOT + "gui/GuiTextPrompt.png";
	
	// NEI: Usage Handler
	public static final String NEI_CHIP_HANDLER = TEXTURES_ROOT + "gui/ChipHandler.png";

	// Other
	public static final String MISC_BUTTONS = TEXTURES_ROOT + "other/buttons_1.png";
	

	// Items
	public static final String ITEM_CASE = References.MOD_ID + ":case";
	public static final String ITEM_CHIP_BLANK = References.MOD_ID + ":chip_blank";
	public static final String ITEM_CHIP_ENCODED = References.MOD_ID + ":chip_encoded";
	public static final String ITEM_CHIP_INVALID = References.MOD_ID + ":chip_invalid";
	public static final String ITEM_PAD_ON = References.MOD_ID + ":pad_on" + (ConfigurationManager.ENABLE_ALT_TEXTURES ? "_alt" : "");
	public static final String ITEM_PAD_OFF = References.MOD_ID + ":pad_off" + (ConfigurationManager.ENABLE_ALT_TEXTURES ? "_alt" : "");


	// Block Texture: Crafter
	public static final String CRAFTER_TOP = References.MOD_ID + ":crafter_top";
	public static final String CRAFTER_BOTTOM = References.MOD_ID + ":crafter_bottom";
	public static final String CRAFTER_FRONT = References.MOD_ID + ":crafter_front";
	public static final String CRAFTER_SIDE = References.MOD_ID + ":crafter_side";

	// Block Texture: Workbench
	public static final String WORKBENCH_TOP = "crafting_table_top";
	public static final String WORKBENCH_BOTTOM = "planks_spruce";
	public static final String WORKBENCH_FRONT = "crafting_table_front";
	public static final String WORKBENCH_SIDE = "crafting_table_side";

}
