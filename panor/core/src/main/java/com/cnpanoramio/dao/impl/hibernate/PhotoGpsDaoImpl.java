package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.domain.PhotoGps;

@Repository("photoGpsDao")
public class PhotoGpsDaoImpl extends GenericDaoHibernate<PhotoGps,  PhotoGps.PhotoGpsPK> implements PhotoGpsDao {

	public PhotoGpsDaoImpl() {
		super(PhotoGps.class);
		// TODO Auto-generated constructor stub
	}

	public List<PhotoGps> getAll(Long id) {
		Query gpsQuery = getSession().createQuery("from PhotoGps p where photo_id = :id");
		
		gpsQuery.setParameter("id", id);

		return gpsQuery.list();
	}

	
}
