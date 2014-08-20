package com.cnpanoramio.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.json.CommentResponse.Comment;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MessageResponse extends ExceptionResponse {

	private List<Message> messages;
	
	public static class Message {
		
		@JsonProperty("user")
		private UserOpenInfo user;
		
		private com.cnpanoramio.domain.Message.MessageType type;
		
		private List<PhotoProperties> photos = new ArrayList<PhotoProperties>(0);
		
		private String content;
		
		private List<String> tags = new ArrayList<String>(0);
		
		private List<Comment> comments = new ArrayList<Comment>(0);

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

		public List<PhotoProperties> getPhotos() {
			return photos;
		}

		public void setPhotos(List<PhotoProperties> photos) {
			this.photos = photos;
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
		
		
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
}
