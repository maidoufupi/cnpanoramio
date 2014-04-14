package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserOpenInfo {

	private Long id;
	
	private String name;
	
	private Long avatar;
	
	@JsonProperty("photo_count")
	private int photoCount;
	
	@JsonProperty("photo_views")
	private int photoViews;

	@JsonProperty("photo_favorites")
	private int photoFavorites;
	
	private List<String> tags = new ArrayList<String>(0);
	
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

	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	public int getPhotoViews() {
		return photoViews;
	}

	public void setPhotoViews(int photoViews) {
		this.photoViews = photoViews;
	}

	public int getPhotoFavorites() {
		return photoFavorites;
	}

	public void setPhotoFavorites(int photoFavorites) {
		this.photoFavorites = photoFavorites;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}	
	
}
