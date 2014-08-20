package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.appfuse.model.User;


@Entity
@Table(name = "message")
public class Message extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5101945801466130351L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private MessageType type;
	
	@Column(name="entity_id")
	private Long entityId;
	
	@ManyToOne
	private User user;	
	
//	@ManyToOne
////	@JoinColumn(name = "photo_id")
//	private Photo photo;
//	
//	@ManyToOne
////	@JoinColumn(name = "travel_id")
//	private Travel travel;
		
	public enum MessageType {
		photo, travel
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

//	public Photo getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(Photo photo) {
//		this.photo = photo;
//	}
//
//	public Travel getTravel() {
//		return travel;
//	}
//
//	public void setTravel(Travel travel) {
//		this.travel = travel;
//	}	
	
}
