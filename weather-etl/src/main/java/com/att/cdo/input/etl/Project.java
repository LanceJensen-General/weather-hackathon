package com.att.cdo.input.etl;

import java.util.List;

import com.att.cdo.parser.GenericType;

public class Project {

	public List<String> columns;

	public List<String> getColumns() {
		return columns;
	}

	@GenericType(genericType=String.class)
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columns == null) ? 0 : columns.hashCode());
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
		Project other = (Project) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [columns=" + columns + "]";
	}
	
}
