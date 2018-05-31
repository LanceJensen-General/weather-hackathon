package com.att.cdo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

import com.att.cdo.input.etl.ETLConfiguration;
import com.att.cdo.input.etl.Group;
import com.att.cdo.input.etl.Project;
import com.att.cdo.input.etl.Select;
import com.att.cdo.input.etl.Transform;
import com.att.cdo.trait.Logging;

public class ETLService implements Logging {

	private static final String GROUP_MATCHER_PATTERN = "^.*[^\\\\][(].*$", DEFAULT_DELIMITER = "|";
	private Map<String, Integer> header = null;

	public void process(ETLConfiguration etlConfiguration) {

		getLogger().info("Starting ETL process with the following configuration:\n" + etlConfiguration.toString());

		setHeaders(etlConfiguration.getHeaders());

		List<String[]> records = readFileRecords(etlConfiguration.getInputTo(), etlConfiguration.getDelimiterPattern(),
				etlConfiguration.getSkipLines());

		List<String[]> selectedRecords = selectRecords(etlConfiguration.getSelect(), records);

		List<String[]> groupRecords = groupRecords(etlConfiguration.getGroup(), selectedRecords);

		List<String[]> transformedRecords = transformRecords(etlConfiguration.getTransform(), groupRecords);

		List<String[]> projectedRecords = projectRecords(etlConfiguration.getProject(), transformedRecords);

		String recordDelimiter = etlConfiguration.getDelimiterPattern();
		if (recordDelimiter.length() > 1) {
			recordDelimiter = DEFAULT_DELIMITER;
		}

		String serializedHeader = serializeHeader(this.header, recordDelimiter);

		List<String> serializedRecords = serializeRecords(projectedRecords, recordDelimiter);

		loadSerializedRecordsToDisk(serializedHeader, serializedRecords, etlConfiguration.getOutputTo(),
				etlConfiguration.getInputTo().getName());

		getLogger().info("ETL process completed successfully:\n" + etlConfiguration.toString());
	}

	/**
	 * groupRecords - is intended to do multiple aggregations but due to time
	 * will only do sums... broke with code changes... TODO: Fix
	 * 
	 * @param group
	 * @param records
	 * @return
	 */
	public List<String[]> groupRecords(Group group, List<String[]> records) {
		List<String[]> groupedRecords = new LinkedList<String[]>();
		if (group != null) {
			if (group.getFields().size() + 1 > header.size()) {
				throw new RuntimeException(
						"Only group by fields and the field to be aggrigated can be in the record. Use project to remove data.");
			} else if (group.getFields().size() + 1 < header.size()) {
				throw new RuntimeException("Record does not contain all the grouping fields.");
			}
			
			Map<String, GroupResult> aggregator = new HashMap<String, GroupResult>();
			for (String[] record : records) {
				String key = "";
				for (String groupByField : group.getFields()) {
					key = key + record[header.get(groupByField)] + "-";
				}
				Double value = new Double(record[header.get(group.getAggregationField())]);
				if (aggregator.containsKey(key)) {
					GroupResult currentResult = aggregator.get(key);
					currentResult.setAggregation(currentResult.getAggregation()
							+ new Double(record[header.get(group.getAggregationField())]));
					aggregator.put(key, currentResult);
				} else {
					GroupResult results = new GroupResult();
					results.setRecord(record);
					results.setAggregation(value);
					aggregator.put(key, results);
				}
			}
			
			for (GroupResult result : aggregator.values()) {
				String[] record = result.getRecord();
				record[header.get(group.getAggregationField())] = result.getAggregation().toString();
				groupedRecords.add(record);
			}
		} else {
			groupedRecords = records;
		}
		return groupedRecords;
	}

	public String serializeHeader(Map<String, Integer> header, String recordDelimiter) {
		String[] headerOrder = new String[header.size()];
		for (Entry<String, Integer> entry : header.entrySet()) {
			headerOrder[entry.getValue()] = entry.getKey();
		}
		return String.join(recordDelimiter, headerOrder);
	}

	public void loadSerializedRecordsToDisk(String serializedHeader, List<String> serializedRecords, File outputTarget,
			String name) {
		File outputFile = null;
		serializedRecords.add(0, serializedHeader);
		if (outputTarget.isDirectory()) {
			if (name == null) {
				name = UUID.fromString("serializedRecords").toString() + ".txt";
			}
			outputFile = new File(outputTarget, name);
		} else {
			outputFile = outputTarget;
		}
		try {
			Files.write(outputFile.toPath(), serializedRecords, new OpenOption[] { WRITE, CREATE_NEW });
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Error when writing to " + outputFile.getAbsolutePath() + ".", e);
			System.exit(1);
		}
	}

