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
	
	
	
}
