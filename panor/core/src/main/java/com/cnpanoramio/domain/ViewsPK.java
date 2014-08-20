package com.cnpanoramio.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewsPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3230380886353242471L;

	public ViewsPK() {
		super();
	}

	public ViewsPK(Long photoId, String appId) {
		super();
		this.photoId = photoId;
		this.appId = appId;
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
		this.date = cal.getTime();   
	}
	
	public ViewsPK(Long photoId, String appId, Date date) {
		super();
		this.photoId = photoId;
		this.appId = appId;
		this.date = date;   
	}
	
	@Column(name="photo_id")
	private Long photoId;
	
	@Column(name="app_id")
	private String appId;
	
	@Column(name="date")
	private Date date;
	
	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((photoId == null) ? 0 : photoId.hashCode());
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
		ViewsPK other = (ViewsPK) obj;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (photoId == null) {
			if (other.photoId != null)
				return false;
		} else if (!photoId.equals(other.photoId))
			return false;
		return true;
	}	
}
