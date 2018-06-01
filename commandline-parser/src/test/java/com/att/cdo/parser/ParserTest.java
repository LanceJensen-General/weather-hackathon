package com.att.cdo.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.att.cdo.converter.InvalidConfigurationException;
import com.att.cdo.pojo.InnerPojo;
import com.att.cdo.pojo.TestPojo;

/**
 * ParserTest - is a junit test class designed to verify that the Parser can
 * parse data to a pojo.
 *
 */
public class ParserTest {

	public static int FIRST_ENTRY = 0, SECOND_ENTRY = 1;
	public static int FIRST_ARRAY_ENTRY = 1, SECOND_ARRAY_ENTRY = 2, LAST_ARRAY_ENTRY = 4;
	public static int EXPECTED_LIST_SIZE = 2;

	@Test
	public void shouldHandleParsingUris()
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException, URISyntaxException {
		// Given
		String expectedUriString = "ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-lite/isd-lite-format.txt";
		String[] args = { "-urisField", expectedUriString,
				expectedUriString, "-uriField",
				expectedUriString };
		TestPojo pojo = new TestPojo();

		// When
		Parser.parse(args, pojo);

		// Then
		assertThat(pojo.getUrisField().size(), is(EXPECTED_LIST_SIZE));
		assertThat(pojo.getUrisField().get(FIRST_ENTRY), is(new URI(args[FIRST_ARRAY_ENTRY])));
		assertThat(pojo.getUrisField().get(SECOND_ENTRY), is(new URI(args[SECOND_ARRAY_ENTRY])));
		assertThat(pojo.getUriField(), is(new URI(args[LAST_ARRAY_ENTRY])));
	}

	

	@Test
	public void shouldHandleParsingIntegers()
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		// Given
		String[] args = { "-integersField", "1", "2", "-integerField", "3" };
		TestPojo pojo = new TestPojo();

		// When
		Parser.parse(args, pojo);

		// Then
		assertThat(pojo.getIntegersField().size(), is(EXPECTED_LIST_SIZE));
		assertThat(pojo.getIntegersField().get(FIRST_ENTRY), is(new Integer(args[FIRST_ARRAY_ENTRY])));
		assertThat(pojo.getIntegersField().get(SECOND_ENTRY), is(new Integer(args[SECOND_ARRAY_ENTRY])));
		assertThat(pojo.getIntegerField(), is(new Integer(args[LAST_ARRAY_ENTRY])));
	}

	@Test
	public void shouldHandleParsingDoubles()
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		// Given
		String[] args = { "-doublesField", "1.0", "2.0", "-doubleField", "3.0" };
		TestPojo pojo = new TestPojo();

		// When
		Parser.parse(args, pojo);

		// Then
		assertThat(pojo.getDoublesField().size(), is(EXPECTED_LIST_SIZE));
		assertThat(pojo.getDoublesField().get(FIRST_ENTRY), is(new Double(args[FIRST_ARRAY_ENTRY])));
		assertThat(pojo.getDoublesField().get(SECOND_ENTRY), is(new Double(args[SECOND_ARRAY_ENTRY])));
		assertThat(pojo.getDoubleField(), is(new Double(args[LAST_ARRAY_ENTRY])));
	}

	@Test
	public void shouldHandleParsingStrings()
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		// Given
		String[] args = { "-stringsField", "listEntry1", "listEntry2", "-stringField", "fieldValue" };
		TestPojo pojo = new TestPojo();

		// When
		Parser.parse(args, pojo);

		// Then
		assertThat(pojo.getStringsField().size(), is(EXPECTED_LIST_SIZE));
		assertThat(pojo.getStringsField().get(FIRST_ENTRY), is(args[FIRST_ARRAY_ENTRY]));
		assertThat(pojo.getStringsField().get(SECOND_ENTRY), is(args[SECOND_ARRAY_ENTRY]));
		assertThat(pojo.getStringField(), is(args[LAST_ARRAY_ENTRY]));
	}
	
//	@Test
//	public void shouldHandleParsingFiles()
//			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
//			InvalidPojoSetterConfiguration, InvalidConfigurationException {
//		// Given
//		String[] args = { "-filesField", "/tmp/1", "/tmp/2", "-fileField", "/tmp/3" };
//		TestPojo pojo = new TestPojo();
//
//		// When
//		Parser.parse(args, pojo);
//
//		// Then
//		assertThat(pojo.getFilesField().size(), is(EXPECTED_LIST_SIZE));
//		assertThat(pojo.getFilesField().get(FIRST_ENTRY), is(new File(args[FIRST_ARRAY_ENTRY])));
//		assertThat(pojo.getFilesField().get(SECOND_ENTRY), is(new File(args[SECOND_ARRAY_ENTRY])));
//		assertThat(pojo.getFileField(), is(new File(args[LAST_ARRAY_ENTRY])));
//	}
	
	@Test
	public void shouldHandleNestedPojoStructure()
			throws IlleagalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException,
			InvalidPojoSetterConfiguration, InvalidConfigurationException {
		// Given
		String[] args = { "-innerPojo", "--messageField", "Hello from the inner pojo", "--enclosingObjects", "1" };
		InnerPojo expectedInnerPojo = new InnerPojo();
		expectedInnerPojo.setEnclosingObjects(new Integer("1"));
		expectedInnerPojo.setMessageField(args[2]);
		TestPojo pojo = new TestPojo();

		// When
		Parser.parse(args, pojo);

		// Then
		assertEquals(pojo.getInnerPojo(),expectedInnerPojo);
	}
}
