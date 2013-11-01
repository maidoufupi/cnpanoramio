package com.cnpanoramio.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Tag {
     
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="photo_id")
	private Photo photo;
	
	private String tag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Photo getPicture() {
		return photo;
	}

	public void setPicture(Photo photo) {
		this.photo = photo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
