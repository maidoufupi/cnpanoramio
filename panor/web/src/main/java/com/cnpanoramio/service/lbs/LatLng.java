package com.cnpanoramio.service.lbs;

public class LatLng {

	public double lat;
	public double lng;

	// Construct a point at the origin
	public LatLng() {
		this.lat = 0;
		this.lng = 0;
	}

	// Construct a point at the specified location (lat, lng)
	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

}
