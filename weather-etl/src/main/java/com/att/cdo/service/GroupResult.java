package com.att.cdo.service;

import java.util.List;

public class GroupResult {

	private String[] record;
	private Double aggregation;
	
	public String[] getRecord() {
		return record;
	}
	public void setRecord(String[] record) {
		this.record = record;
	}
	public Double getAggregation() {
		return aggregation;
	}
	public void setAggregation(Double aggregation) {
		this.aggregation = aggregation;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggregation == null) ? 0 : aggregation.hashCode());
		result = prime * result + ((record == null) ? 0 : record.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupResult other = (GroupResult) obj;
		if (aggregation == null) {
			if (other.aggregation != null)
				return false;
		} else if (!aggregation.equals(other.aggregation))
			return false;
		if (record == null) {
			if (other.record != null)
				return false;
		} else if (!record.equals(other.record))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GroupResult [record=" + record + ", aggregation=" + aggregation + "]";
	}
	
	
	
}
