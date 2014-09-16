package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.json.CommentResponse.Comment;
import com.cnpanoramio.json.TravelResponse.Travel;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MessageResponse extends ExceptionResponse {

	private Message message;
	
	private List<Message> messages;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public static class Message {
		
		private Long id;
		
		@JsonProperty("user")
		private UserOpenInfo user;
		
		private com.cnpanoramio.domain.Message.MessageType type;
		
		@JsonProperty("create_date")
		private Date createDate;
		
		private PhotoProperties photo;
		
		private Travel travel;
		
		private String content;
				
		private List<String> tags = new ArrayList<String>(0);
		
		private List<Comment> comments = new ArrayList<Comment>(0);
		
		// 多少个赞
		@JsonProperty("like_count")
		private int likeCount;
		
		// 用户本人是否赞
		private Boolean like;
		
		@JsonProperty("share_message")
		private Message shareMessage;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public UserOpenInfo getUser() {
			return user;
		}

		public void setUser(UserOpenInfo user) {
			this.user = user;
		}

		public com.cnpanoramio.domain.Message.MessageType getType() {
			return type;
		}

		public void setType(com.cnpanoramio.domain.Message.MessageType type) {
			this.type = type;
		}

		public PhotoProperties getPhoto() {
			return photo;
		}

		public void setPhoto(PhotoProperties photo) {
			this.photo = photo;
		}

		public Travel getTravel() {
			return travel;
		}

		public void setTravel(Travel travel) {
			this.travel = travel;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<String> getTags() {
			return tags;
		}

		public void setTags(List<String> tags) {
			this.tags = tags;
		}

		public List<Comment> getComments() {
			return comments;
		}

		public void setComments(List<Comment> comments) {
			this.comments = comments;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
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

		public Message getShareMessage() {
			return shareMessage;
		}

		public void setShareMessage(Message shareMessage) {
			this.shareMessage = shareMessage;
		}		
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
}
