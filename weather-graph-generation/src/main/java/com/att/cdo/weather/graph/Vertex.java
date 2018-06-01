package com.att.cdo.weather.graph;

import java.util.LinkedList;
import java.util.List;

import com.att.cdo.weather.domain.WeatherStation;
import com.att.cdo.weather.quadtree.NeighborDistance;

public class Vertex {
	
	private WeatherStation node;
	private Edge maxEdge;
	private List<Edge> edges;
	
	public Vertex(WeatherStation station) {
		this.node = station;
		this.edges = new LinkedList<Edge>();
	}
	
	public Integer getId() {
		return node.getGraphId();
	}
	
	public void addEdge(NeighborDistance edgeSource) {
		Edge newEdge = new Edge();
		WeatherStation source = (WeatherStation) edgeSource.getPointOfInterest();
		WeatherStation target = (WeatherStation) edgeSource.getNeighbor();
		newEdge.setGradient((target.getTempreture()-source.getTempreture())/edgeSource.getDistance());
		newEdge.setInVertexId(target.getGraphId());
		edges.add(newEdge);
		if(maxEdge == null || newEdge.getGradient() > this.maxEdge.getGradient()) {
			this.maxEdge = newEdge;
		}
	}
	
	public WeatherStation getNode() {
		return node;
	}

	public Edge getMaxGradientEdge() {
		return maxEdge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((maxEdge == null) ? 0 : maxEdge.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
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
		Vertex other = (Vertex) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (maxEdge == null) {
			if (other.maxEdge != null)
				return false;
		} else if (!maxEdge.equals(other.maxEdge))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vertex [node=" + node + ", maxEdge=" + maxEdge + ", edges=" + edges + "]";
	}

	
}
