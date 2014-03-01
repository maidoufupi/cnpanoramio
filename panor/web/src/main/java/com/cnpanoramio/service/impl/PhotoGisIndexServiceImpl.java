package com.cnpanoramio.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoGisIndexDao;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PhotoGisIndexService;

@Service("gisindexService")
@Transactional
public class PhotoGisIndexServiceImpl implements PhotoGisIndexService {

	@Autowired
	private PhotoGisIndexDao photoDao;

	@Override
	public boolean updateGisIndex() {
		return photoDao.updateGisIndex();
	}

	public List<PhotoGisIndex> getBoundPhotos(Point sw, Point ne,
			int zoomLevel, int width, int height) {
		return photoDao.getBoundPhotos(sw, ne, zoomLevel, width, height);
	}

}
