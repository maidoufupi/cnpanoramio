package com.cnpanoramio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PhotoGisIndexPK implements Serializable {
	private static final long serialVersionUID = 4073768409031173807L;
	
	@Column(name="zoom_level", length = 2)
	private int zoomLevel;
	
	private Double lat;
	private Double lng;
	
	public int getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
            return true;
        if (!(obj instanceof PhotoGisIndexPK))
            return false;
        PhotoGisIndexPK pk = (PhotoGisIndexPK) obj;
        return pk.getZoomLevel() == this.zoomLevel && 
        		pk.getLat() == this.lat &&
        		pk.getLng() == this.lng;
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}	
}
