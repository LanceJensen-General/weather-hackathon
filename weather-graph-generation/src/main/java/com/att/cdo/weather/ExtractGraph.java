package com.att.cdo.weather;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.att.cdo.converter.InvalidConfigurationException;
import com.att.cdo.weather.input.Configuration;
import com.att.cdo.weather.service.GraphGenerationService;
import com.att.cdo.parser.IlleagalArgumentException;
import com.att.cdo.parser.InvalidPojoSetterConfiguration;
import com.att.cdo.parser.Parser;

public class ExtractGraph {

	private static GraphGenerationService graphService = new GraphGenerationService();
	
	private static Logger logger = Logger.getLogger(ExtractGraph.class.getName());

	public static void main(String[] args) throws IOException {
		
		getLogger().info("Configuring weather application components...");
		
		Configuration configuration = new Configuration();
		try {
			Parser.parse(args, configuration);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | IlleagalArgumentException
				| InvalidPojoSetterConfiguration | InvalidConfigurationException e) {
			getLogger().log(Level.ALL,"An error occured. Please check your arguments.\n",e);

		}	
		process(configuration);
		
	}

	static void process(Configuration configuration) throws IOException {
		getLogger().info("Starting graph encoding...");
		graphService.createGraph(configuration);
		getLogger().info("Your run completed successfully.");
	}

	private static Logger getLogger() {
		return logger;
	}
}
