package xk.xact.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Random collection of stuff that
 * I don't understand
 */
public class NumberHelper {
	
	/**
	 * Will round a double to a given length
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Will turn red, green, blue into one integer
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int rgbToInt(int red, int green, int blue) {
		return ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
	}
	
    public static int argb(int A, int R, int G, int B){     
        byte[] colorByteArr = { (byte) A, (byte) R, (byte) G, (byte) B };
        return byteArrToInt(colorByteArr);
    }

    public static int byteArrToInt(byte[] colorByteArr) {
        return (colorByteArr[0] << 24) + ((colorByteArr[1] & 0xFF) << 16) + ((colorByteArr[2] & 0xFF) << 8) + (colorByteArr[3] & 0xFF);
    }
    
	
	public static boolean isMouseInBound(int mouseX, int mouseY, int left, int right, int top, int bottom) {
		if (mouseX > left && mouseX < right)
			if (mouseY > top && mouseY < bottom)
				return true;
		return false;
	}
}
