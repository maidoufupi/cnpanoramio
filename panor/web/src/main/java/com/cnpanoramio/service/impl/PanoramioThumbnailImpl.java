package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoGisIndexDao;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PanoramioThumbnailService;
import com.cnpanoramio.service.json.BoundSize;
import com.cnpanoramio.service.json.PhotoThumbnail;
import com.cnpanoramio.service.json.PhotoThumbnails;

@Service("panorThumbService")
@Transactional
public class PanoramioThumbnailImpl implements PanoramioThumbnailService {

	private transient final Log log = LogFactory
			.getLog(PanoramioThumbnailService.class);
	
	@Autowired
	private PhotoGisIndexDao photoDao;

	@Override
	public PhotoThumbnails getPhotoThumbnails(BoundSize boundSize) {

		PhotoThumbnails photoThumbs = new PhotoThumbnails();
		Collection<PhotoThumbnail> cPhotoThumbs = new ArrayList<PhotoThumbnail>();
		// gaode zoom level 和 PhotoGisIndex zoomLevel对应关系
		List<PhotoGisIndex> photos = photoDao
				.getBoundPhotos(
						new Point(boundSize.getBoundSWLat(), boundSize
								.getBoundSWLng()),
						new Point(boundSize.getBoundNELat(), boundSize
								.getBoundNELng()), (int)Math.round(10 - Math.ceil(boundSize.getZoomLevel() / 2)),
						boundSize.getWidth(), boundSize.getHeight());

		if (log.isDebugEnabled()) {
			log.debug(photos.size());
		}

		for (PhotoGisIndex photo : photos) {
			PhotoThumbnail thumb = new PhotoThumbnail();
			thumb.setPhotoId(photo.getPhotoId());
			thumb.setLat(photo.getPk().getLat());
			thumb.setLng(photo.getPk().getLng());
			cPhotoThumbs.add(thumb);
		}

		photoThumbs.setThumbnails(cPhotoThumbs);
		return photoThumbs;
	}

	@Override
	public Response read(Long photoId) {
		Response response = null;// photoService.read(photoId);
		return response;

	}
}
