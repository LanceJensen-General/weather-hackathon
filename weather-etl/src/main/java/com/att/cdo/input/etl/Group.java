package com.att.cdo.input.etl;

import java.util.List;

import com.att.cdo.parser.GenericType;

public class Group {

	private List<String> fields;
	private String aggregationType;
	private String aggregationField;
	
	public List<String> getFields() {
		return fields;
	}
	@GenericType(genericType=String.class)
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public String getAggregationType() {
		return aggregationType;
	}
	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
	}
	public String getAggregationField() {
		return aggregationField;
	}
	public void setAggregationField(String setAggregationField) {
		this.aggregationField = setAggregationField;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggregationType == null) ? 0 : aggregationType.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((aggregationField == null) ? 0 : aggregationField.hashCode());
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
		Group other = (Group) obj;
		if (aggregationType == null) {
			if (other.aggregationType != null)
				return false;
		} else if (!aggregationType.equals(other.aggregationType))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (aggregationField == null) {
			if (other.aggregationField != null)
				return false;
		} else if (!aggregationField.equals(other.aggregationField))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Group [fields=" + fields + ", aggregationType=" + aggregationType + ", setAggregationField="
				+ aggregationField + "]";
	}
	
	
	
}
