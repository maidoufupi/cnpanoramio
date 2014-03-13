package com.cnpanoramio.dao;

import java.util.Collection;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.IndexPhoto;

public interface IndexPhotoDao extends GenericDao<IndexPhoto, Long> {
	
	public Collection<IndexPhoto> save(Collection<IndexPhoto> photos);

}
