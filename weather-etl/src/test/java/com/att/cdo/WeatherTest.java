package com.att.cdo;

import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.att.cdo.input.Configuration;
import com.att.cdo.input.etl.ETLConfiguration;
import com.att.cdo.input.etl.Select;
import com.att.cdo.input.ftp.FTPPullConfiguration;
import com.att.cdo.trait.Logging;

public class WeatherTest implements Logging {

	@Test
	public void shouldPullStationListingFileDown() throws MalformedURLException {
		// Given
		FTPPullConfiguration weatherConfiguration = new FTPPullConfiguration();
		File outputDirectory = new File("/Users/lj556b/weatherDownloadDirectory");
		weatherConfiguration.setOutputDirectory(outputDirectory);
		URL stationListingFile = new URL("ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-history.txt");
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
		File stationFile = new File("/Users/lj556b/weatherDownloadDirectory/isd-history.txt");
		File usStationsFile = new File("/Users/lj556b/weatherDownloadDirectory/usStations.txt");
		if (!stationFile.exists()) {
			shouldPullStationListingFileDown();
		}
		ETLConfiguration etlConfiguration = new ETLConfiguration();
		etlConfiguration.setDelimiterPattern(
				"^(.{6})\\s(.{5})\\s(.{29})\\s(.{4})\\s(.{2})\\s(.{5})\\s(.{7})\\s(.{8})\\s(.{7})\\s(.{8})\\s(.{8})$");
		etlConfiguration.setHeaders(Arrays.asList(new String[] { "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL",
				"LAT", "LON", "ELEV(M)", "BEGIN", "END" }));
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
	public void shouldTransformStationListToUsOnlyStationsWArgs() throws MalformedURLException {
		// Given
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/isd-history.txt");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStations.txt");
		String[] args = { "-etlConfiguration", "--inputTo", inputFile.getAbsolutePath(), "--outputTo",
				outputFile.getAbsolutePath(), "--skipLines", "22", "--delimiterPattern",
				"^(.{6})\\s(.{5})\\s(.{29})\\s(.{4})\\s(.{2})\\s(.{5})\\s(.{7})\\s(.{8})\\s(.{7})\\s(.{8})\\s(.{8})$",
				"--headers", "USAF", "WBAN", "STATION", "NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN",
				"END", "--select", "---whereColumn", "CTRY", "---matches", "^US.*$"

		};

		if (!inputFile.exists()) {
			shouldTransformStationListToUsOnlyStations();
		}
		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
	}

	@Test
	public void shouldTransformStationListToStationsInStatesWArgs() throws MalformedURLException {
		// Given
		File stationFile = new File("/Users/lj556b/weatherDownloadDirectory/usStations.txt");
		File usStationsFile = new File("/Users/lj556b/weatherDownloadDirectory/usStateStations.txt");
		String[] args = { "-etlConfiguration", "--delimiterPattern", "|", "--inputTo", stationFile.getAbsolutePath(),
				"--outputTo", usStationsFile.getAbsolutePath(), "--headers", "USAF", "WBAN", "STATION NAME", "CTRY",
				"ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "--select", "---whereColumn", "ST", "---matches",
				"^[A-Z].*$"
		};

		if (!stationFile.exists()) {
			shouldTransformStationListToUsOnlyStations();
		}
		// When
		Weather.main(args);
		// Then
		assertTrue(usStationsFile.exists());
	}

	@Test
	public void shouldTransformStationListOpenIn2017Or2018WArgs() throws MalformedURLException {
		// Given
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStateStations.txt");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017or2018.txt");
		String[] args = { "-etlConfiguration", "--delimiterPattern", "|", "--inputTo", inputFile.getAbsolutePath(),
				"--outputTo", outputFile.getAbsolutePath(), "--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST",
				"CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "--select", "---whereColumn", "END", "---matches",
				"^201[78].*$"

		};

