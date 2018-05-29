package com.att.cdo.parser;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Hints - are a annotations used on setters of pojos in the 
 * parser to indicate the the type of a field with a generic parameterization.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GenericType {
	
	Class<?> genericType()  default Object.class;

}
