package com.att.cdo.input.etl;

import java.io.File;

import java.util.List;

public class ETLConfiguration {
	
	private String delimiterPattern = "|";
	private List<String> headers;
	private File inputTo;
	private File outputTo = new File(".");
	private Integer skipLines;
	private Select select;
	private Group group;
	private Project project;
	private Transform transform;
	
	public String getDelimiterPattern() {
		return delimiterPattern;
	}
	public void setDelimiterPattern(String delimiterPattern) {
		this.delimiterPattern = delimiterPattern;
	}
	public List<String> getHeaders() {
		return headers;
	}
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
	public Integer getSkipLines() {
		return skipLines;
	}
	public void setSkipLines(Integer skipLines) {
		this.skipLines = skipLines;
	}
	public Select getSelect() {
		return select;
	}
	public void setSelect(Select select) {
		this.select = select;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Transform getTransform() {
		return transform;
	}
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delimiterPattern == null) ? 0 : delimiterPattern.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((inputTo == null) ? 0 : inputTo.hashCode());
		result = prime * result + ((outputTo == null) ? 0 : outputTo.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((select == null) ? 0 : select.hashCode());
		result = prime * result + ((skipLines == null) ? 0 : skipLines.hashCode());
		result = prime * result + ((transform == null) ? 0 : transform.hashCode());
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
		ETLConfiguration other = (ETLConfiguration) obj;
		if (delimiterPattern == null) {
			if (other.delimiterPattern != null)
				return false;
		} else if (!delimiterPattern.equals(other.delimiterPattern))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
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
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (select == null) {
			if (other.select != null)
				return false;
		} else if (!select.equals(other.select))
			return false;
		if (skipLines == null) {
			if (other.skipLines != null)
				return false;
		} else if (!skipLines.equals(other.skipLines))
			return false;
		if (transform == null) {
			if (other.transform != null)
				return false;
		} else if (!transform.equals(other.transform))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ETLConfiguration [delimiterPattern=" + delimiterPattern + ", headers=" + headers + ", inputTo="
				+ inputTo + ", outputTo=" + outputTo + ", skipLines=" + skipLines + ", select=" + select + ", group="
				+ group + ", project=" + project + ", transform=" + transform + "]";
	}
	
	
}
