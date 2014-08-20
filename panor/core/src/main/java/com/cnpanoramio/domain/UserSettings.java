package com.cnpanoramio.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.appfuse.model.User;

import com.cnpanoramio.MapVendor;

@XmlRootElement(name = "UserSettings")
@Entity
@Table(name = "user_settings")
public class UserSettings {
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private User user;
	
	@Id 
    private Long id;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="user_tag")
	private Set<Tag> tags = new HashSet<Tag>(0);
	
	@OneToMany(mappedBy="user")
//	@JoinTable(name="user_travel")
	private List<Travel> travels = new ArrayList<Travel>(0);
	
	// 地图供应商
//	@XmlAttribute(name = "map_vendor")
	@Column(name = "map_vendor")
	private MapVendor mapVendor;
			
	// 昵称
	@Column(name = "name")
	private String name;
	
	// 网址名称，它将会出现在您的网址中
	@Column(name = "url_name")
//	@XmlAttribute(name = "url_name")
	private String urlName; 
	
	// 您的网页
	@Column(name = "homepage_url")
//	@XmlAttribute(name = "homepage_url")
	private String homepageUrl;
	
	// 写一些关于您自己
	@Column(name = "description", length = 500)
	private String description;
	
	// 我的照片有新的评论
	@Column(name = "alert_comments")
//	@XmlAttribute(name = "alert_comments")
	private Boolean alertComments;
	
	// 我订阅的用户上传了新照片
	@Column(name = "alert_photos")
//	@XmlAttribute(name = "alert_photos")
	private Boolean alertPhotos;
	
	// 新的和更新后的群组加入邀请
	@Column(name = "alert_group_invitations")
//	@XmlAttribute(name = "alert_group_invitations")
	private Boolean alertGroupInvitations;
	
	// 启用私密信息
	@Column(name = "private_messages")
//	@XmlAttribute(name = "private_messages")
	private Boolean privateMessages;
	
	// 保留所有权利
	@Column(name = "all_rights_reserved")
//	@XmlAttribute(name = "all_rights_reserved")
	private Boolean allRightsReserved;
	
	// 是否允许用作商业用途？
	@Column(name = "commercial_use")
//	@XmlAttribute(name = "commercial_use")
	private Boolean commercialUse;
	
	// 是否允许修改？
	@Column(name = "modify")
	private Boolean modify;
	
	// 头像图片的ID
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="avatar")
	private Avatar avatar;
	
	// follower
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name="user_follower",
			joinColumns = @JoinColumn(name="id"),
	        inverseJoinColumns = @JoinColumn(name="follower_id")
	)
	private Set<User> follower = new HashSet<User>(0);
	
	// following
//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinTable(name="user_following")
//	private Set<User> following = new HashSet<User>(0);
	
	// recycle
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
//	@JoinTable(name="user_recycle")
	private Set<Recycle> recycle = new HashSet<Recycle>(0);
	
	// following
	@OneToMany(mappedBy="owner", fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<Circle> circles = new HashSet<Circle>(0);
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, orphanRemoval=true)
//	@JoinTable(name="user_message")
	private List<MessageQueue> messageQueue = new ArrayList<MessageQueue>(0);
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.id = user.getId();
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

	public Long getId() {
		return id;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Travel> getTravels() {
		return travels;
	}

	public void setTravels(List<Travel> travels) {
		this.travels = travels;
	}

	public Set<Circle> getCircles() {
		return circles;
	}

	public void setCircles(Set<Circle> circles) {
		this.circles = circles;
	}

	public List<MessageQueue> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(List<MessageQueue> messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Set<Recycle> getRecycle() {
		return recycle;
	}
	
	public Set<User> getFollower() {
		return follower;
	}

	public void setFollower(Set<User> follower) {
		this.follower = follower;
	}

	@SuppressWarnings("unused")
	private void setRecycle(Set<Recycle> recycle) {
		this.recycle = recycle;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSettings other = (UserSettings) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}	
		
}