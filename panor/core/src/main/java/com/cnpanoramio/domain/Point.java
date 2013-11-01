package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Point {

	private Double geoLong;
	
	private Double geoLat;
	// altitude
	private Double geoAlti ;
	
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
}
