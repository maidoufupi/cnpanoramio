package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoPanoramioDao;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PanoramioResponse.PhotoPanoramio;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoPanoramioIndexService;

@Service("panoramioIndexService")
@Transactional
public class PhotoPanoramioIndexServiceImpl implements
		PhotoPanoramioIndexService {
	
	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PhotoPanoramioIndexDao panorIndexDao;
	
	@Autowired
	private PhotoPanoramioDao photoPanoramioDao;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private UserSettingsDao userDao;

	@Override
	public boolean updatePanoramioIndex() {
		return panorIndexDao.updatePhotoPanoramioIndex();
	}

	@Override
	public List<PhotoPanoramio> getPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height) {
		List<Photo> photos = panorIndexDao.getPhotoPanoramio(swLat, swLng, neLat, neLng, level, vendor, width,
				height);
		log.debug("get photo size: " + photos.size());
		List<PhotoPanoramio> pps = fillPhotoPanoramioList(photos);
		log.debug("get pp size: " + pps.size());
		return pps;
	}

	@Override
	public List<PhotoPanoramio> getUserPhotoPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height, Long userId) {
		return fillPhotoPanoramioList(panorIndexDao.getUserPhotoPanoramio(swLat, swLng, neLat, neLng,
				level, vendor, width, height, userId, false));
	}

	@Override
	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng,
			Double neLat, Double neLng, int level, MapVendor vendor, int width,
			int height, Long userId) {
		return fillPhotoPanoramioList(panorIndexDao.getUserPhotoPanoramio(swLat, swLng, neLat, neLng,
				level, vendor, width, height, userId, true));
	}

	@Override
	public boolean updatePhotoLatestIndex() {
		return panorIndexDao.updatePhotoLatestIndex();
	}

	@Override
	public List<PhotoPanoramio> getLatestPanoramio(Double swLat, Double swLng,
			Double neLat, Double neLng, int level, MapVendor vendor, int width,
			int height) {
		return fillPhotoPanoramioList(panorIndexDao.getLatestPanoramio(swLat, swLng, neLat, neLng,
				level, vendor, width, height));
	}
	
	private List<PhotoPanoramio> fillPhotoPanoramioList(List<Photo> photos) {
		List<PhotoPanoramio> pps = new ArrayList<PhotoPanoramio>();
		PhotoPanoramio pp;
		for(Photo photo : photos) {
			pp = fillPhotoPanoramio(photo);
			if(null != pp) {
				pps.add(pp);
			}
		}
		return pps;
	}

	private PhotoPanoramio fillPhotoPanoramio(Photo photo) {
		PhotoPanoramio pp = new PhotoPanoramio();
		pp.setPhotoId(photo.getId());
		Point point = photo.getGpsPoint();
		if(null != point) {
			pp.setLat(point.getLat());
			pp.setLng(point.getLng());
			pp.setAlt(point.getAlt());
			pp.setAddress(point.getAddress());
		}else {
			return null;
		}
		pp.setRating(photo.getRating());
		pp.setTitle(photo.getTitle());
		pp.setCreateDate(photo.getCreateDate());
		pp.setUserId(photo.getOwner().getId());
		UserSettings user = userDao.get(pp.getUserId());
		pp.setUsername(user.getName());
		
		return pp;
	}

}
