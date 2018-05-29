package com.att.cdo.converter;

public interface Converter {

	public <T> T convert(String value, Class<T> targetClassType);

}
