package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "photo_latest_index")
public class PhotoLatestIndex {

	@EmbeddedId
	private PhotoPanoramioIndexPK pk;
	
	@Column(name = "photo_id")
	private Long photoId;
	
	@Column(name = "rating")
	private Integer rating;
	
	@Column(name = "photo_rating")
	private Integer photoRating;
	
	public PhotoPanoramioIndexPK getPk() {
		return pk;
	}

	public void setPk(PhotoPanoramioIndexPK pk) {
		this.pk = pk;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
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
	
	
}

