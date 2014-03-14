package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoGisIndexDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;

@Deprecated
@Repository("photoGisIndexDao")
public class PhotoGisIndexDaoImpl extends GenericDaoHibernate<PhotoGisIndex, Long> implements PhotoGisIndexDao {

	public PhotoGisIndexDaoImpl() {
		super(PhotoGisIndex.class);
	}

	public List<Photo> getUserPhotos(User user) {
		Query photoListQuery = getSession().createQuery("from Photo p where owner = :owner");
				
		photoListQuery.setParameter("owner", user);

		return photoListQuery.list();
	}

	@Override
	public List<PhotoGisIndex> getBoundPhotos(Point sw, Point ne,
			int zoomLevel, int width, int height) {
		Query query = getSession().createSQLQuery(
				"CALL getPhotoGisIndex(:swLat, :swLng, :neLat, :neLng, :zoomLevel, :width, :height)")
				.addEntity(PhotoGisIndex.class)
				.setParameter("swLat", sw.getLat())
				.setParameter("swLng", sw.getLng())
				.setParameter("neLat", ne.getLat())
				.setParameter("neLng", ne.getLng())
				.setParameter("zoomLevel", zoomLevel)
				.setParameter("width", width)
				.setParameter("height", height);
			 
		List result = query.list();

		return result;
	}

	@Override
	public boolean updateGisIndex() {
		Query query = getSession().createSQLQuery(
				"CALL updatePhotoGisIndex()");
		query.executeUpdate();
		return true;
	}
}
