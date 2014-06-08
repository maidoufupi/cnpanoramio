package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.PhotoPanoramioDao;
import com.cnpanoramio.domain.PhotoPanoramio;

@Repository("photoPanoramioDao")
public class PhotoPanoramioDaoImpl extends GenericDaoHibernate<PhotoPanoramio,  Long> implements PhotoPanoramioDao {

	public PhotoPanoramioDaoImpl() {
		super(PhotoPanoramio.class);
		// TODO Auto-generated constructor stub
	}
}
