package com.att.cdo.input.etl;

public class Select {
	
	private String whereColumn;
	private String matches;
	
	public String getWhereColumn() {
		return whereColumn;
	}
	public void setWhereColumn(String whereColumn) {
		this.whereColumn = whereColumn;
	}
	public String getMatches() {
		return matches;
	}
	public void setMatches(String matches) {
		this.matches = matches;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matches == null) ? 0 : matches.hashCode());
		result = prime * result + ((whereColumn == null) ? 0 : whereColumn.hashCode());
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
		Select other = (Select) obj;
		if (matches == null) {
			if (other.matches != null)
				return false;
		} else if (!matches.equals(other.matches))
			return false;
		if (whereColumn == null) {
			if (other.whereColumn != null)
				return false;
		} else if (!whereColumn.equals(other.whereColumn))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Select [whereColumn=" + whereColumn + ", matches=" + matches + "]";
	}
	
}
