package com.cnpanoramio.service.impl;

import javax.servlet.http.HttpSession;

import com.cnpanoramio.domain.*;
import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private LikeManager likeManager;
	
	@Autowired
	private FavoriteDao favoriteDao;
	
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
			gps = photo.getGps().get(MapVendor.gaode);
			if(null != gps) {
				pp.setPoint(gps.getPoint());
				pp.setVendor(gps.getVendor().name());
			}else {
				pp.setPoint(photo.getGpsPoint());
//				pp.setVendor(MapVendor.gaode.name());
			}
		}
		
		// 360度全景照片
		pp.setIs360(photo.isIs360());
		// 图片主颜色
		pp.setColor(photo.getColor());
		
		PhotoDetails details = photo.getDetails();
		if(null != details) {
			if(details.getOrientation() == 5 ||
					details.getOrientation() == 6 ||
					details.getOrientation() == 7 ||
					details.getOrientation() == 8) {
				// 根据图片附加信息进行大小旋转
				pp.setWidth(details.getPixelYDimension());
				pp.setHeight(details.getPixelXDimension());
			}else {
				pp.setWidth(details.getPixelXDimension());
				pp.setHeight(details.getPixelYDimension());
			}
			
			// 优先取DateTimeOriginal 再取DateTimeDigitized，然后再考虑DateTime
			if(null != details.getDateTimeOriginal()) {
				pp.setDateTime(details.getDateTimeOriginal());
			}else if(null != details.getDateTimeDigitized()) {
				pp.setDateTime(details.getDateTimeDigitized());
			}else {
				pp.setDateTime(details.getDateTime());
			}		
		}
		return pp;
	}
	
	@Override
	public PhotoProperties getPhotoProperties(Long id, User user) {
		Photo photo = photoManager.get(id);
		PhotoProperties prop = transform(photo);
		// 返回此图片是否被他 收藏 赞
		if(null != user) {
			Like like = likeManager.getLikePhoto(user, photo);
			if(null != like) {
				prop.setLike(true);
			}
			Favorite f = favoriteDao.get(id, user.getId());
			if (null != f) {
				prop.setFavorite(true);
			}
		}
		return prop;
	}
	
	@Override
	public String getPhotoOssKey(Photo photo) {
		return photo.getId() + "." + photo.getFileType();
	}

}
