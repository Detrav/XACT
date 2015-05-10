package xk.xact.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import xk.xact.util.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Keybinds {
	public static KeyBinding clear = new KeyBinding(References.Keys.KEY_CLEAR, Keyboard.KEY_X, References.Keys.KEY_CATEGORY);
	public static KeyBinding load = new KeyBinding(References.Keys.KEY_LOAD, Keyboard.KEY_L, References.Keys.KEY_CATEGORY);
	public static KeyBinding prev = new KeyBinding(References.Keys.KEY_PREV, Keyboard.KEY_LEFT, References.Keys.KEY_CATEGORY);
	public static KeyBinding next = new KeyBinding(References.Keys.KEY_NEXT, Keyboard.KEY_RIGHT, References.Keys.KEY_CATEGORY);
	public static KeyBinding delete = new KeyBinding(References.Keys.KEY_DELETE, Keyboard.KEY_DELETE, References.Keys.KEY_CATEGORY);
//	public static KeyBinding reveal = new KeyBinding(References.Keys.KEY_REVEAL, Keyboard.KEY_LSHIFT, References.Keys.KEY_CATEGORY);
	public static KeyBinding openGrid = new KeyBinding(References.Keys.KEY_OPENGRID, Keyboard.KEY_Z, References.Keys.KEY_CATEGORY);
}
