package com.att.cdo.weather.domain;

import com.att.cdo.weather.spatial.Point;

public class WeatherStation implements Point{
	
	private String usaf;
	private String wban;
	private String stationName;
	private Double tempreture;
	private Double latitude;
	private Double longitude;
	private Double radiansY;
	private Double radiansX;
	private Integer graphId;
	
	public String getUsaf() {
		return usaf;
	}
	public void setUsaf(String usaf) {
		this.usaf = usaf;
	}
	public String getWban() {
		return wban;
	}
	public void setWban(String wban) {
		this.wban = wban;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getTempreture() {
		return tempreture;
	}
	public void setTempreture(Double tempreture) {
		this.tempreture = tempreture;
	}
	public Integer getGraphId() {
		return graphId;
	}
	public void setGraphId(Integer graphId) {
		this.graphId = graphId;
	}
	@Override
	public Double getX() {
		if(radiansX == null) {
			if(this.longitude != null) {
				radiansX = longitude * Math.PI / 180;
			}
		}
		return radiansX;
	}
	@Override
	public Double getY() {
		if(radiansY == null) {
			if(this.latitude != null) {
				radiansY = latitude * Math.PI / 180;
			}
		}
		return radiansY;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graphId == null) ? 0 : graphId.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((radiansX == null) ? 0 : radiansX.hashCode());
		result = prime * result + ((radiansY == null) ? 0 : radiansY.hashCode());
		result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
		result = prime * result + ((tempreture == null) ? 0 : tempreture.hashCode());
		result = prime * result + ((usaf == null) ? 0 : usaf.hashCode());
		result = prime * result + ((wban == null) ? 0 : wban.hashCode());
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
		WeatherStation other = (WeatherStation) obj;
		if (graphId == null) {
			if (other.graphId != null)
				return false;
		} else if (!graphId.equals(other.graphId))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (radiansX == null) {
			if (other.radiansX != null)
				return false;
		} else if (!radiansX.equals(other.radiansX))
			return false;
		if (radiansY == null) {
			if (other.radiansY != null)
				return false;
		} else if (!radiansY.equals(other.radiansY))
			return false;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		if (tempreture == null) {
			if (other.tempreture != null)
				return false;
		} else if (!tempreture.equals(other.tempreture))
			return false;
		if (usaf == null) {
			if (other.usaf != null)
				return false;
		} else if (!usaf.equals(other.usaf))
			return false;
		if (wban == null) {
			if (other.wban != null)
				return false;
		} else if (!wban.equals(other.wban))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WeatherStation [usaf=" + usaf + ", wban=" + wban + ", stationName=" + stationName + ", tempreture="
				+ tempreture + ", latitude=" + latitude + ", longitude=" + longitude + ", radiansY=" + radiansY
				+ ", radiansX=" + radiansX + ", graphId=" + graphId + "]";
	}
	
	
	
	
}
