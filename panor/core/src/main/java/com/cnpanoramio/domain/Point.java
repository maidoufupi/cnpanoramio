package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Embeddable
@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Point {
	
	@Column(name = "lat")
	private Double lat;
	@Column(name = "lng")
	private Double lng;
	
	// altitude
	@Column(name = "alt")
	private Double alt;
	
	@Column(name = "address")
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getAlt() {
		return alt;
	}

	public void setAlt(Double alt) {
		this.alt = alt;
	}

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
