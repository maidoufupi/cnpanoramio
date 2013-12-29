package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpanoramio.MapVendor;

@Entity
@Table(name = "latlng")
public class LatLng {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "lat")
	private Double lat;
	
	@Column(name = "lng")
	private Double lng;
	
	@Column(name = "alt")
	private Double alt;
	
	@Column(name = "vendor")
	private MapVendor mapVendor;
	
	@Column(name = "address")
	private String address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public MapVendor getMapVendor() {
		return mapVendor;
	}

	public void setMapVendor(MapVendor mapVendor) {
		this.mapVendor = mapVendor;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
