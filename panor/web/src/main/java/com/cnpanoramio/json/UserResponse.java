package com.cnpanoramio.json;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.UserSettings;

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
	
	private Settings settings;
	
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
	
	public static class Settings {
		
		@JsonProperty("user_id") 
	    private Long userId;
		
		// 地图供应商
		@JsonProperty("map_vendor")
		private MapVendor mapVendor;
				
		// 昵称
		@JsonProperty("name")
		private String name;
		
		// 网址名称，它将会出现在您的网址中
		@JsonProperty("url_name")
		private String urlName; 
		
		// 您的网页
		@JsonProperty("homepage_url")
		private String homepageUrl;
		
		// 写一些关于您自己
		@JsonProperty("description")
		private String description;
		
		// 我的照片有新的评论
		@JsonProperty("alert_comments")
		private Boolean alertComments;
		
		// 我订阅的用户上传了新照片
		@JsonProperty("alert_photos")
		private Boolean alertPhotos;
		
		// 新的和更新后的群组加入邀请
		@JsonProperty("alert_group_invitations")
		private Boolean alertGroupInvitations;
		
		// 启用私密信息
		@JsonProperty("private_messages")
		private Boolean privateMessages;
		
		// 保留所有权利
		@JsonProperty("all_rights_reserved")
		private Boolean allRightsReserved;
		
		// 是否允许用作商业用途？
		@JsonProperty("commercial_use")
		private Boolean commercialUse;
		
		// 是否允许修改？
		@Column(name = "modify")
		private Boolean modify;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public MapVendor getMapVendor() {
			return mapVendor;
		}

		public void setMapVendor(MapVendor mapVendor) {
			this.mapVendor = mapVendor;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrlName() {
			return urlName;
		}

		public void setUrlName(String urlName) {
			this.urlName = urlName;
		}

		public String getHomepageUrl() {
			return homepageUrl;
		}

		public void setHomepageUrl(String homepageUrl) {
			this.homepageUrl = homepageUrl;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Boolean getAlertComments() {
			return alertComments;
		}

		public void setAlertComments(Boolean alertComments) {
			this.alertComments = alertComments;
		}

		public Boolean getAlertPhotos() {
			return alertPhotos;
		}

		public void setAlertPhotos(Boolean alertPhotos) {
			this.alertPhotos = alertPhotos;
		}

		public Boolean getAlertGroupInvitations() {
			return alertGroupInvitations;
		}

		public void setAlertGroupInvitations(Boolean alertGroupInvitations) {
			this.alertGroupInvitations = alertGroupInvitations;
		}

		public Boolean getPrivateMessages() {
			return privateMessages;
		}

		public void setPrivateMessages(Boolean privateMessages) {
			this.privateMessages = privateMessages;
		}

		public Boolean getAllRightsReserved() {
			return allRightsReserved;
		}

		public void setAllRightsReserved(Boolean allRightsReserved) {
			this.allRightsReserved = allRightsReserved;
		}

		public Boolean getCommercialUse() {
			return commercialUse;
		}

		public void setCommercialUse(Boolean commercialUse) {
			this.commercialUse = commercialUse;
		}

		public Boolean getModify() {
			return modify;
		}

		public void setModify(Boolean modify) {
			this.modify = modify;
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

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	
	
}