		if (!inputFile.exists()) {
			shouldTransformStationListToUsOnlyStations();
		}
		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
	}

	@Test
	public void shouldConfirmStationsAllOpenIn2017() throws MalformedURLException {
		// Given
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017or2018.txt");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017.txt");
		String[] args = { 
				"-etlConfiguration", 
					"--delimiterPattern", "|", 
					"--inputTo", inputFile.getAbsolutePath(),
					"--outputTo", outputFile.getAbsolutePath(), 
					"--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", 
					"--select", 
						"---whereColumn", "BEGIN", 
						"---matches", "^((?!2018).)*$"

		};

		if (!inputFile.exists()) {
			shouldTransformStationListToUsOnlyStations();
		}
		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
	}

	@Test
	public void shouldPullStationDataFilesDownFromUrlList() throws MalformedURLException {
		// Given
		FTPPullConfiguration weatherConfiguration = new FTPPullConfiguration();
		File outputDirectory = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017");
		weatherConfiguration.setOutputDirectory(outputDirectory);
		File stationDataUrlListingFile = new File(
				"/Users/lj556b/weatherDownloadDirectory/2017_weather_station_listing_paths_final.txt");
		weatherConfiguration.setPullFilesFromUrlList(stationDataUrlListingFile);

		Configuration configuration = new Configuration();
		configuration.setWeatherPullConfiguration(weatherConfiguration);

		// When
		Weather.executeWeatherWorkflow(configuration);
	}

	
	
	// Create a raw temp table:
	// gunzip *.gz
	// awk '{print $0, FILENAME}' * >> rawTempTable.txt
	@Test
	public void shouldParseWeatherFile() {
		// Given
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/rawTempTable.txt");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/TempTable.psv");
		parseWeatherFile(inputFile,outputFile);
	}

	private void parseWeatherFile(File inputFile, File outputFile) {
		
		String[] args = { 
				"-etlConfiguration", 
					"--delimiterPattern", "^(.{4})\\s(.{2})\\s(.{2})\\s(.{2})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{5})\\s(.{17})$",
					"--inputTo", inputFile.getAbsolutePath(), 
					"--outputTo", outputFile.getAbsolutePath(), 
					"--headers", "YEAR", "MONTH", "DAY", "HOUR", "TEMP", "DEW", "PRESSURE", "WINDDIR", "WINDSPEED", "SKY", "RAIN", "RAIN6HR", "KEY" 
		};
		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
	}

	
	@Test
	public void shouldFilterForNoRainTempRecords() {
//		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/TempTable.psv");
//		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/MidnightTemps.psv");
//		String[] args = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", inputFile.getAbsolutePath(), 
//					"--outputTo", outputFile.getAbsolutePath(), 
//					"--headers", "YEAR", "MONTH", "DAY", "HOUR", "TEMP", "DEW", "PRESSURE", "WINDDIR", "WINDSPEED", "SKY", "RAIN", "RAIN6HR", "KEY",
//					"--select", 
//						"---whereColumn", "HOUR", 
//						"---matches", "^00$"
//		};
//		// When
//		Weather.main(args);
//		// Then
//		assertTrue(outputFile.exists());
//		File outputFile2 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/MidnightTempsNoRainIn6Hours.psv");		
//		String[] args2 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", outputFile.getAbsolutePath(), 
//					"--outputTo", outputFile2.getAbsolutePath(), 
//					"--headers", "YEAR", "MONTH", "DAY", "HOUR", "TEMP", "DEW", "PRESSURE", "WINDDIR", "WINDSPEED", "SKY", "RAIN", "RAIN6HR", "KEY",
//					"--select", 
//						"---whereColumn", "RAIN6HR", 
//						"---matches", "^-9999$",
//					"--project",	
//						"---columns", "RAIN6HR", "TEMP", "KEY", "MONTH", "DAY"
//		};
//		// When
//		Weather.main(args2);
//		// Then
//		assertTrue(outputFile2.exists());
//		
//		File outputFile3 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/MidnightTempsCount.psv");		
//		String[] args3 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", outputFile2.getAbsolutePath(), 
//					"--outputTo", outputFile3.getAbsolutePath(), 
//					"--headers", "RAIN6HR", "TEMP", "KEY", "MONTH", "DAY",
//					"--transform", 
//						"---column", "RAIN6HR", 
//						"---pattern", "^.*$", 
//						"---replacement", "1"
//		};
//		// When
//		Weather.main(args3);
//		// Then
//		assertTrue(outputFile3.exists());
//		
		File outputFile4 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/MidnightTempsCountMonthDay.psv");		
//		String[] args4 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "^(.{25}).(.{5})$",
//					"--inputTo", outputFile3.getAbsolutePath(), 
//					"--outputTo", outputFile4.getAbsolutePath(), 
//					"--headers", "1|TEMP|KEY", "MONTH*DAY",
//					"--transform", 
//						"---column", "MONTH*DAY", 
//						"---pattern", "^(\\d{2})\\|(\\d{2})$", 
//						"---replacement", "$1*$2"
//		};
//		// When
//		Weather.main(args4);
//		// Then
//		assertTrue(outputFile4.exists());
//		
//		File outputFile5 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/CountMonthDay.psv");		
//		String[] args5 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", outputFile4.getAbsolutePath(), 
//					"--outputTo", outputFile5.getAbsolutePath(), 
//					"--headers", "COUNT", "TEMP", "KEY", "MONTH*DAY",
//					"--project",	
//						"---columns", "COUNT", "KEY", "MONTH*DAY",
//		};
//		// When
//		Weather.main(args5);
//		// Then
//		assertTrue(outputFile5.exists());
//		
		File outputFile6 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/StationCountOfClearNight.psv");		
//		String[] args6 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", outputFile5.getAbsolutePath(), 
//					"--outputTo", outputFile6.getAbsolutePath(), 
//					"--headers", "COUNT", "KEY", "MONTH*DAY",
//					"--group", 
//						"---fields", "MONTH*DAY", "KEY",
//						"---aggregationType", "sum", 
//						"---aggregationField", "COUNT"
//		};
//		// When
//		Weather.main(args6);
//		// Then
//		assertTrue(outputFile6.exists());
		
		// Found that the March 20th 2017 had 916 precipitation free stations in the US
		File outputFile7 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/March20thMidnightStationTemp.psv");
		String[] args7 = { 
				"-etlConfiguration", 
					"--delimiterPattern", "|",
					"--inputTo", outputFile4.getAbsolutePath(), 
					"--outputTo", outputFile7.getAbsolutePath(), 
					"--headers", "COUNT", "TEMP", "KEY", "MONTH*DAY",
					"--select", 
						"---whereColumn", "MONTH*DAY", 
						"---matches", "^03\\*20$",
					"--project",	
						"---columns", "TEMP", "KEY"	
		};
		// When
		Weather.main(args7);
		// Then
		assertTrue(outputFile7.exists());
		
	}
	
	
	@Test
	public void shouldCreateTempJoinKey() {
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017.txt");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017TempKey.txt");
		String[] args = { 
				"-tableJoinConfiguration", 
					"--delimiter", "|",
					"--inputTo", inputFile.getAbsolutePath(), 
					"--outputTo", outputFile.getAbsolutePath(), 
					"--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", 
					"--fields", 
						"---columns", "USAF", "WBAN",
						"---delimiter", "*",
						"---newColumnName", "TEMP KEY"
						
		};

		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
	}
	
	@Test
	public void shouldTransformUSAFAndBanToKey() {
		// Given
				File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017TempKey.txt");
				File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017Key.txt");
				String[] args = { 
						"-etlConfiguration", 
							"--delimiterPattern", "|", 
							"--inputTo", inputFile.getAbsolutePath(),
							"--outputTo", outputFile.getAbsolutePath(), 
							"--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "TEMP KEY", 
							"--transform", 
								"---column", "TEMP KEY", 
								"---pattern", "^(\\d{6}).(\\d{5})$", 
								"---replacement", "$1-$2-2017"
				};

				// When
				Weather.main(args);
				// Then
				assertTrue(outputFile.exists());
	}
	
	@Test
	public void shouldJoinTempToStationInfo() {
		// Given
		File inputFile = new File("/Users/lj556b/weatherDownloadDirectory/stations2017Key.txt");
		File inputFile2 = new File("/Users/lj556b/weatherDownloadDirectory/usStationData2017/March20thMidnightStationTemp.psv");
		File outputFile = new File("/Users/lj556b/weatherDownloadDirectory/StationTempMarch30-2017.txt");
				
		String[] args = { 
				"-tableJoinConfiguration", 
					"--delimiter", "|",
					"--inputTo", inputFile.getAbsolutePath(), 
					"--outputTo", outputFile.getAbsolutePath(), 
					"--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "KEY", 
					"--fileTable", 
						"---input", inputFile2.getAbsolutePath(),
						"---delimiter", "|",
						"---headers", "TEMP", "KEY",
						"---joinColumn", "KEY"
		};
		
		// When
		Weather.main(args);
		// Then
		assertTrue(outputFile.exists());
		
//		File outputFile2 = new File("/Users/lj556b/weatherDownloadDirectory/StationTempMarch30-2017.txt");
//		String[] args2 = { 
//				"-etlConfiguration", 
//					"--delimiterPattern", "|",
//					"--inputTo", outputFile.getAbsolutePath(), 
//					"--outputTo", outputFile2.getAbsolutePath(), 
//					"--headers", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "KEY", "NULL", "TEMP", "KEY",
//					"--project",	
//						"---columns", "USAF", "WBAN", "STATION NAME", "CTRY", "ST", "CALL", "LAT", "LON", "ELEV(M)", "BEGIN", "END", "TEMP"
//		};
	}
	
}
