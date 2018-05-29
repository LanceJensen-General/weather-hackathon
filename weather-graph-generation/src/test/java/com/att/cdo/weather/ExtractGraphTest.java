package com.att.cdo.weather;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.att.cdo.trait.Logging;
import com.att.cdo.weather.input.Configuration;

public class ExtractGraphTest implements Logging{

	@Test
	public void test() throws IOException {
		
		// Given
		File inputFile = new File("target/test-classes/usStateStationsWithFileNames.txt");
		File outputFile = new File("weather_stations.graphml");
		Configuration configuration = new Configuration();
		configuration.setInputStationDataFile(inputFile);
		configuration.setOutputGraphMlFile(outputFile);
		
		// When
		ExtractGraph.process(configuration);
		
		// Then
		assertTrue(outputFile.exists());
	}

}
