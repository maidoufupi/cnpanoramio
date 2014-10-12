package com.cnpanoramio.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Views;
import com.cnpanoramio.service.ViewsManager;

@Service
@Transactional
public class ViewsManagerImpl implements ViewsManager {

	@Autowired
	private ViewsDao viewsDao;
	
	@Override
	public void view(Long photoId, String appId) {
		viewsDao.view(photoId, appId);

	}

	@Override
	public int getViewsCount(Long photoId, String appId) {
		return viewsDao.getViewsCount(photoId, appId);
	}

	@Override
	public int getViewsCount(Long photoId) {
		return viewsDao.getViewsCount(photoId);				
	}

	@Override
	public List<Views> getViewsList(Long photoId, String appId) {
		return viewsDao.getViewsList(photoId, appId);
	}

	@Override
	public List<Views> getViewsList(Long photoId) {
		return viewsDao.getViewsList(photoId);
	}

	@Override
	public List<Views> getViewsList(Long photoId, Date date) {
		return viewsDao.getViewsList(photoId, date);
	}

	@Override
	public Views getViews(Long photoId, String appId, Date date) {
		return viewsDao.getViews(photoId, appId, date);
	}

	@Override
	public int getViewsCount(Long photoId, Date date) {
		return viewsDao.getViewsCount(photoId, date);
	}

	@Override
	public void removePhotoViews(Photo photo) {
		viewsDao.removePhotoViews(photo);
		
	}

}
