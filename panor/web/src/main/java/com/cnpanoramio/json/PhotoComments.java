package com.cnpanoramio.json;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cnpanoramio.json.CommentResponse.Comment;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoComments {
	
	private Long id;
	
	private int count;
	
	private Collection<Comment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}	

}
