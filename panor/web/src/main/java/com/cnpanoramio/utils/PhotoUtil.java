package com.cnpanoramio.utils;

import java.text.SimpleDateFormat;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Point;
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
		cameraInfo.setDateTimeOriginal(details.getDateTimeOriginal());
		cameraInfo.setExposureTime(details.getExposureTime());
		cameraInfo.setFocalLength(details.getFocalLength());
		cameraInfo.setFNumber(details.getFNumber());
		cameraInfo.setIso(details.getISO());
		cameraInfo.set曝光补偿("0");
		cameraInfo.set闪光灯("无闪光灯");

		return cameraInfo;
	}
	
	public synchronized static PhotoProperties transformProperties(Photo photo) {
		PhotoProperties pp = new PhotoProperties();
		pp.setId(photo.getId());
		pp.setTitle(photo.getTitle());
		pp.setDescription(photo.getDescription());
		pp.setCreateTime(format.format(photo.getCreateDate().getTime()));
		return pp;
	}
}