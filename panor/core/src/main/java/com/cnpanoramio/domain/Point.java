package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Point {
    
	private Double lat;
	private Double lng;
	
	// altitude
	private Double alt;
	
	private String address;
	
	public Point() {
		super();
	}
	
	public Point( Double geoLat, Double geoLng) {
		super();
		this.lat = geoLat;
		this.lng = geoLng;
	}
	
	public Point( Double geoLat, Double geoLng, Double geoAlti) {
		super();
		this.lat = geoLat;
		this.lng = geoLng;
		this.alt = geoAlti;
	}
	
	@Column(name = "lng")
	public Double getLng() {
		return lng;
	}
	public void setLng(Double geoLong) {
		this.lng = geoLong;
	}
	
	@Column(name = "lat")
	public Double getLat() {
		return lat;
	}
	public void setLat(Double geoLat) {
		this.lat = geoLat;
	}
	
	@Column(name = "alt")
	public Double getAlt() {
		return alt;
	}
	public void setAlt(Double geoAlti) {
		this.alt = geoAlti;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
