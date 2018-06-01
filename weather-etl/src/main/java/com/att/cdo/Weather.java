package com.att.cdo;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.att.cdo.converter.InvalidConfigurationException;
import com.att.cdo.input.Configuration;
import com.att.cdo.parser.IlleagalArgumentException;
import com.att.cdo.parser.InvalidPojoSetterConfiguration;
import com.att.cdo.parser.Parser;
import com.att.cdo.service.ETLService;
import com.att.cdo.service.FTPService;
import com.att.cdo.service.JoinService;
import com.att.cdo.trait.Logging;

/**
 * Weather - is the entry point to all functionality in the
 * weather application.
 */
public class Weather {
	
	private static FTPService ftpService = new FTPService();
	private static ETLService etlService = new ETLService();
	private static JoinService joinService = new JoinService();
	
	private static Logger logger = Logger.getLogger(Weather.class.getName());

	public static void main(String[] args) {
		
		getLogger().info("Configuring weather application components...");
		
		Configuration configuration = new Configuration();
		try {
			Parser.parse(args, configuration);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | IlleagalArgumentException
				| InvalidPojoSetterConfiguration | InvalidConfigurationException e) {
			getLogger().log(Level.SEVERE,"An error occured. Please check your arguments.\n",e);

		}	
		executeWeatherWorkflow(configuration);
		
	}

	public static void executeWeatherWorkflow(Configuration configuration) {
		
		if(configuration.getWeatherPullConfiguration() != null) {
			ftpService.pull(configuration.getWeatherPullConfiguration());
		}
		
		if(configuration.getEtlConfiguration() != null) {
			etlService.process(configuration.getEtlConfiguration());
		}
		
		if(configuration.getTableJoinConfiguration() != null) {
			joinService.process(configuration.getTableJoinConfiguration());
		}
		
		getLogger().info("Your run completed successfully.");
	}

	private static Logger getLogger() {
		return logger;
	}
}
