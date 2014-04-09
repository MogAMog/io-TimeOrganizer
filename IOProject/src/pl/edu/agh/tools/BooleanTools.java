package pl.edu.agh.tools;

public class BooleanTools {

	private static final int TRUE = 1;
	private static final int FALSE = 0;
	
	public static int convertBooleanToInt(boolean isTrue) {
		return isTrue ? TRUE : FALSE;
	}
	
	public static boolean convertIntToBoolean(int value) {
		return value != FALSE ? true : false;
	}
	
}
