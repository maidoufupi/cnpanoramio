package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alt == null) ? 0 : alt.hashCode());
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
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
		Point other = (Point) obj;
		if (alt == null) {
			if (other.alt != null)
				return false;
		} else if (!alt.equals(other.alt))
			return false;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		return true;
	}	
	
}
