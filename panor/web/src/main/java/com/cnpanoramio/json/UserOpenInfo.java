package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.TravelResponse.Travel;


@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserOpenInfo {

	private Long id;
	
	// 系统用户名
	private String username;
		
	// 昵称
	private String name;
	
	// 头像
	private Long avatar;
	
	// 描述
	private String description;
	
	// 拥有的图片总数
	@JsonProperty("photo_count")
	private Long photoCount;
	
	// 其图片被查看总数
	@JsonProperty("photo_views")
	private Integer photoViews;

	// 其图片被加星总数
	@JsonProperty("photo_favorites")
	private Integer photoFavorites;
	
	// 其所有标签
	private Set<String> tags = new HashSet<String>(0);
	
	// 其所有travel
	private List<Travel> travels = new ArrayList<Travel>(0);
	
	@JsonProperty("follow")
	private Boolean follow;
	
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

	public Long getAvatar() {
		return avatar;
	}

	public void setAvatar(Long avatar) {
		this.avatar = avatar;
	}

	public Long getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(Long photoCount) {
		this.photoCount = photoCount;
	}

	public Integer getPhotoViews() {
		return photoViews;
	}

	public void setPhotoViews(Integer photoViews) {
		this.photoViews = photoViews;
	}

	public Integer getPhotoFavorites() {
		return photoFavorites;
	}

	public void setPhotoFavorites(Integer photoFavorites) {
		this.photoFavorites = photoFavorites;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public List<Travel> getTravels() {
		return travels;
	}

	public void setTravels(List<Travel> travels) {
		this.travels = travels;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getFollow() {
		return follow;
	}

	public void setFollow(Boolean follow) {
		this.follow = follow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
	
}
