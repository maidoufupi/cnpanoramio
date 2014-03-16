package com.cnpanoramio.json;


import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PhotoCameraInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoCameraInfo {
	
	private Long id;

	private Date createDate;
	private Double lat;
	private Double lng;
	private Double alt;
	private String userName;
	
	// 相机型号
	private String model;
	// 拍摄日期
	private String dateTimeOriginal;
	// 曝光时间
	private String exposureTime;
	// 焦距
	private String focalLength;
	// 光圈
	private String FNumber;
	// ISO
	private String ISO;
	// 曝光补偿
	private String ExposureBias;
	// 闪光灯
	private Short Flash;
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getFocalLength() {
		return focalLength;
	}
	public void setFocalLength(String focalLength) {
		this.focalLength = focalLength;
	}

	public String getISO() {
		return ISO;
	}
	public void setISO(String iSO) {
		ISO = iSO;
	}
	public String getExposureBias() {
		return ExposureBias;
	}
	public void setExposureBias(String exposureBias) {
		ExposureBias = exposureBias;
	}
	public Short getFlash() {
		return Flash;
	}
	public void setFlash(Short flash) {
		Flash = flash;
	}
	public String getFNumber() {
		return FNumber;
	}
	public void setFNumber(String fNumber) {
		FNumber = fNumber;
	}
	
	
}
