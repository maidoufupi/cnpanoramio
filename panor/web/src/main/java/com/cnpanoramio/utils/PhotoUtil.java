package com.cnpanoramio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;

public class PhotoUtil {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
	
	public synchronized static PhotoCameraInfo transformCameraInfo(Photo photo) {
		PhotoCameraInfo cameraInfo = new PhotoCameraInfo();
		cameraInfo.setId(photo.getId());
		cameraInfo.setCreateDate(photo.getCreateDate());

		Point gps = photo.getGpsPoint();
		if (gps != null) {
			cameraInfo.setLat(gps.getLat());
			cameraInfo.setLng(gps.getLng());
			cameraInfo.setAlt(gps.getAlt());
		}
		cameraInfo.setUserName(photo.getOwner().getUsername());

		PhotoDetails details = photo.getDetails();
		cameraInfo.setModel(details.getModel());
		cameraInfo.setMake(details.getMake());
		if(null != details.getDateTimeOriginal()) {
			cameraInfo.setDateTimeOriginal(format.format(details.getDateTimeOriginal()));
		}
		
		if(null != details.getExposureTime()) {
			cameraInfo.setExposureTime(details.getExposureTime()*10000 + "/10000 s");
		}
		cameraInfo.setFocalLength(details.getFocalLength() == null ? "" : details.getFocalLength() + " mm");
		cameraInfo.setFNumber(details.getFNumber() == null ? "" : "f/" + details.getFNumber());
		cameraInfo.setISO(details.getISO() == null ? "" : "ISO-" + details.getISO());
		if(details.getExposureBias() == null || details.getExposureBias() == 0) {
			cameraInfo.setExposureBias("0 step");
		}else {
			cameraInfo.setExposureBias(details.getExposureBias() + " step");			
		}		
		cameraInfo.setFlash(details.getFlash());

		return cameraInfo;
	}
	
	public synchronized static PhotoProperties transformProperties(Photo photo) {
		PhotoProperties pp = new PhotoProperties();
		pp.setId(photo.getId());
		pp.setTitle(photo.getTitle());
		pp.setDescription(photo.getDescription());
		Date createTime = photo.getCreateDate();
		if(null != createTime) {
			pp.setCreateTime(format.format(createTime.getTime()));
		}
		pp.setUserId(photo.getOwner().getId());
		
		for(Tag tag : photo.getTags()) {
			pp.getTags().add(tag.getTag());
		}
		
		pp.setFavCount(photo.getFavorites().size());
		pp.setPoint(photo.getGpsPoint());
		// 目前默认高德供应商坐标
		pp.setVendor(MapVendor.gaode.name());
		pp.setIs360(photo.isIs360());
		
		PhotoDetails details = photo.getDetails();
		if(null != details) {
			pp.setWidth(details.getPixelXDimension());
			pp.setHeight(details.getPixelYDimension());
			pp.setDateTime(details.getDateTime());
		}
		return pp;
	}
	
	public synchronized static MapVendor getMapVendor(String vendor) {
    	MapVendor mVendor;
		if (vendor.equalsIgnoreCase("gaode")) {
			mVendor = MapVendor.gaode;
		} else if (vendor.equalsIgnoreCase("qq")) {
			mVendor = MapVendor.qq;
		} else if (vendor.equalsIgnoreCase("baidu")) {
			mVendor = MapVendor.baidu;
		} else if (vendor.equalsIgnoreCase("ali")) {
			mVendor = MapVendor.ali;
		} else if (vendor.equalsIgnoreCase("sogou")) {
			mVendor = MapVendor.sogou;
		} else if (vendor.equalsIgnoreCase("mapbar")) {
			mVendor = MapVendor.mapbar;
		} else {
			mVendor = MapVendor.gps;
		}
		return mVendor;
    }
}