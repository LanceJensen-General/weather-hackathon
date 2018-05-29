package com.att.cdo;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;

import com.att.cdo.input.Configuration;
import com.att.cdo.input.etl.ETLConfiguration;
import com.att.cdo.input.etl.Select;
import com.att.cdo.input.ftp.FTPPullConfiguration;

public class WeatherTest {

	@Test
	public void shouldPullStationListingFileDown() throws MalformedURLException {
		// Given
		FTPPullConfiguration weatherConfiguration = new FTPPullConfiguration();
		File outputDirectory = new File("/tmp/weatherDownloadDirectory");
		weatherConfiguration.setOutputDirectory(outputDirectory);
		URL stationListingFile = new URL( "ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-history.txt");
		weatherConfiguration.setPullFile(stationListingFile);
		
		Configuration configuration = new Configuration();
		configuration.setWeatherPullConfiguration(weatherConfiguration);
		
		File expectedOutputFile = new File(outputDirectory, new File(stationListingFile.getFile()).getName());
		
		// When
		Weather.executeWeatherWorkflow(configuration);
		
		// Then
		assertTrue(expectedOutputFile.exists());
	}
	
	@Test
	public void shouldTransformStationListToUsOnlyStations() throws MalformedURLException {
		// Given
		File stationFile = new File("/tmp/weatherDownloadDirectory/isd-history.txt");
		File usStationsFile = new File("/tmp/weatherDownloadDirectory/usStations.txt");
		if(!stationFile.exists()) {
			shouldPullStationListingFileDown();
		}
		ETLConfiguration etlConfiguration = new ETLConfiguration();
		etlConfiguration.setDelimiterPattern(
				"^(.{6})\\s(.{5})\\s(.{29})\\s(.{4})\\s(.{2})\\s(.{5})\\s(.{7})\\s(.{8})\\s(.{7})\\s(.{8})\\s(.{8})$"
				);
		etlConfiguration.setHeaders(Arrays.asList(
				new String[]{"USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END"}
				));
		etlConfiguration.setInputTo(stationFile);
		etlConfiguration.setOutputTo(usStationsFile);
		etlConfiguration.setSkipLines(22);
		Select select = new Select();
		select.setWhereColumn("CTRY");
		select.setMatches("^US.*$");
		etlConfiguration.setSelect(select);
		Configuration configuration = new Configuration();
		configuration.setEtlConfiguration(etlConfiguration);
		// When
		Weather.executeWeatherWorkflow(configuration);
		// Then
		assertTrue(usStationsFile.exists());
	}

	
	@Test
	public void shouldTransformStationListToStationsInStates() throws MalformedURLException {
		// Given
		File stationFile = new File("/tmp/weatherDownloadDirectory/usStations.txt");
		File usStationsFile = new File("/tmp/weatherDownloadDirectory/usStateStations.txt");
		if(!stationFile.exists()) {
			shouldTransformStationListToUsOnlyStations();
		}
		ETLConfiguration etlConfiguration = new ETLConfiguration();
		etlConfiguration.setDelimiterPattern("|");
		etlConfiguration.setHeaders(Arrays.asList(
				new String[]{"USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END"}
				));
		etlConfiguration.setInputTo(stationFile);
		etlConfiguration.setOutputTo(usStationsFile);
		etlConfiguration.setSkipLines(22);
		Select select = new Select();
		select.setWhereColumn("ST");
		select.setMatches("^[A-Za-z]{2}$");
		etlConfiguration.setSelect(select);
		Configuration configuration = new Configuration();
		configuration.setEtlConfiguration(etlConfiguration);
		// When
		Weather.executeWeatherWorkflow(configuration);
		// Then
		assertTrue(usStationsFile.exists());
	}
	
	// Created stations files names in linux
	// cut -d'|' -f1,2 usStateStations.txt |tr "|" "-" | sed 's/$/-2017/' >> usStateStationFileNames.txt
	// paste -d '|' usStateStations.txt usStateStationFileNames.txt >> usStateStationsWithFileNames.txt
	// sed -e 's#^#ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-lite/2017/#' usStateStationFileNames.txt >> 2017_temp_weather_station_listing_paths.txt
	// sed -e 's#$#.gz#' 2017_temp_weather_station_listing_paths.txt >> 2017_weather_station_listing_paths.txt
	// sed '1d' 2017_weather_station_listing_paths.txt >> 2017_weather_station_listing_paths_final.txt
	@Test
	public void shouldPullStationDataFilesDownFromUrlList() throws MalformedURLException {
		// Given
		FTPPullConfiguration weatherConfiguration = new FTPPullConfiguration();
		File outputDirectory = new File("/tmp/weatherDownloadDirectory/stateStationData");
		weatherConfiguration.setOutputDirectory(outputDirectory);
		File stationDataUrlListingFile = new File("/tmp/weatherDownloadDirectory/2017_weather_station_listing_paths_final.txt");
		weatherConfiguration.setPullFilesFromUrlList(stationDataUrlListingFile);
		
		Configuration configuration = new Configuration();
		configuration.setWeatherPullConfiguration(weatherConfiguration);
			
		// When
		Weather.executeWeatherWorkflow(configuration);
	}
	
}
