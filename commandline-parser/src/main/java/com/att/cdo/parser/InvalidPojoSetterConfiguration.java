package com.att.cdo.parser;

/**
 * InvalidPojoSetterConfiguration - indicates to the developer that
 * they have insufficently configured their pojo for marshalling data.
 */
public class InvalidPojoSetterConfiguration extends Exception {

	public InvalidPojoSetterConfiguration(String message) {
		super(message);
	}
}
