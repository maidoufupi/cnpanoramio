package com.cnpanoramio.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.appfuse.model.User;


@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4705603625193289640L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private CommentType type;
	
	@ManyToOne
	@JoinColumn(name="photo_id")
	private Photo photo;
	
	@ManyToOne
	@JoinColumn(name="travel_id")
	private Travel travel;
	
	@ManyToOne
	@JoinColumn(name="comment_id")
	private Comment comment;
		
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(length=1000)
	private String content;
		
	@OneToMany(mappedBy="comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Like> likes = new HashSet<Like>(0);
	
	@OneToMany(mappedBy="comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<Comment>(0);

	public enum CommentType {
		photo, travel, comment
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public CommentType getType() {
		return type;
	}

	public void setType(CommentType type) {
		this.type = type;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	
}
