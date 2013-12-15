package com.cnpanoramio.service.json;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BoundSize")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundSize {
	private Double boundNELat;
	private Double boundNELng;
	private Double boundSWLat;
	private Double boundSWLng;

	private Integer sizeX;
	private Integer sizeY;
	
	public Double getBoundNELat() {
		return boundNELat;
	}
	public void setBoundNELat(Double boundNELat) {
		this.boundNELat = boundNELat;
	}
	public Double getBoundNELng() {
		return boundNELng;
	}
	public void setBoundNELng(Double boundNELng) {
		this.boundNELng = boundNELng;
	}
	public Double getBoundSWLat() {
		return boundSWLat;
	}
	public void setBoundSWLat(Double boundSWLat) {
		this.boundSWLat = boundSWLat;
	}
	public Double getBoundSWLng() {
		return boundSWLng;
	}
	public void setBoundSWLng(Double boundSWLng) {
		this.boundSWLng = boundSWLng;
	}
	public Integer getSizeX() {
		return sizeX;
	}
	public void setSizeX(Integer sizeX) {
		this.sizeX = sizeX;
	}
	public Integer getSizeY() {
		return sizeY;
	}
	public void setSizeY(Integer sizeY) {
		this.sizeY = sizeY;
	}
}