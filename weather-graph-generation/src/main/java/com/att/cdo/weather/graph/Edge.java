package com.att.cdo.weather.graph;

public class Edge {

	private Double gradient;
	private Integer inVertexId;
	
	public Double getGradient() {
		return gradient;
	}
	public void setGradient(Double gradient) {
		this.gradient = gradient;
	}
	public Integer getInVertexId() {
		return inVertexId;
	}
	public void setInVertexId(Integer inVertexId) {
		this.inVertexId = inVertexId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gradient == null) ? 0 : gradient.hashCode());
		result = prime * result + ((inVertexId == null) ? 0 : inVertexId.hashCode());
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
		Edge other = (Edge) obj;
		if (gradient == null) {
			if (other.gradient != null)
				return false;
		} else if (!gradient.equals(other.gradient))
			return false;
		if (inVertexId == null) {
			if (other.inVertexId != null)
				return false;
		} else if (!inVertexId.equals(other.inVertexId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Edge [gradient=" + gradient + ", inVertexId=" + inVertexId + "]";
	}
	
	
}
