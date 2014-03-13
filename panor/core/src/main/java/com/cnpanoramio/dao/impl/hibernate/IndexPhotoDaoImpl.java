package com.cnpanoramio.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.IndexPhotoDao;
import com.cnpanoramio.domain.IndexPhoto;

@Repository("indexPhotoDao")
public class IndexPhotoDaoImpl extends GenericDaoHibernate<IndexPhoto, Long> implements IndexPhotoDao {

	public IndexPhotoDaoImpl() {
		super(IndexPhoto.class);
	}

	@Override
	public Collection<IndexPhoto> save(Collection<IndexPhoto> photos) {
		ArrayList<IndexPhoto> iPhotos= new ArrayList<IndexPhoto>();
		
		for(IndexPhoto photo : photos) {
			iPhotos.add(save(photo));
		}
		return iPhotos;
	}
	

}
