package com.cnpanoramio.domain;

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
@Table(name = "message_queue")
@Deprecated
public class MessageQueue extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4058706989971984596L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "message_id")
	private Message message;
	
	@Column(name="have_read")
	private boolean haveRead;
	
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

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public boolean isHaveRead() {
		return haveRead;
	}

	public void setHaveRead(boolean haveRead) {
		this.haveRead = haveRead;
	}
}
