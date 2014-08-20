package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Embeddable
@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Point {
    
	public double lat;
	public double lng;
	
	// altitude
	public double alt;
	
	private String address;
	
	public Point() {
		super();
	}
	
	public Point( double geoLat, double geoLng) {
		super();
		this.lat = geoLat;
		this.lng = geoLng;
	}
	
	public Point( double geoLat, double geoLng, double geoAlti) {
		super();
		this.lat = geoLat;
		this.lng = geoLng;
		this.alt = geoAlti;
	}
	
	
	public double getLng() {
		return lng;
	}
	
	@Column(name = "lng", nullable = true)
	public void setLng(double geoLong) {
		this.lng = geoLong;
	}
	
	
	public double getLat() {
		return lat;
	}
	
	@Column(name = "lat", nullable = true)
	public void setLat(double geoLat) {
		this.lat = geoLat;
	}
	
	public double getAlt() {
		return alt;
	}
	
	@Column(name = "alt", nullable = true)
	public void setAlt(double geoAlti) {
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
		long temp;
		temp = Double.doubleToLongBits(alt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
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
		Point other = (Point) obj;
		if (Double.doubleToLongBits(alt) != Double.doubleToLongBits(other.alt))
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		return true;
	}

	
}
