package com.cnpanoramio.json;

import java.util.Date;

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
		
		@JsonProperty("user_id")
		private Long userId;
		
		@JsonProperty("user_avatar")
		private Long userAvatar;
		
		private String username;
		
		private String content;
		
		@JsonProperty("create_time")
		private Date createTime;
		
		@JsonProperty("photo_id")
		private Long photoId;
		
		private Boolean like;
		
		@JsonProperty("like_count")
		private Integer likeCount;

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
		
		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Long getPhotoId() {
			return photoId;
		}

		public void setPhotoId(Long photoId) {
			this.photoId = photoId;
		}

		public Boolean getLike() {
			return like;
		}

		public void setLike(Boolean like) {
			this.like = like;
		}

		public Integer getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(Integer likeCount) {
			this.likeCount = likeCount;
		}

		public Long getUserAvatar() {
			return userAvatar;
		}

		public void setUserAvatar(Long userAvatar) {
			this.userAvatar = userAvatar;
		}
		
		
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
