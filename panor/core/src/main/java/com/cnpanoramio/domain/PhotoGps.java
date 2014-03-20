package com.cnpanoramio.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	@EmbeddedId
	private PhotoGpsPK pk;
	
	private Point gps;
	
	public PhotoGpsPK getPk() {
		return pk;
	}

	public void setPk(PhotoGpsPK pk) {
		this.pk = pk;
	}	
	
	public Point getGps() {
		return gps;
	}

	public void setGps(Point gps) {
		this.gps = gps;
	}

	@XmlRootElement
	@Embeddable
	public static class PhotoGpsPK implements Serializable {
		
		public PhotoGpsPK() {
		}
		
		public PhotoGpsPK(Long photoId, MapVendor mapVendor) {
			super();
			this.photoId = photoId;
			this.mapVendor = mapVendor;
		}
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7688224326101899701L;

		@Column(name = "photo_id")
		@JsonProperty("photo_id")
		private Long photoId;

		@Enumerated(EnumType.STRING)
		@Column(name = "vendor")
		@JsonProperty("vendor")
		private MapVendor mapVendor;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((mapVendor == null) ? 0 : mapVendor.hashCode());
			result = prime * result
					+ ((photoId == null) ? 0 : photoId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PhotoGpsPK other = (PhotoGpsPK) obj;
			if (mapVendor != other.mapVendor)
				return false;
			if (photoId == null) {
				if (other.photoId != null)
					return false;
			} else if (!photoId.equals(other.photoId))
				return false;
			return true;
		}

		public Long getPhotoId() {
			return photoId;
		}

		public void setPhotoId(Long photoId) {
			this.photoId = photoId;
		}

		public MapVendor getMapVendor() {
			return mapVendor;
		}

		public void setMapVendor(MapVendor mapVendor) {
			this.mapVendor = mapVendor;
		}
		
		
	}
}


