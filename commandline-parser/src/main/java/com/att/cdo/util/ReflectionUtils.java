package com.att.cdo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

	public static Map<String, EncapsulationPair> getBeanFieldMap(Object anObject) {
		Field[] fields = anObject.getClass().getDeclaredFields();
		Method[] methods = anObject.getClass().getDeclaredMethods();

		// Sort and match
		Map<String, EncapsulationPair> pairHash = new HashMap<String, EncapsulationPair>((int) (methods.length * 1.5));
		for (Method method : methods) {
			if (isGetter(method)) {
				String fieldNameKey = obtainFieldName(method.getName()) + "-" + method.getReturnType().getName();
				EncapsulationPair pair = null;
				if (pairHash.containsKey(fieldNameKey)) {
					pair = pairHash.get(fieldNameKey);
				} else {
					pair = new EncapsulationPair();
				}
				pair.setGetter(method);
				pairHash.put(fieldNameKey, pair);
			} else if (isSetter(method)) {
				String fieldNameKey = obtainFieldName(method.getName()) + "-" + method.getParameterTypes()[0].getName();
				EncapsulationPair pair = null;
				if (pairHash.containsKey(fieldNameKey)) {
					pair = pairHash.get(fieldNameKey);
				} else {
					pair = new EncapsulationPair();
				}
				pair.setSetter(method);
				pairHash.put(fieldNameKey, pair);
			}
		}

		Map<String, EncapsulationPair> fieldMap = new HashMap<String, EncapsulationPair>();
		for (Field field : fields) {
			String fieldType = field.getName() + "-" + field.getType().getCanonicalName();
			if (pairHash.containsKey(fieldType)) {
				fieldMap.put(field.getName(), pairHash.get(fieldType));
			}
		}

		return fieldMap;

	}

	private static String obtainFieldName(String name) {
		String fieldName;
		if (name.startsWith("get") || name.startsWith("set")) {
			fieldName = name.substring(3);
		} else {
			fieldName = name.substring(3);
		}
		return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
	}

	public static boolean isGetter(Method method) {
		if ((!method.getName().startsWith("get")) && (!method.getName().startsWith("is")))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		return true;
	}

	public static class EncapsulationPair {

		private Method getter;
		private Method setter;

		public Method getGetter() {
			return getter;
		}

		public void setGetter(Method getter) {
			this.getter = getter;
		}

		public Method getSetter() {
			return setter;
		}

		public void setSetter(Method setter) {
			this.setter = setter;
		}
	}

}
