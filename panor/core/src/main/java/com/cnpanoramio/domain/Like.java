package com.cnpanoramio.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.appfuse.model.User;

@Entity
@Table(name = "likes")
public class Like {

	public static int C_TYPE_PHOTO = 1;
	public static int C_TYPE_COMMENT = 2;
	public static int C_TYPE_TRAVEL = 3;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// 赞的类型，赞的什么
	private Integer type;
	
	@ManyToOne
	@JoinColumn(name="user_id")
    private User user;

	@ManyToOne
	@JoinColumn(name="photo_id")
	private Photo photo;
	
	@ManyToOne
	@JoinColumn(name="comment_id")
	private Comment comment;
	
	@ManyToOne
	@JoinColumn(name="travel_id")
	private Travel travel;
	
	@Column(name="create_time")
	private Calendar createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}	
	
}
