package com.cnpanoramio.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "tag")
public class Tag {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	
	@ManyToMany
	@JoinTable(name="tag_user")
	private Set<UserSettings> users = new HashSet<UserSettings>(0);
	
	@ManyToMany
	@JoinTable(name="tag_photo")
	private Set<Photo> photo = new HashSet<Photo>(0);;
	
	private String tag;
	
	public Tag(){};
	
	public Tag(String tag){
		this.tag = tag;
	};

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Photo> getPhoto() {
		return photo;
	}

	public void setPhoto(Set<Photo> photo) {
		this.photo = photo;
	}	

	public Set<UserSettings> getUsers() {
		return users;
	}

	public void setUsers(Set<UserSettings> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		Tag other = (Tag) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
	
}
