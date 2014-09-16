package com.cnpanoramio.utils;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.security.access.AccessDeniedException;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;

public class PhotoUtil {

//	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
	
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
		if(null == details) {
			details = new PhotoDetails();
		}
		cameraInfo.setModel(details.getModel());
		cameraInfo.setMake(details.getMake());
		if(null != details.getDateTimeOriginal()) {
			cameraInfo.setDateTimeOriginal(details.getDateTimeOriginal());
		}
		
		if(null != details.getExposureTime()) {
			cameraInfo.setExposureTime(details.getExposureTime());
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
	
	public synchronized static List<PhotoProperties> transformPhotos(List<Photo> photos) {
		List<PhotoProperties> pps = new ArrayList<PhotoProperties>();
		for (Photo photo : photos) {
			pps.add(PhotoUtil.transformProperties(photo));
		}
		return pps;
	}
	
	public synchronized static PhotoProperties transformProperties(Photo photo) {
		PhotoProperties pp = new PhotoProperties();
		pp.setId(photo.getId());
		pp.setTitle(photo.getTitle());
		pp.setDescription(photo.getDescription());
		pp.setCreateTime(photo.getCreateDate());
		pp.setUserId(photo.getOwner().getId());
		pp.setFileSize(photo.getFileSize());
		pp.setFileName(photo.getName());
		pp.setFileType(photo.getFileType().toLowerCase());
		
		pp.setOssKey(getPhotoOssKey(photo));
		
		// 旅行
		if(null != photo.getTravelSpot()) {
			pp.setTravelId(photo.getTravelSpot().getTravel().getId());
			pp.setTravelName(photo.getTravelSpot().getTravel().getTitle());
		}		
		
		for(Tag tag : photo.getTags()) {
			pp.getTags().add(tag.getContent());
		}
		// 收藏
		pp.setFavCount(photo.getFavorites().size());
		// 赞
		pp.setLikeCount(photo.getLikes().size());
		// 评论
		pp.setCommentCount(photo.getComments().size());
		
		pp.setPoint(photo.getGpsPoint());
		// 目前默认高德供应商坐标
		pp.setVendor(MapVendor.gaode.name());
		pp.setIs360(photo.isIs360());
		
		PhotoDetails details = photo.getDetails();
		if(null != details) {
			pp.setWidth(details.getPixelXDimension());
			pp.setHeight(details.getPixelYDimension());
			pp.setDateTime(details.getDateTimeOriginal());
		}
		return pp;
	}
	
	public synchronized static MapVendor getMapVendor(String vendor) {
    	MapVendor mVendor;
    	if(null == vendor) {
    		mVendor = MapVendor.gps;
    	}else {
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
    		}else {
    			mVendor = MapVendor.gps;
    		}
    	}
		return mVendor;
    }
	
	public static void checkMyPhoto(Photo photo, User me) {
		if (!photo.getOwner().equals(me)) {
			throw new AccessDeniedException("Access Denied! Photo Id: "	+ photo.getId());
		}
	}
	
	public static String getPhotoOssKey(Photo photo) {
		return photo.getId() + "." + photo.getFileType();
	}
}