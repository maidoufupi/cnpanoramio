package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Point {
    
	private Double geoLat;
	private Double geoLng;
	
	// altitude
	private Double geoAlti ;
	
	private String address;
	
	public Point() {
		super();
	}
	
	public Point( Double geoLat, Double geoLng) {
		super();
		this.geoLat = geoLat;
		this.geoLng = geoLng;
	}
	
	public Point( Double geoLat, Double geoLng, Double geoAlti) {
		super();
		this.geoLat = geoLat;
		this.geoLng = geoLng;
		this.geoAlti = geoAlti;
	}
	
	@Column(name = "geo_long")
	public Double getGeoLng() {
		return geoLng;
	}
	public void setGeoLng(Double geoLong) {
		this.geoLng = geoLong;
	}
	
	@Column(name = "geo_lat")
	public Double getGeoLat() {
		return geoLat;
	}
	public void setGeoLat(Double geoLat) {
		this.geoLat = geoLat;
	}
	
	@Column(name = "geo_alti")
	public Double getGeoAlti() {
		return geoAlti;
	}
	public void setGeoAlti(Double geoAlti) {
		this.geoAlti = geoAlti;
	}

	@Column(name = "geo_address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
