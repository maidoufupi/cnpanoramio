package com.cnpanoramio.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.appfuse.model.User;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "photo")
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@Column(name = "file_type")
	private String fileType;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_size")
	private Integer fileSize;

	@Embedded
	private Point gpsPoint;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "modify_date")
	private Date modifyDate;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	private String title;
	private String description;

	@Column(nullable = true)
	private boolean deleted;

	@Column(name = "mark_best")
	private boolean markBest;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PhotoGps> gps;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "photo")
	@PrimaryKeyJoinColumn
	private PhotoDetails details;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "photo_id")
	private Set<Views> views = new HashSet<Views>(0);

	@OneToMany(mappedBy="pk.photo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Favorite> favorites = new HashSet<Favorite>(0);

	@Column(name = "rating")
	private Integer Rating;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Tag> tags = new HashSet<Tag>(0);
	
	@ManyToOne
	@JoinColumn(name = "travel_id")
	private Travel travel;
	
	@Column(name = "is360")
	private Boolean is360;
	
	@OneToMany
	private Set<Like> likes = new HashSet<Like>(0);
	
	@OneToMany
	private Set<Comment> comments = new HashSet<Comment>(0);
	
	public final Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public Point getGpsPoint() {
		return gpsPoint;
	}

	public void setGpsPoint(Point gpsPoint) {
		this.gpsPoint = gpsPoint;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isMarkBest() {
		return markBest;
	}

	public void setMarkBest(boolean markBest) {
		this.markBest = markBest;
	}

	public Set<PhotoGps> getGps() {
		return gps;
	}

	public void setGps(Set<PhotoGps> gps) {
		this.gps = gps;
	}

	public PhotoDetails getDetails() {
		return details;
	}

	public void setDetails(PhotoDetails details) {
		this.details = details;
	}

	public Integer getRating() {
		return Rating;
	}

	public void setRating(Integer rating) {
		Rating = rating;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		if (!this.tags.contains(tag)) {
			this.tags.add(tag);
		}
	}

	public Set<Views> getViews() {
		return views;
	}

	public void setViews(Set<Views> views) {
		this.views = views;
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Set<Favorite> favorites) {
		this.favorites = favorites;
	}

	public void addFavorite(Favorite favorite) {
		Iterator<Favorite> iter = this.favorites.iterator();
		Favorite fav;
		boolean has = false;
		while (iter.hasNext()) {
			fav = iter.next();
			if (fav.getPk().getUserId().equals(favorite.getPk().getUserId())) {
				has = true;
				break;
			}
		}
		if(!has) {
			favorite.getPk().setPhoto(this);
			this.favorites.add(favorite);			
		}
	}
	
	public void removeFavorite(Long userId) {
		Iterator<Favorite> iter = this.favorites.iterator();
		Favorite fav;
		while (iter.hasNext()) {
			fav = iter.next();
			if (fav.getPk().getUserId().equals(userId)) {
				iter.remove();
			}
		}
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public Boolean isIs360() {
		return is360;
	}

	public void setIs360(Boolean is360) {
		this.is360 = is360;
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
}
