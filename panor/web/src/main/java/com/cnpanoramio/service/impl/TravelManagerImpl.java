package com.cnpanoramio.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.dao.TravelSpotDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.TravelSpot;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class TravelManagerImpl implements TravelManager {

	protected transient final Log log = LogFactory.getLog(getClass());

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private TravelDao travelDao;
	@Autowired
	private TravelSpotDao travelSpotDao;
	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private UserSettingsDao userSettingsDao;

	@Autowired
	private PhotoManager photoManager;

	@Autowired
	private UserManager userManager = null;

	@Override
	public Travel getTravel(Long travelId) {
		return travelDao.get(travelId);
	}

	@Override
	public List<Travel> getTravels(User user) {
		UserSettings settings = userSettingsDao.get(user.getId());
		return convertTravels(settings.getTravels());
	}

	@Override
	public List<Travel> createMyTravel(User user, String travel) {
		UserSettings settings = userSettingsDao.get(user.getId());
		// 如果名称已经存在，则不创建
		Travel tra = travelDao.getByName(user.getId(), travel);
		if (null == tra) {
			tra = new Travel(travel);
			// 保存实体
			tra = travelDao.save(tra);
			// 相互设置
			tra.setUser(settings);
			settings.getTravels().add(tra);
		}
		return settings.getTravels();
	}

	private List<Travel> convertTravels(List<Travel> travels) {
		List<Travel> list = new ArrayList<Travel>();
		for (Travel travel : travels) {
			Travel t = new Travel();
			BeanUtils.copyProperties(travel, t,
					new String[] { "spots", "user" });
			list.add(t);
		}
		return list;
	}

	@Override
	public Photo addTravelPhoto(Travel travel, Photo photo) {

		// 先删除photo原有travel信息
		if(null != photo.getTravelSpot()) {
			if(photo.getTravelSpot().getTravel().equals(travel)) {
				return photo;
			}else {
				photo.getTravelSpot().getPhotos().remove(photo);
			}
		}
		
		TravelSpot travelSpot = null;
		PhotoDetails detail = photo.getDetails();
		Date photoDate = null;
		if (null != detail) {
			photoDate = detail.getDateTimeOriginal();
			if (null != photoDate) {
				String tDate = dateFormat.format(photoDate);
				for (TravelSpot spot : travel.getSpots()) {
					if (dateFormat.format(spot.getTimeStart())
							.equalsIgnoreCase(tDate)) {
						travelSpot = spot;
						break;
					}
				}
			}
		}
		
		if(null == travelSpot) {
			// 如果没有拍摄日期，则添加到同一个没有日期的spot
			for (TravelSpot spot : travel.getSpots()) {
				if (null == spot.getTimeStart()) {
					travelSpot = spot;
					break;
				}
			}
		}

		// 如果既没有匹配日期的spot，有没有无日期的spot，则创建一个spot
		if (null == travelSpot) {
			travelSpot = new TravelSpot();
			travelSpot.setTimeStart(photoDate);

			// 保存实体
			travelSpot.setTravel(travel);

			travelSpot = travelSpotDao.save(travelSpot);

			// 相互设置
			travel.getSpots().add(travelSpot);
		}

		travelSpot.getPhotos().add(photo);
		photo.setTravelSpot(travelSpot);

		return photo;
	}

	@Override
	public Travel changeTravelDesc(Long id, String description) {
		User me = UserUtil.getCurrentUser(userManager);
		Travel travel = travelDao.get(id);
		checkMyTravel(travel, me);
		travel.setDescription(description);
		return travel;
	}

	@Override
	public TravelSpot changeSpot(Long id, TravelSpot spot) {
		User me = UserUtil.getCurrentUser(userManager);
		TravelSpot travelSpot = travelSpotDao.get(id);
		checkMyTravel(travelSpot.getTravel(), me);

		if (null != spot.getTitle()) {
			travelSpot.setTitle(spot.getTitle());
		}
		if (null != spot.getDescription()) {
			travelSpot.setDescription(spot.getDescription());
		}
		if (null != spot.getAddress()) {
			travelSpot.setAddress(spot.getAddress());
		}
		if (null != spot.getTimeStart()) {
			travelSpot.setTimeStart(spot.getTimeStart());
		}

		return travelSpot;
	}

	@Override
	public TravelSpot getTravelSpot(Long id) {
		return travelSpotDao.get(id);
	}

	@Override
	public Travel addTravelPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		Travel travel = travelDao.get(id);
		checkMyTravel(travel, me);

		log.debug("add photos size: " + photos.size());
		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.getPhoto(photoId);
			PhotoUtil.checkMyPhoto(photo, me);
			addTravelPhoto(travel, photo);
		}
		return travel;
	}

	@Override
	public Travel removeTravelPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		Travel travel = travelDao.get(id);
		checkMyTravel(travel, me);

		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.getPhoto(photoId);
			PhotoUtil.checkMyPhoto(photo, me);
			removePhoto(travel, photo);
		}
		return travel;
	}

	/**
	 * 相互删除photo原有travel信息
	 * 
	 * @param travel
	 * @param photo
	 */
	private void removePhoto(Travel travel, Photo photo) {
		if(null !=photo.getTravelSpot() ) {
			Travel photoTravel = photo.getTravelSpot().getTravel();
			if (photoTravel.getId().equals(travel.getId())) {
				photo.getTravelSpot().getPhotos().remove(photo);
				photo.setTravelSpot(null);
			}
		}
		
	}
	
	private void checkMyTravel(Travel travel, User me) {

		if (!travel.getUser().getId().equals(me.getId())) {
			throw new AccessDeniedException("Travel (id=" + travel.getId()
					+ ") is not belong to you");
		}
	}

	@Override
	public TravelSpot createTravelSpot(Long id, TravelSpot spot) {
		User me = UserUtil.getCurrentUser(userManager);
		Travel travel = travelDao.get(id);
		checkMyTravel(travel, me);

		TravelSpot travelSpot = new TravelSpot();
		travelSpot.setTimeStart(spot.getTimeStart());
		travelSpot.setAddress(spot.getAddress());
		travelSpot.setTitle(spot.getTitle());
		travelSpot.setDescription(spot.getDescription());

		// 保存实体
		travelSpot.setTravel(travel);
		travelSpot = travelSpotDao.save(travelSpot);

		// 相互设置
		travel.getSpots().add(travelSpot);

		return travelSpot;
	}

	@Override
	public Travel addSpotPhotos(Long id, List<Long> photos) {
		User me = UserUtil.getCurrentUser(userManager);
		TravelSpot spot = travelSpotDao.get(id);
		checkMyTravel(spot.getTravel(), me);

		Photo photo = null;
		for(Long photoId : photos) {
			photo = photoManager.getPhoto(photoId);
//			PhotoUtil.checkMyPhoto(photo, me);
			addSpotPhoto(spot, photo);
		}
		return spot.getTravel();
	}

	private boolean addSpotPhoto(TravelSpot spot, Photo photo) {
		if(null != photo.getTravelSpot()) {
			if(spot.equals(photo.getTravelSpot())) {
				return true;
			}else {
//				log.debug("spot id: " + photo.getTravelSpot().getId());
				photo.getTravelSpot().getPhotos().remove(photo);
				photoDao.save(photo);
//				log.debug("add spot photo: " + photo.getId());
//				for(Photo p : photo.getTravelSpot().getPhotos()) {
//					log.debug("spot photo: " + p.getId());
//				}
			}
		}
		photo.setTravelSpot(spot);
		spot.getPhotos().add(photo);
		return true;
	}

	@Override
	public Travel deleteSpot(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		TravelSpot spot = travelSpotDao.get(id);
		checkMyTravel(spot.getTravel(), me);
		
		Travel travel = spot.getTravel();
		// 从travel中移除
		travel.getSpots().remove(spot);
		// 从旗下photo中移除
		for(Photo photo : spot.getPhotos()) {
			photo.setTravelSpot(null);
		}
		// 然后删除
		travelSpotDao.remove(spot);
		return travel;
	}

}
