package nanobot.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import nanobot.core.interfaces.INanoGeneral;

/**
 * This class contains several useful introspection functions.
 * @modelguid {CF86E32C-9133-49C9-A41A-577B5BF3E261}
 */
public class NanoIntrospectionUtilities {

	/**
	 * Get the return type of a Method, based upon the method name.
	 * @param object The object for which the method is requested
	 * @param methodName The name of the requested method
	 * @return Method object referencing the requested method
	 * @modelguid {D8D4D257-7717-45DC-83A6-15F1AB80E280}
	 */
	public static Class getMethodReturnType(Class objectClass, String methodName) {
		try {
			return ((Method) objectClass.getMethod(methodName, new Class[] {})).getReturnType();
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Derive the class name from a fully qualified classname.
	 * E.g. "com.thalesgroup.nl.mpc.FrontController" becomes "FrontController".
	 * @param fullyQualifiedClassName The fully qualified classname
	 * @return The class name
	 * @modelguid {52EEFAB8-2FF0-477C-87D8-60C13B1396CC} 
	 */
	public static String unQualifyClassName(String fullyQualifiedClassName) {
		return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf(".") + 1);
	}
	
	/**
	 * Derive the class name from a Class object.
	 * E.g. FrontController.class becomes "FrontController".
	 * @param fullyQualifiedClassName The fully qualified classname
	 * @return The class name
	 * @modelguid {52EEFAB8-2FF0-477C-87D8-60C13B1396CC} 
	 */
	public static String unQualifyClassName(Class c) {
		return unQualifyClassName(c.getName());
	}

	/**
	 * Coverts a string to any of the supported classes. It will always return an object type, never a primitive type.
	 * @return Instance of the requested class representing the supplied value or null if the class is not supported
	 * or the value could not be converted to the requested class.
	 * @modelguid {D471E52D-A9BA-4041-B31B-653366CCE801}
	 */
	private static Object convertStringToClass(Class toClass, String value) {
		if (toClass.equals(String.class)) {
			return value;
		} else if (toClass.equals(long.class) || toClass.equals(Long.class)) {
			return new Long(Long.parseLong(value));
		} else if (toClass.equals(double.class) || toClass.equals(Double.class)) {
			return new Double(Double.parseDouble(value));
		} else if (toClass.equals(float.class) || toClass.equals(Float.class)) {
			return new Float(Float.parseFloat(value));
		} else if (toClass.equals(int.class) || toClass.equals(Integer.class)) {
			return new Integer(Integer.parseInt(value));
		} else if (toClass.equals(boolean.class) || toClass.equals(Boolean.class)) {
			return new Boolean(value);
		} else if (toClass.equals(byte.class) || toClass.equals(Byte.class)) {
			return new Byte(Byte.parseByte(value));
		} else if (toClass.equals(short.class) || toClass.equals(Short.class)) {
			return new Short(Short.parseShort(value));
        } else if (toClass.equals(Object.class)) {
            return value;
		} else {
			System.err.println("Conversion from string to class " + toClass.getName() + " not supported for value: '" + value + "'");
		}
		return null;
	}

	/**
	 * Set the property of given object to the specified value.
	 * The propertyName must be the exact name of the field as defined in the Class
	 * except for the first character which may be uppercase i.s.o. lowercase.
	 * @param object The object containing the property
	 * @param propertyName The name of the property
	 * @param value The value of the property
	 * @modelguid {BDD9EC76-77B8-4C90-894D-4F3135876376}
	 */
	public static void setProperty(Object object, String propertyName, Object value) {
		//Logger.info("SetProperty: propertyName:" + propertyName + " value:" + value + " object:" + object);
		if(propertyName != null && propertyName.length() > 0) {
			try {
				Class propertyClass;
				Object targetObject;
				if (value instanceof String) {
					propertyClass = getPropertyType(object.getClass(), propertyName);
					targetObject = convertStringToClass(propertyClass, (String) value);
				} else if (value instanceof Boolean) {
					propertyClass = boolean.class;
					targetObject = value;
				} else {
					propertyClass = value == null ? getPropertyType(object.getClass(), propertyName) : value.getClass();
					targetObject = value;
				}
				if(Character.isLowerCase(propertyName.charAt(0))) {
					propertyName = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
				}
				invokeMethodByName("set" + propertyName, object, new Class[] {propertyClass}, new Object[] {targetObject});
			} catch (NoSuchMethodException e) {
				System.out.println("NoSuchMethodException: " + e.getMessage());
			}
		}
	}

	/**
	 * Get the property of given object to the specified value.
	 * The propertyName must be the exact name of the field as defined in the Class
	 * @param object The object containing the property
	 * @param propertyName The name of the property (must be getter)
	 * @return The value of the property
	 * @modelguid {FF4D6060-26FF-4B4A-A5D6-B599A59D1540}
	 */
	public static Object getProperty(Object object, String propertyName) {
		System.out.println("GetProperty: propertyName: " + propertyName);
		Object value = null;
		try {
			PropertyDescriptor desc = getPropertyDescriptor(object.getClass(), propertyName);
			if (desc != null) {
				Method getter = desc.getReadMethod();
				if (getter != null) {
					value = getter.invoke(object, new Object[] {});
				}
			}
			// Not correct because of the capitalisation of the name
			//value = object.getClass().getMethod("get" + propertyName, new Class[] {}).invoke(object, new Object[] {});
		} catch (InvocationTargetException e) {
			System.err.println("InvocationTargetException: " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("IllegalAccessException: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("IllegalArgumentException: " + e.getMessage());
		}
		return value;
	}

	/**
	 * Call a method without parameters but with return type
	 * @param target The object on which to invoke the method
	 * @param methodName The name of the method to invoke
	 * @return result of invocation
	 * @modelguid {F08BB6AD-3FD0-4BE0-BE5E-E4A4ACF71C17}
	 */
	public static Object invokeMethodByName(String methodName, Object target) throws NoSuchMethodException {
		return invokeMethodByName(methodName, target, new Class[] {}, new Object[] {});
	}
	
	/**
	 * Call a method with parameters but with return type
	 * @param target The object on which to invoke the method
	 * @param methodName The name of the method to invoke
	 * @return result of invocation
	 * @modelguid {F08BB6AD-3FD0-4BE0-BE5E-E4A4ACF71C17}
	 */
	public static Object invokeMethodByName(String methodName, Object target, Class[] parameterClasses, Object[] parameters) throws NoSuchMethodException {
		try {
			Method method = target.getClass().getMethod(methodName, parameterClasses);
			return invokeMethod(method, target, parameters);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Call a method without parameters but with return type
	 * @param target The object on which to invoke the method
	 * @param methodName The name of the method to invoke
	 * @return result of invocation
	 * @modelguid {F08BB6AD-3FD0-4BE0-BE5E-E4A4ACF71C17}
	 */
	public static Object invokeStaticMethodByName(String methodName, Class target) throws NoSuchMethodException {
		return invokeStaticMethodByName(methodName, target, new Class[] {}, new Object[] {});
	}
	
	/**
	 * Call a method with parameters but with return type
	 * @param target The object on which to invoke the method
	 * @param methodName The name of the method to invoke
	 * @return result of invocation
	 * @modelguid {F08BB6AD-3FD0-4BE0-BE5E-E4A4ACF71C17}
	 */
	public static Object invokeStaticMethodByName(String methodName, Class target, Class[] parameterClasses, Object[] parameters) throws NoSuchMethodException {
		try {
			Method method = target.getMethod(methodName, parameterClasses);
			return invokeMethod(method, target, parameters);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Call a method without paramters but with return type
	 * @param target The object on which to invoke the method
	 * @param method The method to invoke
	 * @return result of invocation
	 * @throws PersistencyException
	 * @modelguid {DE736CE9-CED4-48F8-93D1-6C73D5E31F21}
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, new Object[] {});
	}
	
	/**
	 * Call a method with paramters but with return type
	 * @param target The object on which to invoke the method
	 * @param method The method to invoke
	 * @param parameters The parameters to use when invoking the method
	 * @return result of invocation
	 * @throws PersistencyException
	 * @modelguid {DE736CE9-CED4-48F8-93D1-6C73D5E31F21}
	 */
	public static Object invokeMethod(Method method, Object target, Object[] parameters) {
		Object retVal = null;
		try {
			retVal=method.invoke(target, parameters);
		} 
		catch (IllegalAccessException e) {
			System.err.println(NanoIntrospectionUtilities.unQualifyClassName(e.getClass().getName()) + ": " + e.getMessage());
			System.err.println("Error invoking " + method + " in " + unQualifyClassName(target.getClass()));
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			System.err.println(NanoIntrospectionUtilities.unQualifyClassName(e.getClass().getName()) + ": " + e.getMessage());
			System.err.println("Error invoking " + method + " in " + unQualifyClassName(target.getClass()));
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			System.err.println(NanoIntrospectionUtilities.unQualifyClassName(e.getClass().getName()) + ": " + e.getMessage());
			System.err.println("Error invoking " + method + " in " + unQualifyClassName(target.getClass()));
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * Get the Property names from a bean (class). Only properties with a write method, that are
	 * declared in this class (not inherited).
	 * @param objectClass the Bean to introspect
	 * @return An array of Strings: the names of the properties.
	 * @modelguid {1BE2D842-86BE-43E3-955C-CA56E571427C}
	 */
	public static Set getPropertiesFromBean(Class objectClass) {
		Set<String> result = new HashSet<String>();
		try {
			BeanInfo info = Introspector.getBeanInfo(objectClass);
			if (info != null) {
				PropertyDescriptor[] props = info.getPropertyDescriptors();
				if (props != null) {
					for (int i = 0; i < props.length; i++) {
						if (props[i].getWriteMethod() != null) {
							String propName = props[i].getName();
							if (props[i].getReadMethod() != null) {
								result.add(propName);
							}
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Check wether the given class or any of its superclasses defines the given property
	 * It also checks properties defined by superclasses
	 */
	static public boolean containsProperty(String properyName, Class objectClass) {
		while(objectClass != null && !objectClass.equals(Object.class)) {
			try {
				objectClass.getDeclaredField(properyName);
				return true;
			}
			catch (NoSuchFieldException e) {
				objectClass = objectClass.getSuperclass();	
			}
		}
		return false;
	}

	/**
	 * Check wether the given class or any of its superclasses defines the given method
	 */
	static public boolean containsMethod(String methodName, Class objectClass) {
        boolean retVal = false;
        
			//objectClass.getMethod(methodName, null);
        Method[] methods = objectClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = (Method) methods[i];
            if ( m.getName().equalsIgnoreCase( methodName ) ) {
                retVal = true;
                break;
            }
        }
            
		return retVal;
	}
	/**
	 * Returns an ArrayList of all the public Methods
	 */
	static public ArrayList<String> getPublicMethods(Class objectClass) {
        ArrayList<String> retVal = new ArrayList<String>();
        
        Method[] methods = objectClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = (Method) methods[i];
            if ( m.getModifiers() == Modifier.PUBLIC ) {
            	retVal.add(m.getName());
            }
        }
            
		return retVal;
	}
	/**
	 * Return the type (class) of a property in a class. If available etc.
	 * @modelguid {E281035C-634A-417E-A936-DE00EFB2614B}
	 */
	public static Class getPropertyType(Class objectClass, String property) {
		PropertyDescriptor desc = getPropertyDescriptor(objectClass, property);
		if (desc != null) {
			return desc.getPropertyType();
		} 
		else {
			return null;
		}
	}
	
	/**
	 * Return the methodname (string) of a property in a class. If available etc.
	 */
	public static String getPropertyReadMethodName(Class objectClass, String property) {
		PropertyDescriptor desc = getPropertyDescriptor(objectClass, property);
		if (desc != null && desc.getReadMethod()!=null) {
			return desc.getReadMethod().getName();
		} 
		else {
			return null;
		}
	}
	
	/**
	 * Return the propertydescriptor for a class and a property name
	 * @modelguid {95ECD7D2-0737-4AB0-A0B3-322A0833DA47}
	 */
	private static PropertyDescriptor getPropertyDescriptor(Class objectClass, String property) {
		PropertyDescriptor result = null;
		try {
			PropertyDescriptor[] properties = Introspector.getBeanInfo(objectClass).getPropertyDescriptors();
			if (properties != null) {
				for (int i = 0; i < properties.length; i++) {
					if (properties[i].getName().equalsIgnoreCase(property)) {
						result = properties[i];
					}
				}
			}
		} 
		catch (IntrospectionException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Check if the class is a primitive, a wrapper for a primitive, a string or an enumeration.
	 * @modelguid {452CB300-446A-483C-A560-3A8B71BBAA9E}
	 */
	public static boolean isPrimitiveObject(Class objectClass) {
		boolean result = false;
		if (objectClass.isPrimitive()) {
			result = true;
		} else if (String.class.isAssignableFrom(objectClass)) {
			result = true;
		} else if (Boolean.class.isAssignableFrom(objectClass)) {
			result = true;
		} else if (Number.class.isAssignableFrom(objectClass)) {
			result = true;
		}
		return result;
	}
}
