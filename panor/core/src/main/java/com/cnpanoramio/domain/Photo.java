package com.cnpanoramio.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.appfuse.model.User;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.cnpanoramio.MapVendor;

@Entity
@Table(name = "photo")
public class Photo extends BaseEntity implements Comparable<Photo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2125921095320469942L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@DocumentId
	private Long id;

	@Column(name = "name")
	private String name;

	@Field
	@Column(name = "file_type")
	private String fileType;

	@Field
	@Column(name = "file_path")
	private String filePath;

	@Field
	@Column(name = "file_size")
	private Long fileSize;

	@Embedded
	private Point gpsPoint;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column
	private String title;
	
	@Column(length=1000)
	private String description;

	@Column(name = "mark_best", nullable = true)
	private boolean markBest;

	@OneToMany(mappedBy="photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@MapKey(name="vendor")
	private Map<MapVendor, PhotoGps> gps = new HashMap<MapVendor, PhotoGps>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "photo")
	@PrimaryKeyJoinColumn
	@IndexedEmbedded
	private PhotoDetails details;

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "photo_id")
//	private Set<Views> views = new HashSet<Views>(0);

	@OneToMany(mappedBy="photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@MapKey(name="user")
	private Map<User, Favorite> favorites = new HashMap<User, Favorite>(0);

	@Column(name = "rating")
	private Integer Rating;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinTable(name="photo_tags", 
		joinColumns = @JoinColumn(name = "photo_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id"),
		uniqueConstraints = @UniqueConstraint(name = "uq_photo_tag",
			columnNames = {"photo_id", "tag_id"}))
	private Set<Tag> tags = new HashSet<Tag>(0);
		
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "travel_spot_id")
	private TravelSpot travelSpot;
	
	@Field
	@Column(name = "is360")
	private boolean is360;
	
	// 图片主颜色
	private String color;
	
	@OneToMany(mappedBy="photo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Like> likes = new HashSet<Like>(0);
	
	@OneToMany(mappedBy="photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<Comment>(0);
	
	@OneToMany(mappedBy="photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<PhotoPanoramioIndex> panoramioIndex = new HashSet<PhotoPanoramioIndex>(0);
	
	@OneToMany(mappedBy="photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<PhotoLatestIndex> latestIndex = new HashSet<PhotoLatestIndex>(0);
	
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

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Point getGpsPoint() {
		return gpsPoint;
	}

	public void setGpsPoint(Point gpsPoint) {
		this.gpsPoint = gpsPoint;
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

	public boolean isMarkBest() {
		return markBest;
	}

	public void setMarkBest(boolean markBest) {
		this.markBest = markBest;
	}
	
	public Map<MapVendor, PhotoGps> getGps() {
		return gps;
	}

	public void setGps(Map<MapVendor, PhotoGps> gps) {
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

//	public Set<Views> getViews() {
//		return views;
//	}
//
//	public void setViews(Set<Views> views) {
//		this.views = views;
//	}

	public Map<User, Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Map<User, Favorite> favorites) {
		this.favorites = favorites;
	}

//	public void addFavorite(Favorite favorite) {
//		Iterator<Favorite> iter = this.favorites.iterator();
//		Favorite fav;
//		boolean has = false;
//		while (iter.hasNext()) {
//			fav = iter.next();
//			if (fav.getUser() == favorite.getUser()) {
//				has = true;
//				break;
//			}
//		}
//		if(!has) {
//			favorite.setPhoto(this);
//			this.favorites.add(favorite);			
//		}
//	}
//	
//	public void removeFavorite(Long userId) {
//		Iterator<Favorite> iter = this.favorites.iterator();
//		Favorite fav;
//		while (iter.hasNext()) {
//			fav = iter.next();
//			if (fav.getUser().getId().equals(userId)) {
//				iter.remove();
//			}
//		}
//	}

	public boolean isIs360() {
		return is360;
	}

	public void setIs360(boolean is360) {
		this.is360 = is360;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public int compareTo(Photo obj) {
		return this.getRating() - obj.getRating();
	}

	public TravelSpot getTravelSpot() {
		return travelSpot;
	}

	public void setTravelSpot(TravelSpot travelSpot) {
		this.travelSpot = travelSpot;
	}

	public Set<PhotoPanoramioIndex> getPanoramioIndex() {
		return panoramioIndex;
	}

	public void setPanoramioIndex(Set<PhotoPanoramioIndex> panoramioIndex) {
		this.panoramioIndex = panoramioIndex;
	}

	public Set<PhotoLatestIndex> getLatestIndex() {
		return latestIndex;
	}

	public void setLatestIndex(Set<PhotoLatestIndex> latestIndex) {
		this.latestIndex = latestIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
