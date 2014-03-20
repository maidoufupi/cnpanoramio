package com.cnpanoramio.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PhotoPanoramioIndexService;

@Service("panoramioIndexService")
@Transactional
public class PhotoPanoramioIndexServiceImpl implements PhotoPanoramioIndexService {

	@Autowired
	private PhotoPanoramioIndexDao panorIndexDao;

	@Override
	public boolean updatePanoramioIndex() {
		return panorIndexDao.updatePhotoPanoramioIndex();
	}

	@Override
	public List<PhotoPanoramio> getPanoramio(Point sw, Point ne, int level,
			MapVendor vendor, int width, int height) {
		return panorIndexDao.getPhotoPanoramio(sw, ne, level, vendor, width, height);
	}

	@Override
	public List<PhotoPanoramio> getUserPhotoPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height, Long userId) {
		return panorIndexDao.getUserPhotoPanoramio(swLat, swLng, neLat, neLng, level, vendor, width, height, userId);
	}

	@Override
	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng,
			Double neLat, Double neLng, int level, MapVendor vendor, int width,
			int height, Long userId) {
		return panorIndexDao.getUserFavPanoramio(swLat, swLng, neLat, neLng, level, vendor, width, height, userId);
	}	

}
