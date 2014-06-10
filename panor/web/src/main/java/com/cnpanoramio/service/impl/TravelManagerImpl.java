package com.cnpanoramio.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private UserSettingsDao userSettingsDao;
	
	@Autowired
	private PhotoManager photoManager;
	
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
		if(null == tra) {
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
		for(Travel travel: travels) {
			Travel t = new Travel();
			BeanUtils.copyProperties(travel, t, new String[]{"spots", "user"});
			list.add(t);
		}
		return list;
	}

	@Override
	public Photo addTravelPhoto(Long travelId, Photo photo) {
		
		photo = photoManager.getPhoto(photo.getId());
		Travel travel = travelDao.get(travelId);
		
		TravelSpot travelSpot = null;
		PhotoDetails detail = photo.getDetails();
		Date photoDate = null;
		if(null != detail) {
			photoDate = detail.getDateTime();
			if(null != photoDate) {
				String tDate = dateFormat.format(photoDate);
				for(TravelSpot spot : travel.getSpots()) {
					if(dateFormat.format(spot.getTimeStart()).equalsIgnoreCase(tDate)) {
						travelSpot = spot;
						break;
					}
				}
			}
		}
		
		Travel photoTravel = photo.getTravel();
		if(null != photoTravel) {
			photoTravel.getSpot().getPhotos().remove(photo);
			for(TravelSpot tspot: photoTravel.getSpots()) {
				tspot.getPhotos().remove(photo);
			}
		}
		
		if(null == travelSpot) {
			if(null != photoDate) {
				travelSpot = new TravelSpot();
				travelSpot.setTimeStart(photoDate);
				// 保存实体
				travelSpot.setTravel(travel);
				travelSpot = travelSpotDao.save(travelSpot);
				// 相互设置
				travel.getSpots().add(travelSpot);
			}else {
				travelSpot = travel.getSpot();
				if(null == travelSpot) {
					travelSpot = new TravelSpot();
					// 保存实体
					travelSpot.setTravel(travel);
					travelSpot = travelSpotDao.save(travelSpot);
					// 相互设置
					travel.getSpots().add(travelSpot);
				}
			}
		}

		photo.setTravel(travel);
		travelSpot.getPhotos().add(photo);
		return photo;
	}

	@Override
	public Travel changeTravelDesc(Long id, String description) {
		Travel travel = travelDao.get(id);
		travel.setDescription(description);
		return travel;
	}

	@Override
	public TravelSpot changeSpot(Long id, TravelSpot spot) {
		TravelSpot travelSpot = travelSpotDao.get(id);
		if(null != spot.getTitle()) {
			travelSpot.setTitle(spot.getTitle());
		}
		if(null != spot.getDescription()) {
			travelSpot.setDescription(spot.getDescription());
		}
		if(null != spot.getAddress()) {
			travelSpot.setAddress(spot.getAddress());
		}
		
		return travelSpot;
	}

	@Override
	public TravelSpot getTravelSpot(Long id) {
		return travelSpotDao.get(id);
	}		

}
