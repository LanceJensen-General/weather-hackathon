package com.att.cdo.weather.input;

import java.io.File;

public class Configuration {
	
	private File inputStationDataFile;
	private File outputGraphMlFile;
	
	public File getInputStationDataFile() {
		return inputStationDataFile;
	}
	public void setInputStationDataFile(File inputStationDataFile) {
		this.inputStationDataFile = inputStationDataFile;
	}
	public File getOutputGraphMlFile() {
		return outputGraphMlFile;
	}
	public void setOutputGraphMlFile(File outputGraphMlFile) {
		this.outputGraphMlFile = outputGraphMlFile;
	}
	
	

}
