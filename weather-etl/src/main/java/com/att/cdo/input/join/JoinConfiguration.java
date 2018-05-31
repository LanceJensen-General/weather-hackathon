package com.att.cdo.input.join;

import java.io.File;
import java.util.List;

import com.att.cdo.parser.GenericType;

public class JoinConfiguration {

	private String delimiter = "|";
	private List<String> headers;
	private File inputTo;
	private File outputTo = new File(".");
	
	private Fields fields;
	private FileTable fileTable;
	
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiterPattern) {
		this.delimiter = delimiterPattern;
	}
	public List<String> getHeaders() {
		return headers;
	}
	@GenericType(genericType=String.class)
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	public File getInputTo() {
		return inputTo;
	}
	public void setInputTo(File inputTo) {
		this.inputTo = inputTo;
	}
	public File getOutputTo() {
		return outputTo;
	}
	public void setOutputTo(File outputTo) {
		this.outputTo = outputTo;
	}
	public Fields getFields() {
		return fields;
	}
	public void setFields(Fields fields) {
		this.fields = fields;
	}
	public FileTable getFileTable() {
		return fileTable;
	}
	public void setFileTable(FileTable fileTable) {
		this.fileTable = fileTable;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delimiter == null) ? 0 : delimiter.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((fileTable == null) ? 0 : fileTable.hashCode());
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((inputTo == null) ? 0 : inputTo.hashCode());
		result = prime * result + ((outputTo == null) ? 0 : outputTo.hashCode());
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
		JoinConfiguration other = (JoinConfiguration) obj;
		if (delimiter == null) {
			if (other.delimiter != null)
				return false;
		} else if (!delimiter.equals(other.delimiter))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (fileTable == null) {
			if (other.fileTable != null)
				return false;
		} else if (!fileTable.equals(other.fileTable))
			return false;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (inputTo == null) {
			if (other.inputTo != null)
				return false;
		} else if (!inputTo.equals(other.inputTo))
			return false;
		if (outputTo == null) {
			if (other.outputTo != null)
				return false;
		} else if (!outputTo.equals(other.outputTo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Join [delimiterPattern=" + delimiter + ", headers=" + headers + ", inputTo=" + inputTo
				+ ", outputTo=" + outputTo + ", fields=" + fields + ", fileTable=" + fileTable + "]";
	}
	
	
}
