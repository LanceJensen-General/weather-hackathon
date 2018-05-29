package com.att.cdo.weather.quadtree;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.att.cdo.trait.Logging;
import com.att.cdo.weather.spatial.Point;
import com.att.cdo.weather.spatial.ImmutablePoint;

/**
 * QuadTree - is a data structure that allows you to insertion sort a 2d space
 * by dynamically bucking points in in recursively smaller sub quadrants so that
 * each leaf has at most 3 siblings.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 */
public class QuadTree implements Logging {

	private HashSet<ImmutablePoint> pointRegestry;
	private QuadNode root;

	public QuadTree() {
		this.root = new QuadNode();
		this.pointRegestry = new HashSet<ImmutablePoint>();
	}

	/**
	 * addPoint - adds a point to the QuadTree so that its nearest neighbors can
	 * be found
	 * 
	 * @param point
	 *            to be added to the tree.
	 * @throws SpaceTimeViolation 
	 */
	public void addPoint(Point point) throws SpaceTimeViolation {
		ImmutablePoint pointToRegester = new ImmutablePoint(point.getX(), point.getY());
		if (pointRegestry.contains(pointToRegester)) {
			getLogger().warning("Adding the point " + point.toString()
					+ " is a violation of the space time continum and will be ignored with extream prejudice!");
			throw new SpaceTimeViolation("The point " + point.toString() + " is being rejected as it can not occupy the same point in this graph.");
		} else {
			pointRegestry.add(pointToRegester);
		}
		
		while (!root.bounds(point)) {
			root = root.increaseRootQuadNodeExtents(point);
		}
		root.add(point);
	}

	/**
	 * findNearestNeighbors - seeks to the spatial location of the point of
	 * interest and produces the set of neighbor distances containing the
	 * requiredNumber of nearest neighbors.
	 * 
	 * @param pointOfInterest
	 *            - is a point to find the neighbors for.
	 * @param requiredNeighbors
	 *            - is the minimum number of neighbors to find.
	 * @param noMoreThanRequiredNeighbors
	 *            - allows the user to require that there is exactly the
	 *            required number of neighbors.
	 * @return a list of NeighborDistance objects that near to the point of
	 *         interest.
	 */
	public List<NeighborDistance> findNearestNeighbors(Point pointOfInterest, int requiredNeighbors,
			boolean noMoreThanRequiredNeighbors) {
		List<NeighborDistance> neighbors = root.findNearestNeighbors(pointOfInterest, requiredNeighbors);
		Collections.sort(neighbors);
		if (noMoreThanRequiredNeighbors) {
			neighbors = neighbors.subList(0, requiredNeighbors);
		}
		return neighbors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
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
		QuadTree other = (QuadTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuadTree [root=" + root + "]";
	}

}
