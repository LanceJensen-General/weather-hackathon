package com.att.cdo.weather.quadtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.att.cdo.weather.spatial.ImmutablePoint;
import com.att.cdo.weather.spatial.Point;

public class SquareBounds {

	public static final int LOWER_LEFT_QUADRENT = 0, LOWER_RIGHT_QUADRENT = 1, UPPER_LEFT_QUADRENT = 2,
			UPPER_RIGHT_QUADRENT = 3;

	private Double lowerLeftX;
	private Double lowerLeftY;
	private Double upperRightX;
	private Double upperRightY;
	private Double centerX;
	private Double centerY;

	/**
	 * SquareBounds - assumes two or more different points from which a minimum
	 * bounding area can be defined and then squared to have the shortest edge
	 * have the same length as the longest edge.
	 * 
	 * @param points
	 *            - is a list containing at least two unique points.
	 */
	public SquareBounds(List<Point> points) {
		for (Point point : points) {
			if (lowerLeftX == null) {
				lowerLeftX = point.getX();
				lowerLeftY = point.getY();
				upperRightX = point.getX();
				upperRightY = point.getY();
			} else {
				lowerLeftX = Math.min(lowerLeftX, point.getX());
				lowerLeftY = Math.min(lowerLeftY, point.getY());
				upperRightX = Math.max(upperRightX, point.getX());
				upperRightY = Math.max(upperRightY, point.getY());
			}
		}
		calculateAndSetCenter();
		squareBoundsArea();
	}

	/**
	 * SquareBounds is a constructor defining an bounding area via a center
	 * point and a diameter.
	 * 
	 * @param center
	 *            - is the center point of this SquareBounds.
	 * @param radius
	 *            - is half the length of an edge of this SquareBounds.
	 */
	public SquareBounds(Point center, Double radius) {
		this.centerX = center.getX();
		this.centerY = center.getY();
		setExtents(radius);
	}

	private SquareBounds(Double lowerLeftX, Double lowerLeftY, Double upperRightX, Double upperRightY) {
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		this.upperRightX = upperRightX;
		this.upperRightY = upperRightY;
		calculateAndSetCenter();
	}

	/**
	 * getWidth - returns the width of this bounds along the x axis.
	 * 
	 * @return a double denoting the width of this Bounds.
	 */
	public Double getWidth() {
		return upperRightX - lowerLeftX;
	}

	/**
	 * getHeight - returns the height of this bounds along the y axis.
	 * 
	 * @return a Double denoting the height of this Bounds.
	 */
	public Double getHeight() {
		return upperRightY - lowerLeftY;
	}

	/**
	 * quadrateBounds - maps this bounds area to 4 quadrants.
	 * 
	 * @return
	 */
	public Map<Integer, SquareBounds> quadrateBounds() {
		HashMap<Integer, SquareBounds> newQuadrents = new HashMap<Integer, SquareBounds>();
		newQuadrents.put(LOWER_LEFT_QUADRENT, new SquareBounds(lowerLeftX, lowerLeftY, centerX, centerY));
		newQuadrents.put(LOWER_RIGHT_QUADRENT, new SquareBounds(centerX, lowerLeftY, upperRightX, centerY));
		newQuadrents.put(UPPER_LEFT_QUADRENT, new SquareBounds(lowerLeftX, centerY, centerX, upperRightY));
		newQuadrents.put(UPPER_RIGHT_QUADRENT, new SquareBounds(centerX, centerY, upperRightX, upperRightY));
		return newQuadrents;
	}

	/**
	 * getQuadrantFor - point checks for the quadrant that contains this point
	 * from within this boundary
	 * 
	 * @param point
	 *            - is a coordinate to be checked for its associated quadrant.
	 * @return the integer constant associated with this points boundary
	 *         quadrant.
	 */
	public Integer getQuadrantFor(Point point) {
		if (point.getX() < centerX) {
			if (point.getY() < centerY) {
				return SquareBounds.LOWER_LEFT_QUADRENT;
			} else {
				return SquareBounds.UPPER_LEFT_QUADRENT;
			}
		} else {
			if (point.getY() < centerY) {
				return SquareBounds.LOWER_RIGHT_QUADRENT;
			} else {
				return SquareBounds.UPPER_RIGHT_QUADRENT;
			}
		}
	}

