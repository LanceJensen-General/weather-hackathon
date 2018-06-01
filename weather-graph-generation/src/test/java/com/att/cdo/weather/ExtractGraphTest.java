package com.att.cdo.weather;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.att.cdo.trait.Logging;
import com.att.cdo.weather.input.Configuration;

public class ExtractGraphTest implements Logging{


	public void test() throws IOException {
		
		// Given
		File inputFile = new File("target/test-classes/StationTempMarch30-2017.txt");
		File outputFile = new File("weather_stations_temp.graphml");
		Configuration configuration = new Configuration();
		configuration.setInputStationDataFile(inputFile);
		configuration.setOutputGraphMlFile(outputFile);
		
		// When
		ExtractGraph.process(configuration);
		
		// Then
		assertTrue(outputFile.exists());
	}
	
	@Test
	public void shouldGenerateGraphML() throws IOException {
		//Given
		File inputFile = new File("target/test-classes/StationTempMarch30-2017.txt");
		File outputFile = new File("weather_stations_temp.graphml");
		String[] args = { "-inputStationDataFile", inputFile.getAbsolutePath(), 
						  "-outputGraphMlFile", outputFile.getAbsolutePath()
						  };
		
		// When
		ExtractGraph.main(args);
		// Then
		assertTrue(outputFile.exists());
	}

}
