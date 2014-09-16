package com.cnpanoramio.json;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CommentResponse extends ExceptionResponse {

	public static CommentResponse getInstance() {
		return new CommentResponse();
	}
	
	private Comment comment;
	
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Comment {

		private Long id;
		
		private String type;
		
		@JsonProperty("user_id")
		private Long userId;
		
		@JsonProperty("user_avatar")
		private Long userAvatar;
		
		private String username;
		
		private String content;
		
		@JsonProperty("create_date")
		private Date createDate;
		
		@JsonProperty("entity_id")
		private Long entityId;
		
		private Boolean like;
		
		@JsonProperty("like_count")
		private int likeCount;
		
		private List<Comment> comments = null;

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

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Long getEntityId() {
			return entityId;
		}

		public void setEntityId(Long entityId) {
			this.entityId = entityId;
		}

		public Boolean getLike() {
			return like;
		}

		public void setLike(Boolean like) {
			this.like = like;
		}

		public int getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(int likeCount) {
			this.likeCount = likeCount;
		}

		public Long getUserAvatar() {
			return userAvatar;
		}

		public void setUserAvatar(Long userAvatar) {
			this.userAvatar = userAvatar;
		}

		public List<Comment> getComments() {
			return comments;
		}

		public void setComments(List<Comment> comments) {
			this.comments = comments;
		}
		
		
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
