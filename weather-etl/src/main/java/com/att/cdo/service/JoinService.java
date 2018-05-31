package com.att.cdo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.att.cdo.input.join.Fields;
import com.att.cdo.input.join.FileTable;
import com.att.cdo.input.join.JoinConfiguration;
import com.att.cdo.trait.Logging;

public class JoinService implements Logging{

	private ETLService etlService = new ETLService();

	public void process(JoinConfiguration joinConfiguration) {
		
		getLogger().info("Starting to join data with the following configuration: " + joinConfiguration.toString());
		
		JoinedData joinedData = null;
		List<String[]> leftRecords = etlService.readFileRecords(joinConfiguration.getInputTo(),
				joinConfiguration.getDelimiter(), 0);
		Map<String, Integer> leftHeader = etlService.getHeaderMap(joinConfiguration.getHeaders());

		if (joinConfiguration.getFields() != null) {
			joinedData = new JoinedData();
			joinedData.records = joinFields(leftHeader, leftRecords, joinConfiguration.getFields());
			leftHeader.put(joinConfiguration.getFields().getNewColumnName(), leftHeader.size());
			joinedData.header = leftHeader;
		} else {
			joinedData = new JoinedData();
			joinedData.records = leftRecords;
			joinedData.header = leftHeader;
		}

		if (joinConfiguration.getFileTable() != null) {
			joinedData = join(joinedData.records, joinedData.header, joinConfiguration.getFileTable());
		}
		
		etlService.loadSerializedRecordsToDisk(
				etlService.serializeHeader(joinedData.header, "|"), 
				etlService.serializeRecords(joinedData.records, joinConfiguration.getDelimiter()), 
				joinConfiguration.getOutputTo().getParentFile(),
				joinConfiguration.getOutputTo().getName());
		
		getLogger().info("The data was joined successfully " + joinConfiguration.toString());
	}

	private JoinedData join(List<String[]> leftRecords, Map<String, Integer> leftHeader, FileTable fileTable) {
		List<String[]> newRecords = new LinkedList<String[]>();
		List<String[]> rightRecords = etlService.readFileRecords(fileTable.getInput(), fileTable.getDelimiter(), 0);
		Map<String, Integer> rightHeader = etlService.getHeaderMap(fileTable.getHeaders());

		for (String[] leftRecord : leftRecords) {
			for (String[] rightRecord : rightRecords) {
				String leftField = leftRecord[leftHeader.get(fileTable.getJoinColumn())];
				String rightField = rightRecord[rightHeader.get(fileTable.getJoinColumn())];
				if (leftField != null && rightField != null && leftField.equals(rightField)) {
					String[] joinedRecord = new String[leftRecord.length + rightRecord.length];
					System.arraycopy(leftRecord, 0, joinedRecord, 0, leftRecord.length);
					System.arraycopy(rightRecord, 0, joinedRecord, leftRecord.length, rightRecord.length);
					newRecords.add(joinedRecord);
				}
			}
		}
		JoinedData joinData = new JoinedData();
		joinData.records = newRecords;
		joinData.header = new HashMap<String,Integer>();
		joinData.header.putAll(leftHeader);
		for(String key : rightHeader.keySet()) {
			if(joinData.header.containsKey(key)) {
				Integer index = joinData.header.get(key);
				joinData.header.remove(key);
				joinData.header.put(key+ "(left)", index);
			} 
			joinData.header.put(key, rightHeader.get(key) + leftHeader.size());
		}
		return joinData;
	}

	private List<String[]> joinFields(Map<String, Integer> header, List<String[]> leftRecords, Fields fields) {
		List<String[]> newRecords = new ArrayList<String[]>(leftRecords.size());
		StringBuilder buffer = new StringBuilder();
		for (String[] record : leftRecords) {
			String[] newInstance = new String[record.length + 1];
			System.arraycopy(record, 0, newInstance, 0, record.length);
			for (String joinColumn : fields.getColumns()) {
				if (buffer.length() <= 0) {
					buffer.append(record[header.get(joinColumn)]);
				} else {
					buffer.append(fields.getDelimiter()).append(record[header.get(joinColumn)]);
				}
			}
			newInstance[record.length] = buffer.toString();
			buffer.setLength(0);
			newRecords.add(newInstance);
		}
		return newRecords;
	}

	private class JoinedData
	{
		protected Map<String, Integer> header;
		protected List<String[]> records;
	}

}
