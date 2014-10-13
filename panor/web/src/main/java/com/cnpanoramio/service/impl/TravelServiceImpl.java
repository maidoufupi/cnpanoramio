package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cnpanoramio.dao.TravelSpotDao;
import com.cnpanoramio.domain.Message;
import com.cnpanoramio.domain.Message.MessageType;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.TravelResponse.Travel;
import com.cnpanoramio.json.TravelResponse.TravelSpot;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class TravelServiceImpl implements TravelService {

	@Autowired
	private UserManager userManager;

	@Autowired
	private TravelManager travelManager;

	@Autowired
	private TravelSpotDao travelSpotDao;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoManager photoManager;

	@Autowired
	private MessageManager messageManager;

	@Override
	public Travel getTravel(Long travelId) {
		Travel travel = convertTravel(travelManager.getTravel(travelId));
		Message message = messageManager.getMessage(MessageType.travel,
				travelId);
		if (null != message) {
			travel.setMessageId(message.getId());
		}

		return travel;
	}

	@Override
	public List<Travel> getTravels(User user) {
		List<com.cnpanoramio.domain.Travel> travels = travelManager
				.getTravels(user);
		List<Travel> ts = convertTravels(travels);
		for (Travel travel : ts) {
			travel.setSpots(null);
		}
		return ts;
	}

	@Override
	public Travel createMyTravel(User user, String travel) {
		com.cnpanoramio.domain.Travel tra = travelManager.createMyTravel(user,
				travel);
		return convertTravel(tra);
	}

	public List<Travel> convertTravels(
			List<com.cnpanoramio.domain.Travel> travels) {
		List<Travel> ts = new ArrayList<Travel>();
		for (com.cnpanoramio.domain.Travel t : travels) {
			ts.add(convertTravel(t));
		}
		return ts;
	}

	public Travel convertTravel(com.cnpanoramio.domain.Travel travel) {
		Travel t = new Travel();
		BeanUtils.copyProperties(travel, t, new String[] { "spots", "user",
				"spot", "albumCover" });
		if (null != travel.getUser()) {
			t.setUserId(travel.getUser().getId());
			t.setUsername(travel.getUser().getName());
			t.setUser(UserUtil.getSimpleOpenInfo(travel.getUser()));
		}
		if (null != travel.getSpot()) {
			t.setSpot(convertTravelSpot(travel.getSpot()));
		}
		for (com.cnpanoramio.domain.TravelSpot travelSpot : travel.getSpots()) {
			t.getSpots().add(convertTravelSpot(travelSpot));
		}

		// set album cover photo
		if (null != travel.getAlbumCover()) {
			t.setAlbumCover(PhotoUtil.getPhotoOssKey(travel.getAlbumCover()));
		} else {
			Iterator<com.cnpanoramio.domain.TravelSpot> iter = travel
					.getSpots().iterator();
			if (iter.hasNext()) {
				com.cnpanoramio.domain.TravelSpot spot = iter.next();
				Iterator<Photo> iterPhotos = spot.getPhotos().iterator();
				if (iterPhotos.hasNext()) {
					Photo photo = iterPhotos.next();
					t.setAlbumCover(PhotoUtil.getPhotoOssKey(photo));
				}

			}

		}

		// photo size
		Integer photoSize = 0;
		for (com.cnpanoramio.domain.TravelSpot spot : travel.getSpots()) {
			photoSize += spot.getPhotos().size();
		}
		t.setPhotoSize(photoSize);

		return t;
	}

	public TravelSpot convertTravelSpot(
			com.cnpanoramio.domain.TravelSpot travelSpot) {
		TravelSpot tSpot = new TravelSpot();
		BeanUtils.copyProperties(travelSpot, tSpot, new String[] { "travel",
				"photos" });
		tSpot.setTravelId(travelSpot.getTravel().getId());
		for (Photo photo : travelSpot.getPhotos()) {
			// 过滤掉被标记删除的图片
			if (!photo.isDeleted()) {
				tSpot.getPhotos().add(photoService.transform(photo));
			}
		}
		return tSpot;
	}

	@Override
	public TravelSpot changeSpot(Long id, TravelSpot spot) {

		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.TravelSpot travelSpot = travelSpotDao.get(id);
		checkMyTravel(travelSpot.getTravel(), me);
		
		if (null != spot.getAddress()) {
			travelSpot.setAddress(spot.getAddress());
		}

		if (null != spot.getTitle()) {
			travelSpot.setTitle(spot.getTitle());
		}

		if (null != spot.getDescription()) {
			travelSpot.setDescription(spot.getDescription());
		}

		if (null != spot.getTimeStart()) {
			travelSpot.setTimeStart(spot.getTimeStart());
		}

		for (PhotoProperties photo : spot.getPhotos()) {
			Photo p = photoManager.get(photo.getId());
			// 添加到景点
			if (!travelSpot.equals(p.getTravelSpot())) {
				travelManager.addSpotPhoto(travelSpot, p);
			}

			// 更新位置
			if(null != photo.getPoint()) {
				photoManager.properties(photo.getId(), photo);
			}
			
			// 更新拍摄日期
			if(null != photo.getDateTime()) {
				p.getDetails().setDateTime(photo.getDateTime());
			}
		}
		return convertTravelSpot(travelSpot);
	}

	@Override
	public void changeSpotPhotoPostion(Long id, TravelSpot spot) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.TravelSpot travelSpot = travelSpotDao.get(id);
		checkMyTravel(travelSpot.getTravel(), me);

		for (PhotoProperties photo : spot.getPhotos()) {
			Photo p = photoManager.get(photo.getId());
			if (!travelSpot.equals(p.getTravelSpot())) {
				throw new AccessDeniedException("Photo " + p.getId()
						+ " 不属于TravelSpot " + travelSpot.getId());
			}

			photoManager.properties(photo.getId(), photo);
		}

	}
	
	@Override
	public Travel changeTravel(Travel travel) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travelD = travelManager.get(travel.getId());
		checkMyTravel(travelD, me);
		
		// 地址
		if(null != travel.getAddress()) {
			travelD.setAddress(travel.getAddress());
		}
		
		// title
		if(null != travel.getTitle()) {
			travelD.setTitle(travel.getTitle());
		}
		
		// 描述
		if(null != travel.getDescription()) {
			travelD.setDescription(travel.getDescription());
		}
		
		// 开始时间
		if(null != travel.getTimeStart()) {
			travelD.setTimeStart(travel.getTimeStart());
		}
		
		// 旅行相册封面
		if(null != travel.getAlbumCover()) {
			if(StringUtils.hasLength(travel.getAlbumCover())) {
				travelD.setAlbumCover(photoManager.get(Long.parseLong(travel.getAlbumCover())));
			}else {
				travelD.setAlbumCover(null);
			}
		}
		
		return convertTravel(travelD);
	}

	@Override
	public Travel changeTravelDesc(Long id, String description) {
		com.cnpanoramio.domain.Travel travel = travelManager.getTravel(id);
		User me = UserUtil.getCurrentUser(userManager);
		checkMyTravel(travel, me);
		return convertTravel(travelManager.changeTravelDesc(id, description));
	}

	@Override
	public Travel changeTravelName(Long id, String name) {
		com.cnpanoramio.domain.Travel travel = travelManager.getTravel(id);
		User me = UserUtil.getCurrentUser(userManager);
		checkMyTravel(travel, me);

		return convertTravel(travelManager.changeTravelName(id, name));
	}

	@Override
	public TravelSpot getSpot(Long id) {
		return convertTravelSpot(travelManager.getTravelSpot(id));
	}

	@Override
	public Travel addTravelPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);
		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.get(photoId);
			PhotoUtil.checkMyPhoto(photo, me);
		}

		return convertTravel(travelManager.addTravelPhotos(id, photos));
	}

	@Override
	public Travel removeTravelPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);

		return convertTravel(travelManager.removeTravelPhotos(id, photos));
	}

	@Override
	public TravelSpot createTravelSpot(Long id, TravelSpot spot) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);

		com.cnpanoramio.domain.TravelSpot travelSpot = new com.cnpanoramio.domain.TravelSpot();
		travelSpot.setAddress(spot.getAddress());
		travelSpot.setTitle(spot.getTitle());
		travelSpot.setDescription(spot.getDescription());
		travelSpot.setTimeStart(spot.getTimeStart());
		return convertTravelSpot(travelManager.createTravelSpot(id, travelSpot));
	}

	@Override
	public Travel addSpotPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.TravelSpot spot = travelSpotDao.get(id);
		checkMyTravel(spot.getTravel(), me);
		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.get(photoId);
			PhotoUtil.checkMyPhoto(photo, me);
		}
		return convertTravel(travelManager.addSpotPhotos(id, photos));
	}

	@Override
	public Travel deleteSpot(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.TravelSpot spot = travelSpotDao.get(id);
		checkMyTravel(spot.getTravel(), me);
		return convertTravel(travelManager.deleteSpot(id));
	}

	@Override
	public void deleteTravel(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);
		travelManager.deleteTravel(id);
	}

	@Override
	public void removeTravel(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);
		travelManager.removeTravel(id);
	}

	@Override
	public void cancelDeleteTravel(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		com.cnpanoramio.domain.Travel travel = travelManager.get(id);
		checkMyTravel(travel, me);
		travelManager.cancelDeleteTravel(id);
	}

	private void checkMyTravel(com.cnpanoramio.domain.Travel travel, User me) {

		if (!travel.getUser().getId().equals(me.getId())) {
			throw new AccessDeniedException("Travel (id=" + travel.getId()
					+ ") is not belong to you");
		}
	}

	@Override
	public Travel getNoTravel() {
		User me = UserUtil.getCurrentUser(userManager);
		List<Photo> photos = photoManager.getNoTravel(me);
		TravelSpot spot = new TravelSpot();
		Travel travel = new Travel();

		spot.setPhotos(PhotoUtil.transformPhotos(photos));
		travel.setId(0L);
		travel.getSpots().add(spot);
		travel.setPhotoSize(photos.size());
		return travel;
	}

	
}
