package com.cnpanoramio.service.impl;

import java.util.Iterator;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.RecycleDao;
import com.cnpanoramio.domain.Recycle;
import com.cnpanoramio.domain.Recycle.RecycleType;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.service.UserSettingsManager;

@Service
@Transactional
public class RecycleManagerImpl extends GenericManagerImpl<Recycle, Long> implements RecycleManager {

	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	private TravelService travelService;
	
	@Autowired
	private UserSettingsManager userSettingsManager;
	
	private RecycleDao recycleDao;
	
	public RecycleDao getRecycleDao() {
		return recycleDao;
	}

	@Autowired
	public void setRecycleDao(RecycleDao recycleDao) {
		this.recycleDao = recycleDao;
		this.dao = recycleDao;
	}
	
	@Override
	public Recycle saveRecycle(User user, RecycleType type, Long entityId) {
		UserSettings settings = userSettingsManager.get(user.getId());
		Recycle recycle = new Recycle();
		recycle.setUser(settings);
		recycle.setRecyType(type);
		recycle.setRecyId(entityId);
		recycle = recycleDao.persist(recycle);
		return recycle;
	}

	@Override
	public List<Recycle> getUserRecycleBin(User user) {
		return recycleDao.getUserRecycle(user.getId());		
	}
	
	@Override
	public void cancelRecycle(Long id) {
		Recycle recycle = recycleDao.get(id);
		if(recycle.getRecyType() == RecycleType.photo ) {
			try {
				photoService.cancelDelete(recycle.getRecyId());
			}catch(DataAccessException ex) {
			}				
		}else if(recycle.getRecyType() == RecycleType.travel) {
			try {
				travelService.cancelDeleteTravel(recycle.getRecyId());
			}catch(DataAccessException ex) {
			}	
		}
		recycleDao.remove(recycle);
	}
	
	@Override
	public void removeRecycle(Long id) {
		Recycle recycle = recycleDao.get(id);
		if(recycle.getRecyType() == RecycleType.photo) {
//			try {
				photoService.removePhoto(recycle.getRecyId());
//			}catch(DataAccessException ex) {
//			}				
		}else if(recycle.getRecyType() == RecycleType.travel) {
//			try {
				travelService.removeTravel(recycle.getRecyId());
//			}catch(DataAccessException ex) {
//			}
		}
		recycleDao.remove(recycle);
	}
	
	@Override
	public void emptyRecycleBin(Long id) {
		
		UserSettings settings = userSettingsManager.get(id);
		Iterator<Recycle> iter = settings.getRecycle().iterator();
		while(iter.hasNext()) {
			Recycle recycle = iter.next();
			removeRecycle(recycle.getId());
			// 要从userSettings中删除级联 问题， 待研究
			iter.remove();
		}		
	}

}
