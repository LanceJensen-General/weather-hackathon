package com.att.cdo.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.att.cdo.converter.ConverterRegestry;
import com.att.cdo.converter.InvalidConfigurationException;
import com.att.cdo.util.ReflectionUtils;
import com.att.cdo.util.ReflectionUtils.EncapsulationPair;

/**
 * Parser - is a class that utilizes reflection to automatically parse command
 * line arguments to fields in a pojo. A leading dash indicates an argument and
 * repeated - indicate sub arguments.
 */
public class Parser {

	private static ConverterRegestry converterRegestry = new ConverterRegestry();

	/**
	 * parse - is processes the args values to marshall the data into a pojo.
	 * 
	 * @param args
	 *            - is an array of string arguments to process.
	 * @param pojo
	 *            - is the pojo object to marshal the arguments into.
	 * @return the updated pojo.
	 * @throws IlleagalArgumentException
	 * 			@See ArgumentGroup.groupArguments()
	 * @throws InvalidPojoSetterConfiguration
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvalidConfigurationException
	 */
	public static <T> T parse(String[] args, T pojo)
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		List<ArgumentGroup> argumentGroups = ArgumentGroup.groupArguments(args);
		for (ArgumentGroup group : argumentGroups) {
			populateArguments(group, pojo);
		}
		return pojo;
	}

	/**
	 * populateArguments - processes a grouping to set values on the incoming
	 * pojo
	 * 
	 * @param group
	 *            - is a group of values to set in the pojo of interest.
	 * @param pojo
	 *            - is an object that should be updated.
	 * @throws IllegalAccessException
	 *             - is thrown if the setter of the pojo is not public.
	 * @throws InstantiationException
	 *             - is thrown if there is not a no-arg constructor for the
	 *             object.
	 * @throws InvocationTargetException
	 *             - is thrown if there is a method accessor issue in setting
	 *             the values for the pojo.
	 * @throws InvalidPojoSetterConfiguration
	 *             - is thown if there is a misconfiguration by the library
	 *             user.
	 * @throws InvalidConfigurationException
	 *             - is thrown if a conversion from String is required but there
	 *             is no converter regestered and no single string constructor
	 *             for the class.
	 */
	private static void populateArguments(ArgumentGroup group, Object pojo)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		String targetFieldName = getFieldName(group.getFlag());
		Map<String, EncapsulationPair> fieldMap = ReflectionUtils.getBeanFieldMap(pojo);
		Method setter = fieldMap.get(targetFieldName).getSetter();

		if (!group.getSubFlags().isEmpty()) {
			Object subPojo = setter.getParameterTypes()[0].newInstance();
			for (ArgumentGroup subFlag : group.getSubFlags()) {
				populateArguments(subFlag, subPojo);
			}
			setter.invoke(pojo, subPojo);
		} else {
			if (group.getFlagValues().length == 1) {
				Class<?> targetConversionClass = setter.getParameterTypes()[0];
				Object fieldObject = converterRegestry.convert(group.getFlagValues()[0], targetConversionClass);
				setter.invoke(pojo, fieldObject);
			} else {
				Class<?> targetConversionClass = null;
				GenericType hint = setter.getAnnotation(GenericType.class);

				if (hint != null) {
					targetConversionClass = hint.genericType();
				}

				if (targetConversionClass == null) {
					throw new InvalidPojoSetterConfiguration(
							"Multi-input fields are required to be a list and must have a Hint annotation to resolve the generic type.");
				}
				List<Object> values = new LinkedList<>();
				for (String value : group.getFlagValues()) {
					values.add(converterRegestry.convert(value, targetConversionClass));
				}
				setter.invoke(pojo, values);
			}
		}

	}

	/**
	 * getFieldName - derives a pojo's field name by removing leading '-'
	 * characters.
	 * 
	 * @param flag
	 *            - is a argument flag corrisponding to be converted to a field
	 *            name
	 * @return a fieldName string.
	 */
	private static String getFieldName(String flag) {
		int flagDepth = ArgumentGroup.getFlagDepthFor(flag);
		return flag.substring(flagDepth);
	}

}
