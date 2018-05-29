package com.att.cdo.weather.service;

import java.io.BufferedWriter;

import com.att.cdo.weather.domain.WeatherStation;
import com.att.cdo.weather.quadtree.NeighborDistance;

public class GraphMl {

	private static final String XML_OPEN_STRING = "<?xml version='1.0' ?>\n"
			+ "<graphml xmlns='http://graphml.graphdrawing.org/xmlns'>\n"
			+ 	"\t<key id='usaf'    for='node' attr.name='usaf'    attr.type='string'></key>\n"
			+ 	"\t<key id='wban'    for='node' attr.name='wban'    attr.type='string'></key>\n"
			+ 	"\t<key id='stationName'    for='node' attr.name='stationName'    attr.type='string'></key>\n"
			+ 	"\t<key id='lat'     for='node' attr.name='lat'     attr.type='double'></key>\n"
			+ 	"\t<key id='lon'     for='node' attr.name='lon'     attr.type='double'></key>\n"
			+ 	"\t<key id='tempreture'     for='node' attr.name='tempreture'     attr.type='double'></key>\n"
			+ 	"\t<key id='radiansX'     for='node' attr.name='radiansX'     attr.type='double'></key>\n"
			+ 	"\t<key id='radiansY'     for='node' attr.name='radiansY'     attr.type='double'></key>\n"
			+	"\t<key id='labelE'  for='edge' attr.name='labelE'  attr.type='string'></key>\n"
			+ 	"\t<key id='distance'    for='edge' attr.name='distance'    attr.type='double'></key>\n"
			+ 	"\t<key id='tempGradient'    for='edge' attr.name='tempGradient'    attr.type='double'></key>\n"
			+ 	"\t<graph id='routes' edgedefault='directed'>\n"
			+ "";
	
	private static final String XML_CLOSE_STRING = "</graph>\n"
			+ "</graphml>";
	
	public static String getFileHeader() {	
		return XML_OPEN_STRING;
	}
	
	public static String getNodeFor(WeatherStation station) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("\t<node id='").append(station.getGraphId()).append("'>\n");
		buffer.append(getData("usaf",station.getUsaf()));
		buffer.append(getData("wban",station.getWban()));
		buffer.append(getData("stationName",station.getStationName()));
		buffer.append(getData("lat",station.getLatitude().toString()));
		buffer.append(getData("lon",station.getLongitude().toString()));
		buffer.append(getData("tempreture",station.getTempreture().toString()));
		buffer.append(getData("radiansX",station.getX().toString()));
		buffer.append(getData("radiansY",station.getY().toString()));
		buffer.append("\t</node>\n");
		return buffer.toString();
	}

	private static String getData(String key, String value) {
		return "\t\t<data key='" + key + "'>" + clean(value) + "</data>\n";
	}

	private static String clean(String value) {
		return value.replaceAll("&", " ");
	}

	public static String getEdgeFor(NeighborDistance edge, int id) {
		StringBuilder buffer = new StringBuilder();
		WeatherStation source = (WeatherStation) edge.getPointOfInterest();
		WeatherStation target = (WeatherStation) edge.getNeighbor();
		buffer.append("\t<edge id='").append(id).append("' source='")
			.append(source.getGraphId()).append("' target='")
			.append(target.getGraphId()).append("'>\n");
		buffer.append(getData("labelE","path"));
		buffer.append(getData("distance",edge.getDistance().toString()));
		Double tempGradient = (target.getTempreture()-source.getTempreture())/edge.getDistance();
		buffer.append(getData("tempGradient",tempGradient.toString()));
		buffer.append("\t</edge>\n");
		return buffer.toString();
	}

	public static String getFileClose() {
		return XML_CLOSE_STRING;
	}
	
}
