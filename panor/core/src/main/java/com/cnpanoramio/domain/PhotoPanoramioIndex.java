package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "photo_panoramio_index")
public class PhotoPanoramioIndex {

	@EmbeddedId 
	private PhotoPanoramioIndexPK pk;
	
	@ManyToOne
	@JoinColumn(name = "photo_id")
	private Photo photo;
	
	@Column(name = "rating")
	private Integer rating;
	
	@Column(name = "photo_rating")
	private Integer photoRating;
	
	@Column(name = "big_photo")
	private Boolean bigPhoto;
	
	public PhotoPanoramioIndexPK getPk() {
		return pk;
	}

	public void setPk(PhotoPanoramioIndexPK pk) {
		this.pk = pk;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getPhotoRating() {
		return photoRating;
	}

	public void setPhotoRating(Integer photoRating) {
		this.photoRating = photoRating;
	}

	public Boolean getBigPhoto() {
		return bigPhoto;
	}

	public void setBigPhoto(Boolean bigPhoto) {
		this.bigPhoto = bigPhoto;
	}
	
	
}