	public List<String> serializeRecords(List<String[]> records, String delimiter) {
		List<String> serializedRecords = new LinkedList<String>();
		for (String[] record : records) {
			serializedRecords.add(String.join(delimiter, record));
		}
		return serializedRecords;
	}

	public List<String[]> transformRecords(Transform transform, List<String[]> records) {
		List<String[]> transformedRecords = new LinkedList<String[]>();
		if (transform != null) {
			String columnName = transform.getColumn();
			int columnIndex = getColumnIndex(columnName);
			String pattern = transform.getPattern();
			String replacement = transform.getReplacement();
			for (String[] record : records) {
				record[columnIndex] = record[columnIndex].replaceAll(pattern, replacement);
				transformedRecords.add(record);
			}
		} else {
			transformedRecords = records;
		}
		return transformedRecords;
	}

	public List<String[]> projectRecords(Project project, List<String[]> records) {
		List<String[]> projectedRecords = new LinkedList<String[]>();
		if (project != null) {
			int[] columnIndexes = getColumnIndexes(project.getColumns());
			for (String[] record : records) {
				String[] projectedRecord = new String[columnIndexes.length];
				int index = 0;
				for (int columnIndex : columnIndexes) {
					projectedRecord[index] = record[columnIndex];
					index++;
				}
				projectedRecords.add(projectedRecord);
			}
			Map<String, Integer> newHeader = new HashMap<String, Integer>();
			int newIndex = 0;
			for (String projectedColumn : project.getColumns()) {
				newHeader.put(projectedColumn, newIndex);
				newIndex++;
			}
			header = newHeader;
		} else {
			projectedRecords = records;
		}
		return projectedRecords;
	}

	public List<String[]> selectRecords(Select select, List<String[]> records) {
		List<String[]> selectedRecords = new LinkedList<String[]>();
		if (select != null) {
			String columnName = select.getWhereColumn();
			int columnIndex = getColumnIndex(columnName);

			for (String[] record : records) {
				String field = record[columnIndex];
				String pattern = select.getMatches();
				if (field.matches(pattern)) {
					selectedRecords.add(record);
				}
			}
		} else {
			selectedRecords = records;
		}
		return selectedRecords;
	}

	public List<String[]> readFileRecords(File inputFile, String delimiterPattern, Integer skipLines) {
		List<String[]> records = new LinkedList<String[]>();
		if (skipLines == null) {
			skipLines = new Integer(0);
		}

		if (inputFile != null) {
			List<String> instances = new ArrayList<String>();
			try {
				instances = Files.readAllLines(inputFile.toPath());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not read " + inputFile.getAbsolutePath() + ".", e);
				System.exit(1);
			}

			boolean useMatchExtractor = delimiterPattern.matches(GROUP_MATCHER_PATTERN);
			Pattern matchPattern = Pattern.compile(delimiterPattern);

			for (int currentInstance = skipLines; currentInstance < instances.size(); currentInstance++) {
				String instance = instances.get(currentInstance);
				if (useMatchExtractor) {
					Matcher fieldMatcher = matchPattern.matcher(instance);
					String[] record = new String[fieldMatcher.groupCount()];
					if (fieldMatcher.matches()) {
						for (int group = 1; group <= fieldMatcher.groupCount(); group++) {
							record[group - 1] = fieldMatcher.group(group);
						}
						records.add(record);
					} else {
						getLogger().warning("Omitting unparsable record: " + instance);
					}
				} else {
					records.add(instance.split(Pattern.quote(delimiterPattern)));
				}
			}
		} else {
			getLogger().severe("The command -setETLConfiguration requires the flag --setInputTo <some/file/path>.");
			System.exit(1);
		}
		return records;
	}

	public HashMap<String, Integer> getHeaderMap(List<String> headers) {
		HashMap<String, Integer> headerMap = new HashMap<String, Integer>();
		int index = 0;
		for (String columnName : headers) {
			headerMap.put(columnName, index);
			index++;
		}
		return headerMap;
	}

	public void setHeaders(List<String> headers) {
		this.header = this.getHeaderMap(headers);
	}

	public int[] getColumnIndexes(List<String> columnNames) {
		int[] columnIndexes = new int[columnNames.size()];
		for (int index = 0; index < columnNames.size(); index++) {
			columnIndexes[index] = getColumnIndex(columnNames.get(index));
		}
		return columnIndexes;
	}

	public int getColumnIndex(String columnName) {
		if (header != null) {
			return header.get(columnName);
		} else {
			return new Integer(columnName);
		}
	}

}
