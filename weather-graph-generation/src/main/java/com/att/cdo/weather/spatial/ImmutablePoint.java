package com.att.cdo.weather.spatial;

/**
 * ImmutablePoint - was designed to be a holder of point coordinates that
 * can not be changed.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 */
public class ImmutablePoint implements Point {

	private double x;
	private double y;
	
	public ImmutablePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Double getX() {
		return x;
	}

	@Override
	public Double getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ImmutablePoint other = (ImmutablePoint) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImmutablePoint [x=" + x + ", y=" + y + "]";
	}
	
}
