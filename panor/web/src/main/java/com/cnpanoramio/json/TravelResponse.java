package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TravelResponse extends ExceptionResponse {

	private List<Travel> travels = null;

	private Travel travel;
	private TravelSpot spot;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Travel {
		public Travel() {
			super();
		}
		private Long id;
		@JsonProperty("user_id")
		private Long userId;
		private String username;
		
		private UserOpenInfo user;
		
		private List<TravelSpot> spots = new ArrayList<TravelSpot>(0);
		private TravelSpot spot;
		@JsonProperty("create_time")
		private Date createTime;
		@JsonProperty("modify_time")
		private Date modifyTime;
		@JsonProperty("time_start")
		private Date timeStart;
		@JsonProperty("time_end")
		private Date timeEnd;
		private String address;
		private String title;
		private String description;
		
		@JsonProperty("like_count")
		private Integer likeCount;
		
		// 相册封面图片
		@JsonProperty("album_cover")
		private String albumCover;
		
		// 图片数量
		@JsonProperty("photo_size")
		private Integer photoSize;
		
		// 所属消息
		@JsonProperty("message_id")
		private Long messageId;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public List<TravelSpot> getSpots() {
			return spots;
		}
		public void setSpots(List<TravelSpot> spots) {
			this.spots = spots;
		}
		public TravelSpot getSpot() {
			return spot;
		}
		public void setSpot(TravelSpot spot) {
			this.spot = spot;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Date getModifyTime() {
			return modifyTime;
		}
		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}
		public Date getTimeStart() {
			return timeStart;
		}
		public void setTimeStart(Date timeStart) {
			this.timeStart = timeStart;
		}
		public Date getTimeEnd() {
			return timeEnd;
		}
		public void setTimeEnd(Date timeEnd) {
			this.timeEnd = timeEnd;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public Integer getLikeCount() {
			return likeCount;
		}
		public void setLikeCount(Integer likeCount) {
			this.likeCount = likeCount;
		}
		public String getAlbumCover() {
			return albumCover;
		}
		public void setAlbumCover(String albumCover) {
			this.albumCover = albumCover;
		}
		public UserOpenInfo getUser() {
			return user;
		}
		public void setUser(UserOpenInfo user) {
			this.user = user;
		}
		public Integer getPhotoSize() {
			return photoSize;
		}
		public void setPhotoSize(Integer photoSize) {
			this.photoSize = photoSize;
		}
		public Long getMessageId() {
			return messageId;
		}
		public void setMessageId(Long messageId) {
			this.messageId = messageId;
		}
		
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class TravelSpot {
		private Long id;
		@JsonProperty("travel_id")
		private Long travelId;
		private List<PhotoProperties> photos = new ArrayList<PhotoProperties>(0);
		@JsonProperty("create_time")
		private Date createTime;
		@JsonProperty("modify_time")
		private Date modifyTime;
		@JsonProperty("time_start")
		private Date timeStart;
		@JsonProperty("time_end")
		private Date timeEnd;
		private String address;
		private String title;
		private String description;
		@JsonProperty("center_lat")
		private Double centerLat;
		@JsonProperty("center_lng")
		private Double centerLng;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public Long getTravelId() {
			return travelId;
		}
		public void setTravelId(Long travelId) {
			this.travelId = travelId;
		}
		public List<PhotoProperties> getPhotos() {
			return photos;
		}
		public void setPhotos(List<PhotoProperties> photos) {
			this.photos = photos;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Date getModifyTime() {
			return modifyTime;
		}
		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}
		public Date getTimeStart() {
			return timeStart;
		}
		public void setTimeStart(Date timeStart) {
			this.timeStart = timeStart;
		}
		public Date getTimeEnd() {
			return timeEnd;
		}
		public void setTimeEnd(Date timeEnd) {
			this.timeEnd = timeEnd;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Double getCenterLat() {
			return centerLat;
		}
		public void setCenterLat(Double centerLat) {
			this.centerLat = centerLat;
		}
		public Double getCenterLng() {
			return centerLng;
		}
		public void setCenterLng(Double centerLng) {
			this.centerLng = centerLng;
		}
		
	}
	
	public List<Travel> getTravels() {
		return travels;
	}

	public void setTravels(List<Travel> travels) {
		this.travels = travels;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public TravelSpot getSpot() {
		return spot;
	}

	public void setSpot(TravelSpot spot) {
		this.spot = spot;
	}
	
}
