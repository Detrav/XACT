package xk.xact.util;

import net.minecraft.block.properties.PropertyDirection;

public class References {
	public static final String MOD_ID = "xact";
	public static final String MOD_NAME = "XACT Mod";
	public static final String VERSION = "0.5.2";
	public static final String GUI_FACTORY_CLASS = "xk.xact.config.GuiFactory";
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static class Registry {
		public static final String ITEMCHIPCASE = "chipcase";
		public static final String ITEMCRAFTPAD = "craftpad";
		public static final String ITEMRECIPEENCODED = "recipeencoded";
		public static final String ITEMRECIPEBLANK = "recipeblank";
		public static final String BLOCKMACHINE = "XACTMachine";
		public static final String BLOCKCRAFTINGTABLE = "XACTWorkbench";
	}

	public static class Unlocalized	 {
		public static final String PREFIX = MOD_ID + ":";
		public static final String ITEMCHIPCASE = PREFIX + Registry.ITEMCHIPCASE;
		public static final String ITEMCRAFTPAD = PREFIX + Registry.ITEMCRAFTPAD;
		public static final String ITEMRECIPEENCODED = PREFIX + Registry.ITEMRECIPEENCODED;
		public static final String ITEMRECIPEBLANK = PREFIX +  Registry.ITEMRECIPEBLANK;
		public static final String BLOCKMACHINE = PREFIX + Registry.BLOCKMACHINE;
		public static final String BLOCKCRAFTINGTABLE = PREFIX + Registry.BLOCKCRAFTINGTABLE;
	}
	
	public static class Keys {
		public static final String KEY_CATEGORY = "key.categories." + MOD_ID;
		public static final String KEY_CLEAR = MOD_ID + ".key.clear";
		public static final String KEY_LOAD = MOD_ID + ".key.load";
		public static final String KEY_PREV = MOD_ID + ".key.prev";
		public static final String KEY_NEXT = MOD_ID + ".key.next";
		public static final String KEY_DELETE = MOD_ID + ".key.delete";
		public static final String KEY_REVEAL = MOD_ID + ".key.reveal";
		public static final String KEY_OPENGRID = MOD_ID + ".key.opengrid";
	}
	
	public static class Localization {
		public static final String CRAFTPAD_GUITITLE = "gui.xact:craftpad.name";
		public static final String CRAFTPAD_CHIPTITLE = "gui.xact:craftpad.chip.name";
		public static final String CRAFTER_TITLE = "gui.xact:crafter.name";
		public static final String CRAFTER_INVENTORY = "gui.xact:crafter.inventory.name";
		public static final String CHIP_BLANK = "item.xact:recipechip.blank.desc";
		public static final String CHIP_INVALID = "item.xact:recipechip.invalid.desc";
		public static final String CHIP_RECIPE = "item.xact:recipechip.recipe";
		
		public static final String TOOLTIP_CLEAR = "gui.xact:tipclear";
		public static final String TOOLTIP_SAVE = "gui.xact:tipsave";
		public static final String TOOLTIP_CLEARGRID = "gui.xact:tipcleargrid";
		public static final String TOOLTIP_NEXTRECIPE = "gui.xact:tipnextrecipe";
		public static final String TOOLTIP_LASTRECIPE = "gui.xact:tiplastrecipe";
	}
}
