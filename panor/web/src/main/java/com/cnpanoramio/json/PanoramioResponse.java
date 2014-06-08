package com.cnpanoramio.json;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PanoramioResponse extends ExceptionResponse {
	
	private List<PhotoPanoramio> photos;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class PhotoPanoramio {
		
		@JsonProperty("photo_id")
		private Long photoId;
		
		// gps address info
		private Double lat;
		private Double lng;
		private Double alt;
		private String address;
		
		// photo info
		private Integer rating;
		private String title;
		@JsonProperty("create_date")
		private Date createDate;
		
		// owner info
		@JsonProperty("user_id")
		private Long userId;
		private String username;
		public Long getPhotoId() {
			return photoId;
		}
		public void setPhotoId(Long photoId) {
			this.photoId = photoId;
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
		public Double getAlt() {
			return alt;
		}
		public void setAlt(Double alt) {
			this.alt = alt;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Integer getRating() {
			return rating;
		}
		public void setRating(Integer rating) {
			this.rating = rating;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}


	public List<PhotoPanoramio> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoPanoramio> photos) {
		this.photos = photos;
	}
	
	
}
