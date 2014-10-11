package com.cnpanoramio.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.service.PhotoService;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private HttpSession httpSession;
	
	@Override
	public PhotoProperties transform(Photo photo) {
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
		
		// 根据用户配置选取相应的gps
		MapVendor mapVendor = (MapVendor)httpSession.getAttribute("mapVendor");
		PhotoGps gps = photo.getGps().get(mapVendor);
		if(null != gps) {
			pp.setPoint(gps.getPoint());
			pp.setVendor(mapVendor.name());
		}else {
			pp.setPoint(photo.getGpsPoint());
			pp.setVendor(MapVendor.gaode.name());
		}
		
		pp.setIs360(photo.isIs360());
		
		PhotoDetails details = photo.getDetails();
		if(null != details) {
			pp.setWidth(details.getPixelXDimension());
			pp.setHeight(details.getPixelYDimension());
			pp.setDateTime(details.getDateTimeOriginal());
		}
		return pp;
	}
	
	@Override
	public String getPhotoOssKey(Photo photo) {
		return photo.getId() + "." + photo.getFileType();
	}

}
