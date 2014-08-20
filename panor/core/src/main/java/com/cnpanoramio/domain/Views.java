package com.cnpanoramio.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "views")
public class Views {
	
	@Id
    private ViewsPK pk = new ViewsPK();
	
	@Column
	private int count;
	
	public Views() {
		super();
	}
	
	public Views(Long photoId, String appId) {
		pk.setPhotoId(photoId);
		pk.setAppId(appId);
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
		pk.setDate(cal.getTime());   
	}
	
	public ViewsPK getPk() {
		return pk;
	}

	public void setPk(ViewsPK pk) {
		this.pk = pk;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
