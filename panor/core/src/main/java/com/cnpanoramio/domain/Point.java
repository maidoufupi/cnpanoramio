package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Point {
    
	private Double geoLat;
	private Double geoLong;
	
	// altitude
	private Double geoAlti ;
	
	private String address;
	
	public Point() {
		super();
	}
	
	public Point( Double geoLat, Double geoLong) {
		super();
		this.geoLat = geoLat;
		this.geoLong = geoLong;
	}
	
	public Point( Double geoLat, Double geoLong, Double geoAlti) {
		super();
		this.geoLat = geoLat;
		this.geoLong = geoLong;
		this.geoAlti = geoAlti;
	}
	
	@Column(name = "geo_long")
	public Double getGeoLong() {
		return geoLong;
	}
	public void setGeoLong(Double geoLong) {
		this.geoLong = geoLong;
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
