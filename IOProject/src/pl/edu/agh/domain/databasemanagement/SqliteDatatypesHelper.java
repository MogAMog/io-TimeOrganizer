package pl.edu.agh.domain.databasemanagement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SqliteDatatypesHelper {
	
	private static final List<Class<?>> INTEGER_CLASSES;
	private static final List<Class<?>> NUMERIC_CLASSES;
	private static final List<Class<?>> TEXT_CLASSES;
	private static final List<Class<?>> REAL_CLASSES;
	private static final List<Class<?>> NONE_CLASSES;
	
//	private static final List<String> INTEGER_CLASSES_NAMES;
//	private static final List<String> NUMERIC_CLASSES_NAMES;
//	private static final List<String> TEXT_CLASSES_NAMES;
//	private static final List<String> REAL_CLASSES_NAMES;
//	private static final List<String> NONE_CLASSES_NAMES;
	
	private static final String TEXT_TYPE = "TEXT";
	private static final String NUMERIC_TYPE = "NUMERIC";
	private static final String REAL_TYPE = "REAL";
	private static final String INTEGER_TYPE = "INTEGER";
	private static final String NONE_TYPE = "NONE";
	
	static {
		INTEGER_CLASSES = new ArrayList<Class<?>>();
		INTEGER_CLASSES.add(Byte.class);
		INTEGER_CLASSES.add(Short.class);
		INTEGER_CLASSES.add(Integer.class);
		INTEGER_CLASSES.add(Long.class);
		INTEGER_CLASSES.add(BigInteger.class);
		
		NUMERIC_CLASSES = new ArrayList<Class<?>>();
		NUMERIC_CLASSES.add(Double.class);
		NUMERIC_CLASSES.add(BigDecimal.class);
		NUMERIC_CLASSES.add(Boolean.class);
		
		TEXT_CLASSES = new ArrayList<Class<?>>();
		TEXT_CLASSES.add(Character.class);
		TEXT_CLASSES.add(String.class);
		TEXT_CLASSES.add(java.util.Date.class);
		TEXT_CLASSES.add(java.sql.Date.class);
		TEXT_CLASSES.add(Timestamp.class);
		TEXT_CLASSES.add(Calendar.class);
		TEXT_CLASSES.add(Clob.class);
		
		REAL_CLASSES = new ArrayList<Class<?>>();
		REAL_CLASSES.add(Float.class);
		
		NONE_CLASSES = new ArrayList<Class<?>>();
		NONE_CLASSES.add(Blob.class);
		NONE_CLASSES.add(Serializable.class);

//		INTEGER_CLASSES_NAMES = new ArrayList<String>();
//		INTEGER_CLASSES_NAMES.add("Byte");
//		INTEGER_CLASSES_NAMES.add("Short");
//		INTEGER_CLASSES_NAMES.add("Integer");
//		INTEGER_CLASSES_NAMES.add("Long");
//		INTEGER_CLASSES_NAMES.add("byte");
//		INTEGER_CLASSES_NAMES.add("short");
//		INTEGER_CLASSES_NAMES.add("int");
//		INTEGER_CLASSES_NAMES.add("long");
//		INTEGER_CLASSES_NAMES.add("BigInteger");
//		
//		NUMERIC_CLASSES_NAMES = new ArrayList<String>();
//		NUMERIC_CLASSES_NAMES.add(Double.class);
//		NUMERIC_CLASSES_NAMES.add(BigDecimal.class);
//		NUMERIC_CLASSES_NAMES.add(Boolean.class);
//		
//		TEXT_CLASSES_NAMES = new ArrayList<String>();
//		TEXT_CLASSES_NAMES.add("Character");
//		TEXT_CLASSES_NAMES.add("String");
//		TEXT_CLASSES_NAMES.add("Date");
//		TEXT_CLASSES_NAMES.add("Timestamp");
//		TEXT_CLASSES_NAMES.add("Calendar");
//		TEXT_CLASSES_NAMES.add("Clob");
//		TEXT_CLASSES_NAMES.add();
//		
//		REAL_CLASSES_NAMES = new ArrayList<String>();
//		REAL_CLASSES_NAMES.add(Float.class);
//		
//		NONE_CLASSES_NAMES = new ArrayList<String>();
//		NONE_CLASSES_NAMES.add(Blob.class);
//		NONE_CLASSES_NAMES.add(Serializable.class);
	}
	
	private static boolean checkNumericTypes(Object object) {
		return NUMERIC_CLASSES.contains(object.getClass());
	}
	
	private static boolean checkIntegerTypes(Object object) {
		return INTEGER_CLASSES.contains(object.getClass());
	}
	
	private static boolean checkTextTypes(Object object) {
		return TEXT_CLASSES.contains(object.getClass());
	}
	
	private static boolean checkNoneTypes(Object object) {
		return NONE_CLASSES.contains(object.getClass());
	}
	
	private static boolean checkRealTypes(Object object) {
		return REAL_CLASSES.contains(object.getClass());
	}
	
	private static boolean checkNumericTypes(Class<?> classObject) {
		return NUMERIC_CLASSES.contains(classObject);
	}
	
	private static boolean checkIntegerTypes(Class<?> classObject) {
		return INTEGER_CLASSES.contains(classObject);
	}
	
	private static boolean checkTextTypes(Class<?> classObject) {
		return TEXT_CLASSES.contains(classObject);
	}
	
	private static boolean checkNoneTypes(Class<?> classObject) {
		return NONE_CLASSES.contains(classObject);
	}
	
	private static boolean checkRealTypes(Class<?> classObject) {
		return REAL_CLASSES.contains(classObject);
	}
	
	public static String getSqliteDatabaseType(Object object) throws IllegalArgumentException {
		if(checkTextTypes(object)) {
			return TEXT_TYPE;
		} else if(checkIntegerTypes(object)) {
			return INTEGER_TYPE;
		} if(checkNumericTypes(object)) {
			return NUMERIC_TYPE;
		} else if(checkNoneTypes(object)) {
			return NONE_TYPE;
		} else if(checkRealTypes(object)) {
			return REAL_TYPE;
		} 
		throw new IllegalArgumentException("Class cannot be mapped to SQLite Data Type.");
	}

	public static String getSqliteDatabaseType(Class<?> valueClass) {
		if(checkTextTypes(valueClass)) {
			return TEXT_TYPE;
		} else if(checkIntegerTypes(valueClass)) {
			return INTEGER_TYPE;
		} if(checkNumericTypes(valueClass)) {
			return NUMERIC_TYPE;
		} else if(checkNoneTypes(valueClass)) {
			return NONE_TYPE;
		} else if(checkRealTypes(valueClass)) {
			return REAL_TYPE;
		} 
		throw new IllegalArgumentException("Class cannot be mapped to SQLite Data Type.");
	}
	
	public static String getSqliteDatabaseType(byte value) {
		return INTEGER_TYPE;
	}
	
	public static String getSqliteDatabaseType(short value) {
		return INTEGER_TYPE;
	}
	
	public static String getSqliteDatabaseType(int value) {
		return INTEGER_TYPE;
	}
	
	public static String getSqliteDatabaseType(long value) {
		return INTEGER_TYPE;
	}
	
	public static String getSqliteDatabaseType(boolean value) {
		return NUMERIC_TYPE;
	}
	
	public static String getSqliteDatabaseType(float value) {
		return REAL_TYPE;
	}
	
	public static String getSqliteDatabaseType(double value) {
		return NUMERIC_TYPE;
	}
	
	public static String getSqliteDatabaseType(char value) {
		return TEXT_TYPE;
	}

	public static String parseDateTime(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
		return simpleDateFormat.format(date);
	}
}
