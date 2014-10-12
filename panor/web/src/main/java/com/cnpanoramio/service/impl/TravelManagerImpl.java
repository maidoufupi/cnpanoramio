package com.cnpanoramio.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.TravelManager;

@Service
@Transactional
public class TravelManagerImpl extends GenericManagerImpl<Travel, Long> implements TravelManager {

	protected transient final Log log = LogFactory.getLog(getClass());

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

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
	
	@Autowired
	private RecycleManager recycleManager;

	public TravelDao getTravelDao() {
		return travelDao;
	}

	@Autowired
	public void setTravelDao(TravelDao travelDao) {
		this.travelDao = travelDao;
		this.dao = travelDao;
	}

	@Override
	public Travel getTravel(Long travelId) {
		return travelDao.get(travelId);
	}
	
	@Override
	public List<Travel> getTravels(User user) {
		UserSettings settings = userSettingsDao.get(user.getId());
		List<Travel> travels = new ArrayList<Travel>();
		for(Travel travel : settings.getTravels()) {
			if(!travel.isDeleted()) {
				travels.add(travel);
			}
		}
		return travels;
	}

	@Override
	public Travel createMyTravel(User user, String travel) {
		UserSettings settings = userSettingsDao.get(user.getId());
		// 如果名称已经存在，则不创建
		Travel tra = travelDao.getByName(user.getId(), travel);
		if (null == tra) {
			tra = new Travel(travel);
			// 保存实体
			tra = travelDao.persist(tra);
			// 相互设置
			tra.setUser(settings);
			settings.getTravels().add(tra);
		}
		return tra;
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
				photoDao.save(photo);
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
					if (null != spot.getTimeStart() && dateFormat.format(spot.getTimeStart()).equalsIgnoreCase(tDate)) {
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

			travelSpot = travelSpotDao.persist(travelSpot);

			// 相互设置
			travel.getSpots().add(travelSpot);
		}

		travelSpot.getPhotos().add(photo);
		photo.setTravelSpot(travelSpot);

		return photo;
	}

	@Override
	public Travel changeTravelDesc(Long id, String description) {
		Travel travel = travelDao.get(id);
		travel.setDescription(description);
		return travel;
	}
	
	@Override
	public Travel changeTravelName(Long id, String name) {
		Travel travel = travelDao.get(id);
		travel.setTitle(name);
		return travel;
	}

	@Override
	public TravelSpot changeSpot(Long id, TravelSpot spot) {

		TravelSpot travelSpot = travelSpotDao.get(id);
		
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
		
		Travel travel = this.get(id);
		
		log.debug("add photos size: " + photos.size());
		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.get(photoId);
			addTravelPhoto(travel, photo);
		}
		return travel;
	}

	@Override
	public Travel removeTravelPhotos(Long id, List<Long> photos) {

		Travel travel = this.get(id);
		
		Photo photo = null;
		for (Long photoId : photos) {
			photo = photoManager.get(photoId);
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
	
	@Override
	public TravelSpot createTravelSpot(Long id, TravelSpot spot) {

		Travel travel = this.get(id);
		
		TravelSpot travelSpot = new TravelSpot();
		travelSpot.setTimeStart(spot.getTimeStart());
		travelSpot.setAddress(spot.getAddress());
		travelSpot.setTitle(spot.getTitle());
		travelSpot.setDescription(spot.getDescription());

		// 保存实体
		travelSpot.setTravel(travel);
		travelSpot = travelSpotDao.persist(travelSpot);

		// 相互设置
		travel.getSpots().add(travelSpot);

		return travelSpot;
	}

	@Override
	public Travel addSpotPhotos(Long id, List<Long> photos) {
		TravelSpot spot = travelSpotDao.get(id);
		Photo photo = null;
		for(Long photoId : photos) {
			photo = photoManager.get(photoId);
			addSpotPhoto(spot, photo);
		}
		return spot.getTravel();
	}

	public void addSpotPhoto(TravelSpot spot, Photo photo) {
		if(null != photo.getTravelSpot()) {
			if(spot.equals(photo.getTravelSpot())) {
				
			}else {
				photo.getTravelSpot().getPhotos().remove(photo);
				photoDao.save(photo);
			}
		}
		photo.setTravelSpot(spot);
		spot.getPhotos().add(photo);
	}

	@Override
	public Travel deleteSpot(Long id) {
		TravelSpot spot = travelSpotDao.get(id);
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

	/**
	 * 将旅行相册移动到回收站，如果旅行中有图片则全部放入回收站，如果没有图片则直接删除旅行相册
	 */
	@Override
	public void deleteTravel(Long id) {

		Travel travel = this.get(id);
		
		travel.setDeleted(true);
		
		boolean existPhoto = false;
		for(TravelSpot spot : travel.getSpots()) {
			for(Photo photo : spot.getPhotos()) {
				photoManager.delete(photo.getId());
				existPhoto = true;
			}
		}
		if(!existPhoto) {
			this.removeTravel(travel.getId());
		}
		
//		recycleManager.saveRecycle(me, Recycle.RecycleType.travel, travel.getId());
	}

	/**
	 * 删除旅行相册
	 */
	@Override
	public void removeTravel(Long id) {
		Travel travel = this.get(id);

		for (TravelSpot spot : travel.getSpots()) {
			for (Photo photo : spot.getPhotos()) {
				// 删除图片
				photoManager.removePhoto(photo.getId());
			}
		}
		// 删除旅行
		travelDao.remove(travel);
	}

	@Override
	public void cancelDeleteTravel(Long id) {
		Travel travel = this.get(id);

		travel.setDeleted(false);
		for(TravelSpot spot : travel.getSpots()) {
			for(Photo photo : spot.getPhotos()) {
				photo.setDeleted(false);
			}
		}
	}
}
