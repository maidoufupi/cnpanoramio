package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Deprecated
@Entity
@Table(name = "photo_gisindex")
public class PhotoGisIndex {

	@EmbeddedId
	private PhotoGisIndexPK pk;
    
	@Column(name = "photo_id")
	private Long photoId;
	
	public PhotoGisIndexPK getPk() {
		return pk;
	}

	public void setPk(PhotoGisIndexPK pk) {
		this.pk = pk;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	
	
	
}
