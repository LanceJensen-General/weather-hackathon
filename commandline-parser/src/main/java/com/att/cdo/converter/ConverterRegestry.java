package com.att.cdo.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConverterRegestry {
	
	private static final Map<String,Converter> regestry;
	
	static {
		HashMap<String,Converter> mutableMap = new HashMap<String,Converter>();
		mutableMap.put(null, null);
		regestry = Collections.unmodifiableMap(mutableMap);
	}

	public <T extends Object> T convert(String value, Class<T> targetConversionClass) throws InvalidConfigurationException {
		if(regestry.containsKey(targetConversionClass.getName())) {
			return regestry.get(targetConversionClass.getName()).convert(value, targetConversionClass);
		} else {
			T response = null;
			try {
				Constructor<T> stringConstructor = targetConversionClass.getConstructor(String.class);
				response = stringConstructor.newInstance(value);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException  e) {
				throw new InvalidConfigurationException("Could not convert to " + targetConversionClass + " because no converter exists.");
			}
			return response;
		}
	}

}
