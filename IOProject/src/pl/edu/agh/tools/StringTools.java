package pl.edu.agh.tools;

public class StringTools {

	public static boolean isNotNullOrEmpty(String str) {
		return str != null && !str.isEmpty(); 
	}
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty(); 
	}
}
