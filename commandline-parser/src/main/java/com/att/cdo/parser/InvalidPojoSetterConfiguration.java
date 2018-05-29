package com.att.cdo.parser;

/**
 * InvalidPojoSetterConfiguration - indicates to the developer that
 * they have insufficently configured their pojo for marshalling data.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 */
public class InvalidPojoSetterConfiguration extends Exception {

	public InvalidPojoSetterConfiguration(String message) {
		super(message);
	}
}
