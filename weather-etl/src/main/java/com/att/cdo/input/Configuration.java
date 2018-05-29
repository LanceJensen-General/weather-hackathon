package com.att.cdo.input;

import com.att.cdo.input.etl.ETLConfiguration;
import com.att.cdo.input.ftp.FTPPullConfiguration;

public class Configuration {

	private FTPPullConfiguration weatherPullConfiguration;
	private ETLConfiguration etlConfiguration;
	
	public FTPPullConfiguration getWeatherPullConfiguration() {
		return weatherPullConfiguration;
	}
	public void setWeatherPullConfiguration(FTPPullConfiguration weatherPullConfiguration) {
		this.weatherPullConfiguration = weatherPullConfiguration;
	}
	public ETLConfiguration getEtlConfiguration() {
		return etlConfiguration;
	}
	public void setEtlConfiguration(ETLConfiguration etlConfiguration) {
		this.etlConfiguration = etlConfiguration;
	}

	
	
	
}
