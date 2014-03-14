package com.cnpanoramio.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserOpenInfo {

	private Long id;
	
	private String name;
	
	private Long avatar;
	
	private int photoCount;

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
	
}
