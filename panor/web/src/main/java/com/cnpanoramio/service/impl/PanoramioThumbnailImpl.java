package com.cnpanoramio.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PanoramioThumbnailService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;
import com.cnpanoramio.service.json.BoundSize;
import com.cnpanoramio.service.json.PhotoThumbnail;
import com.cnpanoramio.service.json.PhotoThumbnails;
import com.cnpanoramio.webapp.controller.ExploreWorldController;

@Service("panorThumbService")
@Transactional
public class PanoramioThumbnailImpl implements PanoramioThumbnailService {

	private transient final Log log = LogFactory
			.getLog(PanoramioThumbnailImpl.class);

	private PhotoDao photoDao;
	private PhotoService photoService = null;

	@Override
	public PhotoThumbnails getPhotoThumbnails(BoundSize boundSize) {

		PhotoThumbnails photoThumbs = new PhotoThumbnails();
		Collection<PhotoThumbnail> cPhotoThumbs = new ArrayList<PhotoThumbnail>();
		List<Photo> photos = photoDao.getAll();

		if (log.isDebugEnabled()) {
			log.debug(photos.size());
		}

		for (Photo photo : photos) {
			Point point = photo.getGpsPoint();
			if (log.isDebugEnabled()) {
				log.debug("PhotoId:" + photo.getId() + " LatLng:"
						+ point.getGeoLat() + point.getGeoLng());
			}
			if (point.getGeoLat() < boundSize.getBoundNELat()
					&& point.getGeoLat() > boundSize.getBoundSWLat()
					&& point.getGeoLng() > boundSize.getBoundSWLng()
					&& point.getGeoLng() < boundSize.getBoundNELng()) {
				PhotoThumbnail thumb = new PhotoThumbnail();
				thumb.setPhotoId(photo.getId());
				thumb.setLat(point.getGeoLat());
				thumb.setLng(point.getGeoLng());
				cPhotoThumbs.add(thumb);
			}
		}

		photoThumbs.setThumbnails(cPhotoThumbs);
		return photoThumbs;
	}

	@Override
	public Response read(Long photoId) {
		Response response = photoService.read(photoId);
		return response;

	}

	public PhotoDao getPhotoDao() {
		return photoDao;
	}

	@Autowired
	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	public PhotoService getPhotoService() {
		return photoService;
	}

	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}

}
