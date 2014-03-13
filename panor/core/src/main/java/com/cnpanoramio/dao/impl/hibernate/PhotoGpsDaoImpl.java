package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.domain.PhotoGps;

@Repository("photoGpsDao")
public class PhotoGpsDaoImpl extends GenericDaoHibernate<PhotoGps,  PhotoGps.PhotoGpsPK> implements PhotoGpsDao {

	public PhotoGpsDaoImpl() {
		super(PhotoGps.class);
		// TODO Auto-generated constructor stub
	}

}
