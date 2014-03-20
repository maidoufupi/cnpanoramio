package com.cnpanoramio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "photo_panoramio_index")
public class PhotoPanoramioIndex {

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

	@Embeddable
	public static class PhotoPanoramioIndexPK implements Serializable {
		
		public PhotoPanoramioIndexPK() {
			super();
		}
		
		public PhotoPanoramioIndexPK(int level, Double south, Double west) {
			super();
			this.level = level;
			this.south = south;
			this.west = west;
		}
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -3374178340582472091L;

		@Column(name="level", length = 2)
		private int level;
		
		private Double south;
		private Double west;
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
	            return true;
	        if (!(obj instanceof PhotoPanoramioIndexPK))
	            return false;
	        PhotoPanoramioIndexPK pk = (PhotoPanoramioIndexPK) obj;
	        return pk.getLevel() == this.level && 
	        		pk.getSouth() == this.south &&
	        		pk.getWest() == this.west;
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public Double getSouth() {
			return south;
		}
		public void setSouth(Double south) {
			this.south = south;
		}
		public Double getWest() {
			return west;
		}
		public void setWest(Double west) {
			this.west = west;
		}	
		
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

