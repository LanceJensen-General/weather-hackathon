package com.att.cdo.pojo;

import java.io.File;
import java.net.URI;
import java.util.List;

import com.att.cdo.parser.GenericType;

public class TestPojo {

	private String stringField;
	private List<String> stringsField;
	
	private File fileField;
	private List<File> filesField;
	
	private URI uriField;
	private List<URI> urisField;
	
	private Integer integerField;
	private List<Integer> integersField;
	
	private Double doubleField;
	private List<Double> doublesField;
	
	private Boolean booleanField;
	private List<Boolean> booleansField;
	
	private InnerPojo innerPojo;
	
	
	public InnerPojo getInnerPojo() {
		return innerPojo;
	}
	
	public void setInnerPojo(InnerPojo innerPojo) {
		this.innerPojo = innerPojo;
	}
	
	public String getStringField() {
		return stringField;
	}
	public void setStringField(String stringField) {
		this.stringField = stringField;
	}
	public List<String> getStringsField() {
		return stringsField;
	}
	@GenericType(genericType = String.class)
	public void setStringsField(List<String> stringsField) {
		this.stringsField = stringsField;
	}
	public File getFileField() {
		return fileField;
	}
	public void setFileField(File fileField) {
		fileField = fileField;
	}
	public List<File> getFilesField() {
		return filesField;
	}
	
	@GenericType(genericType = File.class)
	public void setFilesField(List<File> filesField) {
		filesField = filesField;
	}
	public URI getUriField() {
		return uriField;
	}
	public void setUriField(URI uriField) {
		this.uriField = uriField;
	}
	public List<URI> getUrisField() {
		return urisField;
	}
	
	@GenericType(genericType = URI.class)
	public void setUrisField(List<URI> urisField) {
		this.urisField = urisField;
	}
	public Integer getIntegerField() {
		return integerField;
	}
	public void setIntegerField(Integer integerField) {
		this.integerField = integerField;
	}
	public List<Integer> getIntegersField() {
		return integersField;
	}
	
	@GenericType(genericType = Integer.class)
	public void setIntegersField(List<Integer> integersField) {
		this.integersField = integersField;
	}
	public Double getDoubleField() {
		return doubleField;
	}
	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}
	public List<Double> getDoublesField() {
		return doublesField;
	}
	
	@GenericType(genericType = Double.class)
	public void setDoublesField(List<Double> doublesField) {
		this.doublesField = doublesField;
	}
	public Boolean getBooleanField() {
		return booleanField;
	}
	public void setBooleanField(Boolean booleanField) {
		this.booleanField = booleanField;
	}
	public List<Boolean> getBooleansField() {
		return booleansField;
	}
	
	@GenericType(genericType = Boolean.class)
	public void setBooleansField(List<Boolean> booleansField) {
		this.booleansField = booleansField;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booleanField == null) ? 0 : booleanField.hashCode());
		result = prime * result + ((booleansField == null) ? 0 : booleansField.hashCode());
		result = prime * result + ((doubleField == null) ? 0 : doubleField.hashCode());
		result = prime * result + ((doublesField == null) ? 0 : doublesField.hashCode());
		result = prime * result + ((fileField == null) ? 0 : fileField.hashCode());
		result = prime * result + ((filesField == null) ? 0 : filesField.hashCode());
		result = prime * result + ((innerPojo == null) ? 0 : innerPojo.hashCode());
		result = prime * result + ((integerField == null) ? 0 : integerField.hashCode());
		result = prime * result + ((integersField == null) ? 0 : integersField.hashCode());
		result = prime * result + ((stringField == null) ? 0 : stringField.hashCode());
		result = prime * result + ((stringsField == null) ? 0 : stringsField.hashCode());
		result = prime * result + ((uriField == null) ? 0 : uriField.hashCode());
		result = prime * result + ((urisField == null) ? 0 : urisField.hashCode());
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
		TestPojo other = (TestPojo) obj;
		if (booleanField == null) {
			if (other.booleanField != null)
				return false;
		} else if (!booleanField.equals(other.booleanField))
			return false;
		if (booleansField == null) {
			if (other.booleansField != null)
				return false;
		} else if (!booleansField.equals(other.booleansField))
			return false;
		if (doubleField == null) {
			if (other.doubleField != null)
				return false;
		} else if (!doubleField.equals(other.doubleField))
			return false;
		if (doublesField == null) {
			if (other.doublesField != null)
				return false;
		} else if (!doublesField.equals(other.doublesField))
			return false;
		if (fileField == null) {
			if (other.fileField != null)
				return false;
		} else if (!fileField.equals(other.fileField))
			return false;
		if (filesField == null) {
			if (other.filesField != null)
				return false;
		} else if (!filesField.equals(other.filesField))
			return false;
		if (innerPojo == null) {
			if (other.innerPojo != null)
				return false;
		} else if (!innerPojo.equals(other.innerPojo))
			return false;
		if (integerField == null) {
			if (other.integerField != null)
				return false;
		} else if (!integerField.equals(other.integerField))
			return false;
		if (integersField == null) {
			if (other.integersField != null)
				return false;
		} else if (!integersField.equals(other.integersField))
			return false;
		if (stringField == null) {
			if (other.stringField != null)
				return false;
		} else if (!stringField.equals(other.stringField))
			return false;
		if (stringsField == null) {
			if (other.stringsField != null)
				return false;
		} else if (!stringsField.equals(other.stringsField))
			return false;
		if (uriField == null) {
			if (other.uriField != null)
				return false;
		} else if (!uriField.equals(other.uriField))
			return false;
		if (urisField == null) {
			if (other.urisField != null)
				return false;
		} else if (!urisField.equals(other.urisField))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestPojo [stringField=" + stringField + ", stringsField=" + stringsField + ", fileField=" + fileField
				+ ", filesField=" + filesField + ", uriField=" + uriField + ", urisField=" + urisField
				+ ", integerField=" + integerField + ", integersField=" + integersField + ", doubleField=" + doubleField
				+ ", doublesField=" + doublesField + ", booleanField=" + booleanField + ", booleansField="
				+ booleansField + ", innerPojo=" + innerPojo + "]";
	}
	
	
	
	
	
	
}
