package nanobot.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nanobot.core.utils.Log;

public class ReflectionUtil {
	
	@SuppressWarnings("unchecked")
	public static Object getClassInstance(String fullyQualifiedClass) {
		Object o = null;
		try {
			Class c = Class.forName( fullyQualifiedClass );
			o = c.newInstance();
		} catch (ClassNotFoundException e) {
			Log.error(e);
		} catch (InstantiationException e) {
			Log.error(e);
		} catch (IllegalAccessException e) {
			Log.error(e);
		}
		return o;
	}
	
	public static List<String> getVariables( Object o ) {
		List<String> l = new ArrayList<String>();
		Field[] fields = o.getClass().getDeclaredFields();
		for ( Field field: fields ) {
			l.add( field.getName() );
		}
		return l;
	}
	
	public static Object getValue( Object o, String field ) {
		Object retVal = null;
		Method method = getGetMethod( o, field );
		if ( method != null ) {
			try {
				retVal = method.invoke(o , new Object[0]);
			} catch (IllegalArgumentException e) {
				Log.error(e);
			} catch (IllegalAccessException e) {
				Log.error(e);
			} catch (InvocationTargetException e) {
				Log.error(e);
			}
		}
		return retVal;
	}
	
	public static void setValue( Object o, String field, String value ) {
		Method setter = getSetMethod(o, field);
		if ( setter != null ) {
			try {
				setter.invoke(o, value);
			} catch (IllegalArgumentException e) {
				Log.error(e);
			} catch (IllegalAccessException e) {
				Log.error(e);
			} catch (InvocationTargetException e) {
				Log.error(e);
			}
		}
	}

	private static Method getGetMethod(Object o, String field) {
		Method method = null;
		String methodName = "get" + StringUtils.capitalize(field);
		for ( Method m : o.getClass().getDeclaredMethods() ) {
			if ( m.getName().equals( methodName) ) {
				method = m;
				break;
			}
		}
		
		return method;
	}
	
	private static Method getSetMethod(Object o, String field) {
		Method method = null;
		String methodName = "set" + StringUtils.capitalize(field);
		for ( Method m : o.getClass().getDeclaredMethods() ) {
			if ( m.getName().equals( methodName) ) {
				method = m;
				break;
			}
		}
		
		return method;
	}

}