	/**
	 * getQuadrantExtentsPoint - takes quadrent information and finds the extents
	 * point opposite of center.
	 * @param quadrent - is an integer constant as defined the SquareBounds class.
	 * @return an ImmutablePoint indicating the extents of the quadrant.
	 */
	public Point getQuadrantExtentsPoint(int quadrent) {
		Point extentsPoint = null;
		switch (quadrent) {
			case SquareBounds.LOWER_LEFT_QUADRENT:
				extentsPoint = new ImmutablePoint(lowerLeftX, lowerLeftY);
				break;
			case SquareBounds.UPPER_RIGHT_QUADRENT:
				extentsPoint = new ImmutablePoint(upperRightX, upperRightY);
				break;
			case SquareBounds.LOWER_RIGHT_QUADRENT:
				extentsPoint = new ImmutablePoint(upperRightX, lowerLeftY);
				break;
			case SquareBounds.UPPER_LEFT_QUADRENT:
				extentsPoint = new ImmutablePoint(lowerLeftX, upperRightY);
				break;	
		}
		return extentsPoint;
	}

	/**
	 * contains - checks to see if this boundry contains the point passed to the
	 * method.
	 * 
	 * @param point
	 *            - is a coordinate to be checked.
	 * @return true if the point is within the extents of the boundary and false
	 *         otherwise.
	 */
	public boolean contains(Point point) {
		if (betweenXBounds(point) && betweenYBounds(point)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centerX == null) ? 0 : centerX.hashCode());
		result = prime * result + ((centerY == null) ? 0 : centerY.hashCode());
		result = prime * result + ((lowerLeftX == null) ? 0 : lowerLeftX.hashCode());
		result = prime * result + ((lowerLeftY == null) ? 0 : lowerLeftY.hashCode());
		result = prime * result + ((upperRightX == null) ? 0 : upperRightX.hashCode());
		result = prime * result + ((upperRightY == null) ? 0 : upperRightY.hashCode());
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
		SquareBounds other = (SquareBounds) obj;
		if (centerX == null) {
			if (other.centerX != null)
				return false;
		} else if (!centerX.equals(other.centerX))
			return false;
		if (centerY == null) {
			if (other.centerY != null)
				return false;
		} else if (!centerY.equals(other.centerY))
			return false;
		if (lowerLeftX == null) {
			if (other.lowerLeftX != null)
				return false;
		} else if (!lowerLeftX.equals(other.lowerLeftX))
			return false;
		if (lowerLeftY == null) {
			if (other.lowerLeftY != null)
				return false;
		} else if (!lowerLeftY.equals(other.lowerLeftY))
			return false;
		if (upperRightX == null) {
			if (other.upperRightX != null)
				return false;
		} else if (!upperRightX.equals(other.upperRightX))
			return false;
		if (upperRightY == null) {
			if (other.upperRightY != null)
				return false;
		} else if (!upperRightY.equals(other.upperRightY))
			return false;
		return true;
	}

	
	
	@Override
	public String toString() {
		return "SquareBounds [lowerLeftX=" + lowerLeftX + ", lowerLeftY=" + lowerLeftY + ", upperRightX=" + upperRightX
				+ ", upperRightY=" + upperRightY + ", centerX=" + centerX + ", centerY=" + centerY + "]";
	}

	/**
	 * squareBoundsArea - insures that the extents of this bounds is a square.
	 */
	private void squareBoundsArea() {
		Double radius = Math.max(this.getHeight(), this.getWidth()) / 2;
		setExtents(radius);
	}

	/**
	 * setExtents - configures the extents of this SquareBounds by using the
	 * center point and a radius.
	 * 
	 * @param radius
	 *            is the distance from the center point used to define this
	 *            square area. It is equivalent to half an edge length.
	 */
	private void setExtents(Double radius) {
		this.lowerLeftX = centerX - radius;
		this.lowerLeftY = centerY - radius;
		this.upperRightX = centerX + radius;
		this.upperRightY = centerY + radius;
	}

	/**
	 * betweenXBounds - is a clarity method for checking if the x value of a
	 * point is bounded by this bounds object.
	 * 
	 * @param point
	 *            - is the point to be checked.
	 * @return true if the x coordinate lies within the bounds extents.
	 */
	private boolean betweenXBounds(Point point) {
		if (lowerLeftX <= point.getX() && point.getX() <= upperRightX) {
			return true;
		}
		return false;
	}

	/**
	 * betweenYBounds - is a clarity method used to make the logic for contains
	 * clearer.
	 * 
	 * @param point
	 *            - is to be checked for being in y bounds.
	 * @return true if within bounds and false otherwise.
	 */
	private boolean betweenYBounds(Point point) {
		if (lowerLeftY <= point.getY() && point.getY() <= upperRightY) {
			return true;
		}
		return false;
	}

	private void calculateAndSetCenter() {
		centerX = (getWidth() / 2) + lowerLeftX;
		centerY = (getHeight() / 2) + lowerLeftY;
	}
	
	

}
