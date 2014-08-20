package com.cnpanoramio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.MapVendor;

@XmlRootElement
@Entity
@Table(name = "photo_gps")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PhotoGps {
	
//	@EmbeddedId
//	private PhotoGpsPK pk;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Photo photo;
	
	@Enumerated(EnumType.STRING)
	private MapVendor vendor;
	
	private Point point;
	
	public PhotoGps() {
		super();
	}
	
	public PhotoGps(Photo photo, MapVendor vendor) {
		super();
		this.photo = photo;
		this.vendor = vendor;
	}
	
	public PhotoGps(Photo photo, MapVendor vendor, Point point) {
		super();
		this.photo = photo;
		this.vendor = vendor;
		this.point = point;
	}
	
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public MapVendor getVendor() {
		return vendor;
	}

	public void setVendor(MapVendor vendor) {
		this.vendor = vendor;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
}


