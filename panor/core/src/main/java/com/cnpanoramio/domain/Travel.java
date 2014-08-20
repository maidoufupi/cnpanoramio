package com.cnpanoramio.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

@Entity
@Table(name = "travel")
@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Travel {
	
	public Travel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Travel(String title) {
		super();
		this.title = title;
		this.createTime = Calendar.getInstance().getTime();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@DocumentId
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserSettings user;
	
	@OneToMany(mappedBy="travel", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
	private Set<TravelSpot> spots = new HashSet<TravelSpot>(0);
	
	// 默认的spot
	@OneToOne
	@JoinColumn(name="spot_id")
	private TravelSpot spot;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="modify_time")
	private Date modifyTime;
	
	@Column(name="time_start")
	private Date timeStart;
	
	@Column(name="time_end")
	private Date timeEnd;
	
	@Column(name="address")
	@Field
	private String address;
	
	@Column(name="title")
	@Field
	private String title;
	
	@Column(name="description")
	@Field
	private String description;
	
	@Column(name="deleted")
	private Boolean deleted;
	
	@OneToMany(mappedBy="travel")
	private Set<Like> likes = new HashSet<Like>(0);
	
	// 相册封面图片
	@ManyToOne
	@JoinColumn(name="album_cover")
	private Photo albumCover;

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

	public Set<TravelSpot> getSpots() {
		return spots;
	}

	@SuppressWarnings("unused")
	private void setSpots(Set<TravelSpot> spots) {
		this.spots = spots;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TravelSpot getSpot() {
		return spot;
	}

	public void setSpot(TravelSpot spot) {
		this.spot = spot;
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Photo getAlbumCover() {
		return albumCover;
	}

	public void setAlbumCover(Photo albumCover) {
		this.albumCover = albumCover;
	}
		
}
