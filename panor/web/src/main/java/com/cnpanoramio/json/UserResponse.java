package com.cnpanoramio.json;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserResponse {

	public enum Status {
		/**
		 * 一切正常
		 */
		OK,
		/**
		 * 图片ID格式错误
		 */
		ID_FORMAT_ERROR,
		/**
		 * 找不到对象
		 */
		NO_ENTITY,
		/**
		 * 未授权
		 */
		NO_AUTHORIZE,
		/**
		 * 出现异常
		 */
		EXCEPTION
	}
	
	private String status;
	
	private String info;
	
	@JsonProperty("photo_info")
	private PhotoInfo photoInfo;
	
	private Collection<PhotoProperties> photos;
	
	@JsonProperty("open_info")
	private UserOpenInfo openInfo;
	
	public static class PhotoInfo {
		
		public PhotoInfo() {
			super();
		}
		
		public PhotoInfo(int photoCount) {
			this.photoCount = photoCount;
		}
		
		public PhotoInfo(int photoCount, int photoNum) {
			this.photoCount = photoCount;
			this.photoNum = photoNum;
		}
		
		@JsonProperty("photo_count")
		private int photoCount;
		
		@JsonProperty("photo_num")
		private int photoNum;
		
		public int getPhotoCount() {
			return photoCount;
		}

		public void setPhotoCount(int photoCount) {
			this.photoCount = photoCount;
		}

		public int getPhotoNum() {
			return photoNum;
		}

		public void setPhotoNum(int photoNum) {
			this.photoNum = photoNum;
		}	
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Collection<PhotoProperties> getPhotos() {
		return photos;
	}

	public void setPhotos(Collection<PhotoProperties> photos) {
		this.photos = photos;
	}

	public UserOpenInfo getOpenInfo() {
		return openInfo;
	}

	public void setOpenInfo(UserOpenInfo openInfo) {
		this.openInfo = openInfo;
	}

	public PhotoInfo getPhotoInfo() {
		return photoInfo;
	}

	public void setPhotoInfo(PhotoInfo photoInfo) {
		this.photoInfo = photoInfo;
	}

	
	
}
