package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.IndexPhotoDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.IndexPhoto;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.service.IndexService;
import com.cnpanoramio.utils.PhotoUtil;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private IndexPhotoDao indexPhotoDao;

	@Autowired
	private PhotoDao photoDao;

	@Override
	public boolean setIndexPhotos(Collection<Long> photoIds) {

		List<IndexPhoto> photos;;

		IndexPhoto indexPhoto;
		
		photos = indexPhotoDao.getAllDistinct();
		for(IndexPhoto photo : photos) {
			indexPhotoDao.remove(photo);				
		}
		
		photos = new ArrayList<IndexPhoto>();
		try {
			for (Long id : photoIds) {
				indexPhoto = new IndexPhoto();
				indexPhoto.setPhoto(photoDao.get(id));
				photos.add(indexPhoto);
			}
			indexPhotoDao.save(photos);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Collection<PhotoCameraInfo> getIndexPhotos() {
		Collection<PhotoCameraInfo> pcinfos = new ArrayList<PhotoCameraInfo>();
		List<IndexPhoto> photos;
		photos = indexPhotoDao.getAllDistinct();
		PhotoCameraInfo pcinfo;
		for(IndexPhoto photo : photos) {
			pcinfo = PhotoUtil.transformCameraInfo(photo.getPhoto());
			pcinfos.add(pcinfo);
		}
		return pcinfos;
	}
}
