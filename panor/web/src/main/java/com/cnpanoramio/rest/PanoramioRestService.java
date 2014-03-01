package com.cnpanoramio.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.dao.PhotoGisIndexDao;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PhotoGisIndexService;
import com.cnpanoramio.service.json.BoundSize;
import com.cnpanoramio.service.json.PhotoThumbnail;
import com.cnpanoramio.service.json.PhotoThumbnails;

@Controller
@RequestMapping("/api/rest/panoramio")
public class PanoramioRestService {
	
    private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private PhotoGisIndexService gisindexService;

	@RequestMapping(value = "/photo", method = RequestMethod.POST)
	@ResponseBody
	public PhotoThumbnails getThumbs(@RequestBody BoundSize boundSize) {
		PhotoThumbnails photoThumbs = new PhotoThumbnails();
		Collection<PhotoThumbnail> cPhotoThumbs = new ArrayList<PhotoThumbnail>();
		
		if (log.isDebugEnabled()) {
			log.debug(boundSize.getBoundSWLat() + ", " + boundSize
					.getBoundSWLng() + ", " + boundSize.getBoundNELat() + ", "
					+ boundSize.getBoundNELng() + ", " + (int) (10 - Math.ceil((boundSize.getZoomLevel() + 1) / 2)));
		}
		// gaode zoom level 和 PhotoGisIndex zoomLevel对应关系
		List<PhotoGisIndex> photos = gisindexService
				.getBoundPhotos(
						new Point(boundSize.getBoundSWLat(), boundSize
								.getBoundSWLng()),
						new Point(boundSize.getBoundNELat(), boundSize
								.getBoundNELng()), (int) (10 - Math.ceil((boundSize.getZoomLevel() + 1) / 2)),
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
			
}
