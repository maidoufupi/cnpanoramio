package com.cnpanoramio.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.appfuse.model.User;

/**
 * 收藏
 * 
 * @author any
 *
 */
@Entity
@Table(name = "favorite")
public class Favorite {
			
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
    @JoinColumn(name="photo_id", nullable=true)
	private Photo photo;
	
	private Date date;
	
	public Favorite() {
		super();
	}
	
	public Favorite(User user, Photo photo) {
		super();
		this.user = user;
		this.photo = photo;
	}
	
//	public Favorite(Long userId) {
//		super();
//		this.pk.setUserId(userId);
//	}
	
//	public PK getPk() {
//		return pk;
//	}
//
//	public void setPk(PK pk) {
//		this.pk = pk;
//	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	
//	@Embeddable
//	public static class PK implements Serializable {
//		
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -4084911392422210170L;
//		
//		@ManyToOne
//	    @JoinColumn(name="photo_id", nullable=true)
//		private Photo photo;
//
////		@Column(name="photo_id")
////	    private Long photoId;
//		
//		@Column(name="user_id")
//	    private Long userId;
//
////		public Long getPhotoId() {
////			return photoId;
////		}
////
////		public void setPhotoId(Long photoId) {
////			this.photoId = photoId;
////		}
//
//		public Long getUserId() {
//			return userId;
//		}
//
//		public void setUserId(Long userId) {
//			this.userId = userId;
//		}
//		
//
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + ((photo == null) ? 0 : photo.hashCode());
//			result = prime * result
//					+ ((userId == null) ? 0 : userId.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			PK other = (PK) obj;
//			if (photo == null) {
//				if (other.photo != null)
//					return false;
//			} else if (!photo.equals(other.photo))
//				return false;
//			if (userId == null) {
//				if (other.userId != null)
//					return false;
//			} else if (!userId.equals(other.userId))
//				return false;
//			return true;
//		}
//
//		public Photo getPhoto() {
//			return photo;
//		}
//
//		public void setPhoto(Photo photo) {
//			this.photo = photo;
//		}		
//		
//	}
	
}
