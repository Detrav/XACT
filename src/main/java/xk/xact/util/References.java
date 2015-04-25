package xk.xact.util;

public class References {
	public static final String MOD_ID = "xact";
	public static final String MOD_NAME = "XACT Mod";
	public static final String VERSION = "0.4.3";
	public static class Registry {
		public static final String ITEMCHIPCASE = "chipCase";
		public static final String ITEMCRAFTPAD = "craftPad";
		public static final String ITEMRECIPEENCODED = "recipeEncoded";
		public static final String ITEMRECIPEBLANK = "recipeBlank";
	}

	public static class Unlocalized	 {
		public static final String PREFIX = MOD_ID + ":";
		public static final String ITEMCHIPCASE = PREFIX + Registry.ITEMCHIPCASE;
		public static final String ITEMCRAFTPAD = PREFIX + Registry.ITEMCRAFTPAD;
		public static final String ITEMRECIPEENCODED = PREFIX + Registry.ITEMRECIPEENCODED;
		public static final String ITEMRECIPEBLANK = PREFIX +  Registry.ITEMRECIPEBLANK;

	}
	
	public static class Localization {
		public static final String CRAFTPAD_GUITITLE = "gui.xact:craftpad.name";
		public static final String CRAFTPAD_CHIPTITLE = "gui.xact:craftpad.chip.name";
		public static final String CRAFTER_TITLE = "gui.xact:crafter.name";
		public static final String CRAFTER_INVENTORY = "gui.xact:crafter.inventory.name";
		public static final String CHIP_BLANK = "item.xact:recipeChip.blank.desc";
		public static final String CHIP_INVALID = "item.xact:recipeChip.invalid.desc";
		public static final String CHIP_RECIPE = "item.xact:recipeChip.recipe.desc";
	}
}
