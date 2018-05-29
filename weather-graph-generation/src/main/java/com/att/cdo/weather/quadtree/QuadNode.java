package com.att.cdo.weather.quadtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.att.cdo.weather.spatial.Point;

public class QuadNode {

	private SquareBounds areaBounds;
	protected List<Point> leaves;
	protected QuadNode lowerLeftQuad;
	protected QuadNode lowerRightQuad;
	protected QuadNode upperLeftQuad;
	protected QuadNode upperRightQuad;

	/**
	 * Creates a QuadNode without specific boundaries. Boundaries will be
	 * inferred automatically when the fourth node is added to the quad.
	 */
	public QuadNode() {
		this.leaves = new ArrayList<Point>(4);
	}

	/**
	 * Creates a new QuadNode with fixed boundaries.
	 * 
	 * @param quadNodeArea
	 */
	private QuadNode(SquareBounds quadNodeArea) {
		this.areaBounds = quadNodeArea;
		this.leaves = new ArrayList<Point>(4);
	}

	/**
	 * addPoint - adds a point to the quad tree. If a point is added to a quad
	 * node it is checked on if it has sub quadrants. If sub quadrants exist and
	 * the point can be binned in a sub quadrant then its addPoint method is
	 * called until the point reaches a leaf position in a quadrants with no
	 * more than 3 points. Upon 4 point being present in a node it will split
	 * dividing the area into 4 equal area sub QuadNodes.
	 * 
	 * @param point
	 * @return
	 */
	public void add(Point point) {
		if (shouldPassToSubQuad(point)) {
			passToSubQuad(point);
		} else if (shouldAddAsLeaf()) {
			leaves.add(point);
		} else {
			leaves.add(point);
			if (areaBounds == null) {
				areaBounds = new SquareBounds(leaves);
			}
			Map<Integer, SquareBounds> quadrents = areaBounds.quadrateBounds();
			this.lowerLeftQuad = new QuadNode(quadrents.get(SquareBounds.LOWER_LEFT_QUADRENT));
			this.lowerRightQuad = new QuadNode(quadrents.get(SquareBounds.LOWER_RIGHT_QUADRENT));
			this.upperLeftQuad = new QuadNode(quadrents.get(SquareBounds.UPPER_LEFT_QUADRENT));
			this.upperRightQuad = new QuadNode(quadrents.get(SquareBounds.UPPER_RIGHT_QUADRENT));
			for (Point currentPoint : leaves) {
				passToSubQuad(currentPoint);
			}
			leaves = null;
		}

	}
	
	/**
	 * findNearestNeighbors - finds a required number of nearest neighbor candidates to the point of interest.
	 * @param pointOfInterest - is a point inserted into the QuadTree.
	 * @param requiredNeighbors - is the minimum number of neighbors required.
	 * @return an unsorted list on nearest neighbor distance canidates at or above the required number.
	 */
	public List<NeighborDistance> findNearestNeighbors(Point pointOfInterest, int requiredNeighbors) {
		if(containsSubQuads()) {
			QuadNode pointsQuadrant = findSubQuadFor(pointOfInterest);
			List<NeighborDistance> neighbors = pointsQuadrant.findNearestNeighbors(pointOfInterest, requiredNeighbors);
			if(neighbors.size() >= requiredNeighbors) {
				return neighbors;
			}
			if(this.lowerLeftQuad != pointsQuadrant) {
				neighbors.addAll(lowerLeftQuad.findNeigborDistancesFor(pointOfInterest));
			}
			if(this.lowerRightQuad != pointsQuadrant) {
				neighbors.addAll(lowerRightQuad.findNeigborDistancesFor(pointOfInterest));
			}
			if(this.upperLeftQuad != pointsQuadrant) {
				neighbors.addAll(upperLeftQuad.findNeigborDistancesFor(pointOfInterest));
			}
			if(this.upperRightQuad != pointsQuadrant) {
				neighbors.addAll(upperRightQuad.findNeigborDistancesFor(pointOfInterest));
			}
			return neighbors;
		} else { // Base Case
			List<NeighborDistance> neighbors = new ArrayList<NeighborDistance>(requiredNeighbors*2);
			for(Point currentPoint : leaves) {
				if(!currentPoint.equals(pointOfInterest)) {
					neighbors.add(new NeighborDistance(pointOfInterest,currentPoint));
				}
			}
			return neighbors;
		}
	}

	private List<NeighborDistance> findNeigborDistancesFor(Point pointOfInterest) {
		List<Point> decendents = getLeafNodes();
		List<NeighborDistance> neighbors = new ArrayList<NeighborDistance>(decendents.size());
		for(Point currentPoint : decendents) {
			neighbors.add(new NeighborDistance(pointOfInterest,currentPoint));
		}
		return neighbors;
	}
	

	private List<Point> getLeafNodes() {
		if(containsSubQuads()) {
			List<Point> neighbors = new ArrayList<Point>();
			neighbors.addAll(lowerLeftQuad.getLeafNodes());
			neighbors.addAll(lowerRightQuad.getLeafNodes());
			neighbors.addAll(upperLeftQuad.getLeafNodes());
			neighbors.addAll(upperRightQuad.getLeafNodes());
			return neighbors;
		} else {
			return leaves;
		}
	}

