package com.att.cdo.weather.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.att.cdo.trait.Logging;
import com.att.cdo.weather.domain.WeatherStation;
import com.att.cdo.weather.input.Configuration;
import com.att.cdo.weather.quadtree.NeighborDistance;
import com.att.cdo.weather.quadtree.QuadTree;
import com.att.cdo.weather.quadtree.SpaceTimeViolation;

/**
 * GraphGenerationService - builds a graphMl file from the data in the station temperature information.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 */
public class GraphGenerationService implements Logging {
	
	
	private static final int LAT = 6, LONG = 7, STATION_NAME = 2, TEMPRETURE = 8, USAF = 0, WBAN = 1;

	public void createGraph(Configuration configuration) throws IOException {
		
		getLogger().info("Reading station info from " + configuration.getInputStationDataFile() + ".");
		List<String> stationLines = Files.readAllLines(configuration.getInputStationDataFile().toPath());
		List<WeatherStation> stations = getStations(stationLines);
		
		getLogger().info("Opening " + configuration.getOutputGraphMlFile() + " for writing.");
		BufferedWriter graphWriter = new BufferedWriter(new FileWriter(configuration.getOutputGraphMlFile()));
		graphWriter.write(GraphMl.getFileHeader());
		
		getLogger().info("Writing vertex node information.");
		int id = 0;
		QuadTree quadTree = new QuadTree();
		List<WeatherStation> duplicateLocations = new LinkedList<WeatherStation>();
		for(WeatherStation station : stations) {
			station.setGraphId(id);
			id++;
			try {
				quadTree.addPoint(station);
				graphWriter.write(GraphMl.getNodeFor(station));
				graphWriter.flush();
			} catch (SpaceTimeViolation e) {
				id--;
				duplicateLocations.add(station);
			}
		}
		stations.removeAll(duplicateLocations);
		
		getLogger().info("Writing edge information.");
		for(WeatherStation station : stations) {
			List<NeighborDistance> neighbors = quadTree.findNearestNeighbors(station, 10, false);
			for(NeighborDistance edge : neighbors) {
				graphWriter.write(GraphMl.getEdgeFor(edge, id));
				graphWriter.flush();
				id++;
			}
		}
		
		getLogger().info("Closing graph definition.");
		graphWriter.write(GraphMl.getFileClose());
		graphWriter.flush();
		graphWriter.close();

	}

	private List<WeatherStation> getStations(List<String> stationLines) {
		String header = null;
		List<WeatherStation> stations = new ArrayList<WeatherStation>();
		
		for (String stationInfo : stationLines) {
			if(header == null) {
				header = stationInfo;
			} else {
				stations.add(parseStation(stationInfo));
			}
			
		}
		return stations;
	}

	
	/**
	 * parseStation - populates the data from the station info lines.
	 * The problem with the data sources has lead me to use the elevation
	 * value as a proof of concept on the temperature data.
	 * @param stationInfo is a pipe delimited string containing station info 
	 * with fields at the positions specified in the constants of the GraphGeneration
	 * service.
	 * @return a weather station.
	 */
	private WeatherStation parseStation(String stationInfo) {
		String[] record = stationInfo.split(Pattern.quote("|"));
		WeatherStation station = new WeatherStation();
		station.setLatitude(cleanDouble(record[LAT]));
		station.setLongitude(cleanDouble(record[LONG]));
		station.setStationName(record[STATION_NAME]);
		station.setTempreture(cleanDouble(record[TEMPRETURE]));
		station.setUsaf(record[USAF]);
		station.setWban(record[WBAN]);
		return station;
	}

	private Double cleanDouble(String value) {
		value = value.replaceAll("\\s", "");
		if(value.isEmpty()) {
			return new Double(0.0);
		} else {
			try {
				return new Double(value);
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "The input value " + value + " caused the following:\n", e);
				throw e;
			}
		}
	}

}
