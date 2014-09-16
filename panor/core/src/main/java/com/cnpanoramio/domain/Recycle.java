package com.cnpanoramio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recycle")
public class Recycle extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796390836280086933L;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserSettings user;

	@Column(name="recy_type")
	@Enumerated(EnumType.STRING)
	private RecycleType recyType;
	
	@Column(name="recy_id")
	private Long recyId;
	
	public enum RecycleType {
		photo, travel, message
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSettings getUser() {
		return user;
	}

	public void setUser(UserSettings user) {
		this.user = user;
	}

	public RecycleType getRecyType() {
		return recyType;
	}

	public void setRecyType(RecycleType recyType) {
		this.recyType = recyType;
	}

	public Long getRecyId() {
		return recyId;
	}

	public void setRecyId(Long recyId) {
		this.recyId = recyId;
	}
	
	
}
