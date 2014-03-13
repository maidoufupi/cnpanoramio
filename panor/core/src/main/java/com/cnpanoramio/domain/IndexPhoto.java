package com.cnpanoramio.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "index_photo")
public class IndexPhoto {

	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	private Photo photo;
	
	@Id
    private Long id;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.id = photo.getId();
		this.photo = photo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
