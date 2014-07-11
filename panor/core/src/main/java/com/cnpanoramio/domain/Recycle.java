package com.cnpanoramio.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Entity
@Table(name = "recycle")
public class Recycle {
	
	public static String CON_TYPE_PHOTO = "photo";
	public static String CON_TYPE_TRAVEL = "travel";
	

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserSettings user;
	
	@JsonProperty("create_time")
	@Column(name="create_time")
	private Date createTime;
	
	@JsonProperty("recy_type")
	@Column(name="recy_type")
	private String recyType;
	
	@JsonProperty("recy_id")
	@Column(name="recy_id")
	private Long recyId;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRecyType() {
		return recyType;
	}

	public void setRecyType(String recyType) {
		this.recyType = recyType;
	}

	public Long getRecyId() {
		return recyId;
	}

	public void setRecyId(Long recyId) {
		this.recyId = recyId;
	}
	
	
}
