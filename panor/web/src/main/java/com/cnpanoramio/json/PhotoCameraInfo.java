package com.cnpanoramio.json;


import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PhotoCameraInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoCameraInfo {

	private Date createDate;
	private Double lat;
	private Double lng;
	private Double alt;
	private String userName;
	
	private String model;
	private String dateTimeOriginal;
	private String exposureTime;
	private Double focalLength;
	private Double fNumber;
	private String iso;
	private String 曝光补偿;
	private String 闪光灯;
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getAlt() {
		return alt;
	}
	public void setAlt(Double alt) {
		this.alt = alt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDateTimeOriginal() {
		return dateTimeOriginal;
	}
	public void setDateTimeOriginal(String dateTimeOriginal) {
		this.dateTimeOriginal = dateTimeOriginal;
	}
	public String getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}
	public Double getFocalLength() {
		return focalLength;
	}
	public void setFocalLength(Double focalLength) {
		this.focalLength = focalLength;
	}
	public Double getFNumber() {
		return fNumber;
	}
	public void setFNumber(Double fNumber) {
		this.fNumber = fNumber;
	}
	public String getIso() {
		return iso;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
	public String get曝光补偿() {
		return 曝光补偿;
	}
	public void set曝光补偿(String 曝光补偿) {
		this.曝光补偿 = 曝光补偿;
	}
	public String get闪光灯() {
		return 闪光灯;
	}
	public void set闪光灯(String 闪光灯) {
		this.闪光灯 = 闪光灯;
	}	
}