	/**
	 * bounds - checks to see if this quad node's extents bounds the point. If
	 * there are no bounds to the quad node then it is root and by default
	 * bounds all points.
	 * 
	 * @param point
	 *            - is a coordinate to see if it is within the quad nodes
	 *            extents.
	 * @return true if the point is within the extents or false otherwise.
	 */
	public boolean bounds(Point point) {
		if (areaBounds == null) {
			return true;
		}
		return areaBounds.contains(point);
	}

	/**
	 * increaseRootQuadNodeExtents - creates a new root node with 4x the area
	 * which has grown in the direction of the unboundedPoint argument;
	 * 
	 * @param unboundedPoint
	 *            - is used to grow the direction of the quad tree when the new
	 *            root node is created.
	 * @return a new root QuadNode with the old root as one of its children.
	 */
	public QuadNode increaseRootQuadNodeExtents(Point unboundedPoint) {
		int pointQuadrant = areaBounds.getQuadrantFor(unboundedPoint);
		QuadNode newRoot = new QuadNode(
				new SquareBounds(areaBounds.getQuadrantExtentsPoint(pointQuadrant), areaBounds.getWidth()));
		newRoot.leaves = null;
		Map<Integer, SquareBounds> quadrents = newRoot.areaBounds.quadrateBounds();
		newRoot.lowerLeftQuad = new QuadNode(quadrents.get(SquareBounds.LOWER_LEFT_QUADRENT));
		newRoot.lowerRightQuad = new QuadNode(quadrents.get(SquareBounds.LOWER_RIGHT_QUADRENT));
		newRoot.upperLeftQuad = new QuadNode(quadrents.get(SquareBounds.UPPER_LEFT_QUADRENT));
		newRoot.upperRightQuad = new QuadNode(quadrents.get(SquareBounds.UPPER_RIGHT_QUADRENT));
		
		switch (pointQuadrant) {
		case SquareBounds.LOWER_LEFT_QUADRENT:
			newRoot.upperRightQuad = this;
			break;
		case SquareBounds.LOWER_RIGHT_QUADRENT:
			newRoot.upperLeftQuad = this;
			break;
		case SquareBounds.UPPER_LEFT_QUADRENT:
			newRoot.lowerRightQuad = this;
			break;
		case SquareBounds.UPPER_RIGHT_QUADRENT:
			newRoot.lowerLeftQuad = this;
			break;
		}
		return newRoot;

	}

	private void passToSubQuad(Point point) {
		findSubQuadFor(point).add(point);
	}
	
	private QuadNode findSubQuadFor(Point point) {
		QuadNode subQuad = null;
		Integer quadrant = areaBounds.getQuadrantFor(point);
		switch (quadrant) {
		case SquareBounds.LOWER_LEFT_QUADRENT:
			subQuad = lowerLeftQuad;
			break;
		case SquareBounds.LOWER_RIGHT_QUADRENT:
			subQuad = lowerRightQuad;
			break;
		case SquareBounds.UPPER_LEFT_QUADRENT:
			subQuad = upperLeftQuad;
			break;
		case SquareBounds.UPPER_RIGHT_QUADRENT:
			subQuad = upperRightQuad;
			break;
		}
		return subQuad;
	}

	private boolean shouldPassToSubQuad(Point point) {
		if (containsSubQuads()) {
			return true;
		}
		return false;
	}

	private boolean shouldAddAsLeaf() {
		if (!containsSubQuads() && leaves != null && leaves.size() < 2) {
			return true;
		}
		return false;
	}

	private boolean containsSubQuads() {
		return lowerLeftQuad != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaBounds == null) ? 0 : areaBounds.hashCode());
		result = prime * result + ((leaves == null) ? 0 : leaves.hashCode());
		result = prime * result + ((lowerLeftQuad == null) ? 0 : lowerLeftQuad.hashCode());
		result = prime * result + ((lowerRightQuad == null) ? 0 : lowerRightQuad.hashCode());
		result = prime * result + ((upperLeftQuad == null) ? 0 : upperLeftQuad.hashCode());
		result = prime * result + ((upperRightQuad == null) ? 0 : upperRightQuad.hashCode());
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
		QuadNode other = (QuadNode) obj;
		if (areaBounds == null) {
			if (other.areaBounds != null)
				return false;
		} else if (!areaBounds.equals(other.areaBounds))
			return false;
		if (leaves == null) {
			if (other.leaves != null)
				return false;
		} else if (!leaves.equals(other.leaves))
			return false;
		if (lowerLeftQuad == null) {
			if (other.lowerLeftQuad != null)
				return false;
		} else if (!lowerLeftQuad.equals(other.lowerLeftQuad))
			return false;
		if (lowerRightQuad == null) {
			if (other.lowerRightQuad != null)
				return false;
		} else if (!lowerRightQuad.equals(other.lowerRightQuad))
			return false;
		if (upperLeftQuad == null) {
			if (other.upperLeftQuad != null)
				return false;
		} else if (!upperLeftQuad.equals(other.upperLeftQuad))
			return false;
		if (upperRightQuad == null) {
			if (other.upperRightQuad != null)
				return false;
		} else if (!upperRightQuad.equals(other.upperRightQuad))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuadNode [areaBounds=" + areaBounds + ", leaves=" + leaves + ", lowerLeftQuad=" + lowerLeftQuad
				+ ", lowerRightQuad=" + lowerRightQuad + ", upperLeftQuad=" + upperLeftQuad + ", upperRightQuad="
				+ upperRightQuad + "]";
	}

	
}
