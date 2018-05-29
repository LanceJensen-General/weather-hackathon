package com.att.cdo.weather.quadtree;

import com.att.cdo.weather.spatial.Point;

/**
 * NeigborDistance - is a way of encapsulating the distance between neighboring
 * points in the QuadTree;
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 */
public class NeighborDistance implements Comparable<NeighborDistance> {
	
	private Point pointOfInterest;
	private Point neighbor;
	private Double distance;
	
	public NeighborDistance(Point pointOfInterest, Point neighbor) {
		this.pointOfInterest = pointOfInterest;
		this.neighbor = neighbor;
		calculateDistance();
	}

	public Point getPointOfInterest() {
		return pointOfInterest;
	}

	public Point getNeighbor() {
		return neighbor;
	}

	public Double getDistance() {
		return distance;
	}

	private void calculateDistance() {
		distance = Math.sqrt( 
				(Math.pow((pointOfInterest.getX()-neighbor.getX()), 2)) +
				(Math.pow((pointOfInterest.getY()-neighbor.getY()), 2))
				);
		
	}
	
	@Override
	public int compareTo(NeighborDistance distance) {
		if(this.getDistance() < distance.getDistance()) {
			return -1;
		} else if (this.getDistance() > distance.getDistance()) {
			return 1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((neighbor == null) ? 0 : neighbor.hashCode());
		result = prime * result + ((pointOfInterest == null) ? 0 : pointOfInterest.hashCode());
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
		NeighborDistance other = (NeighborDistance) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (neighbor == null) {
			if (other.neighbor != null)
				return false;
		} else if (!neighbor.equals(other.neighbor))
			return false;
		if (pointOfInterest == null) {
			if (other.pointOfInterest != null)
				return false;
		} else if (!pointOfInterest.equals(other.pointOfInterest))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NeighborDistance [pointOfInterest=" + pointOfInterest + ", neighbor=" + neighbor + ", distance="
				+ distance + "]";
	}

	
	
	

}
