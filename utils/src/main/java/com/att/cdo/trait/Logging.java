package com.att.cdo.trait;

import java.util.logging.Logger;

/**
 * Logging - implements a java trait that allows the user to
 * add a logger to any object by putting implements logging
 * in the class header.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 *
 */
public interface Logging {
	
	/**
	 * getLogger - returns a java logger for the class this trait
	 * is attached to.
	 * @return a logger.
	 */
	default Logger getLogger() {
		Logger logger = Logger.getLogger(this.getClass().getName());
		return logger;
	}

}
