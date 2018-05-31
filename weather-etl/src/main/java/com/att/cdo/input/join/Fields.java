package com.att.cdo.input.join;

import java.util.List;

import com.att.cdo.parser.GenericType;

public class Fields {
	
	private List<String> columns;
	private String delimiter = "";
	private String newColumnName;
	public List<String> getColumns() {
		return columns;
	}
	@GenericType(genericType=String.class)
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getNewColumnName() {
		return newColumnName;
	}
	public void setNewColumnName(String newColumnName) {
		this.newColumnName = newColumnName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columns == null) ? 0 : columns.hashCode());
		result = prime * result + ((delimiter == null) ? 0 : delimiter.hashCode());
		result = prime * result + ((newColumnName == null) ? 0 : newColumnName.hashCode());
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
		Fields other = (Fields) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
			return false;
		if (delimiter == null) {
			if (other.delimiter != null)
				return false;
		} else if (!delimiter.equals(other.delimiter))
			return false;
		if (newColumnName == null) {
			if (other.newColumnName != null)
				return false;
		} else if (!newColumnName.equals(other.newColumnName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Fields [columns=" + columns + ", delimiter=" + delimiter + ", newColumnName=" + newColumnName + "]";
	}
	
	
}
