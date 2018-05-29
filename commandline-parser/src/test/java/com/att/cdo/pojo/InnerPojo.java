package com.att.cdo.pojo;

public class InnerPojo {

	private String messageField;
	private Integer enclosingObjects;
	
	public String getMessageField() {
		return messageField;
	}
	public void setMessageField(String messageField) {
		this.messageField = messageField;
	}
	public Integer getEnclosingObjects() {
		return enclosingObjects;
	}
	public void setEnclosingObjects(Integer enclosingObjects) {
		this.enclosingObjects = enclosingObjects;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enclosingObjects == null) ? 0 : enclosingObjects.hashCode());
		result = prime * result + ((messageField == null) ? 0 : messageField.hashCode());
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
		InnerPojo other = (InnerPojo) obj;
		if (enclosingObjects == null) {
			if (other.enclosingObjects != null)
				return false;
		} else if (!enclosingObjects.equals(other.enclosingObjects))
			return false;
		if (messageField == null) {
			if (other.messageField != null)
				return false;
		} else if (!messageField.equals(other.messageField))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "InnerPojo [messageField=" + messageField + ", enclosingObjects=" + enclosingObjects + "]";
	}
	
	
}
