package com.cnpanoramio.json;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.MapVendor;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserResponse extends ExceptionResponse {
	
	@JsonProperty("photo_info")
	private PhotoInfo photoInfo;
	
	private Collection<PhotoProperties> photos;
	
	@JsonProperty("open_info")
	private UserOpenInfo openInfo;
	
	private UserSettings settings;
	
	private List<Circle> circles;
	
	private Circle circle;
	
	private List<UserOpenInfo> followers;
	
	// 系统推荐关注
	private List<UserOpenInfo> follow;
	
	public static class PhotoInfo {
		
		public PhotoInfo() {
			super();
		}
		
		public PhotoInfo(Long photoCount) {
			this.photoCount = photoCount;
		}
		
		public PhotoInfo(Long photoCount, int photoNum) {
			this.photoCount = photoCount;
			this.photoNum = photoNum;
		}
		
		@JsonProperty("photo_count")
		private Long photoCount;
		
		@JsonProperty("photo_num")
		private int photoNum;
		
		public Long getPhotoCount() {
			return photoCount;
		}

		public void setPhotoCount(Long photoCount) {
			this.photoCount = photoCount;
		}

		public int getPhotoNum() {
			return photoNum;
		}

		public void setPhotoNum(int photoNum) {
			this.photoNum = photoNum;
		}	
	}
	
	
	
	public static class Circle {
		private Long id;
		private String name;
		private Set<UserOpenInfo> users = new HashSet<UserOpenInfo>(0);
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Set<UserOpenInfo> getUsers() {
			return users;
		}
		public void setUsers(Set<UserOpenInfo> users) {
			this.users = users;
		}
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

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}

	public List<Circle> getCircles() {
		return circles;
	}

	public void setCircles(List<Circle> circles) {
		this.circles = circles;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public List<UserOpenInfo> getFollowers() {
		return followers;
	}

	public void setFollowers(List<UserOpenInfo> followers) {
		this.followers = followers;
	}

	public List<UserOpenInfo> getFollow() {
		return follow;
	}

	public void setFollow(List<UserOpenInfo> follow) {
		this.follow = follow;
	}
	
}
