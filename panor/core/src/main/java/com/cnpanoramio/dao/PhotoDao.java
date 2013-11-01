package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Photo;

public interface PhotoDao extends GenericDao<Photo, Long>{

	public List<Photo> getUserPhotos(User user);
	
	
}
