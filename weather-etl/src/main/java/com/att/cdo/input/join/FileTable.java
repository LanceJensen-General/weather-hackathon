package com.att.cdo.input.join;

import java.io.File;
import java.util.List;

import com.att.cdo.parser.GenericType;

public class FileTable {
	
	private File input;
	private String delimiter;
	private List<String> headers;
	private String joinColumn;
	
	public File getInput() {
		return input;
	}
	public void setInput(File input) {
		this.input = input;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getJoinColumn() {
		return joinColumn;
	}
	public void setJoinColumn(String joinColumn) {
		this.joinColumn = joinColumn;
	}
	
	public List<String> getHeaders() {
		return headers;
	}
	@GenericType(genericType=String.class)
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delimiter == null) ? 0 : delimiter.hashCode());
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((joinColumn == null) ? 0 : joinColumn.hashCode());
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
		FileTable other = (FileTable) obj;
		if (delimiter == null) {
			if (other.delimiter != null)
				return false;
		} else if (!delimiter.equals(other.delimiter))
			return false;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (joinColumn == null) {
			if (other.joinColumn != null)
				return false;
		} else if (!joinColumn.equals(other.joinColumn))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileTable [input=" + input + ", delimiter=" + delimiter + ", headers=" + headers + ", joinColumn="
				+ joinColumn + "]";
	}
	
	
	
}
