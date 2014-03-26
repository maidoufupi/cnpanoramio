package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
import com.cnpanoramio.domain.Point;

@Repository("photoPanoramioIndexDao")
public class PhotoPanoramioIndexDaoImpl extends GenericDaoHibernate<PhotoPanoramioIndex,  PhotoPanoramioIndexPK> implements PhotoPanoramioIndexDao {

	public PhotoPanoramioIndexDaoImpl() {
		super(PhotoPanoramioIndex.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<PhotoPanoramio> getPhotoPanoramio(Point sw, Point ne,
			int level, MapVendor mVendor, int width, int height) {

		Query query = getSession().createSQLQuery(
				"CALL getPhotoPanoramioIndex(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height, :latest)")
				.addEntity(PhotoPanoramio.class)
				.setParameter("swLat", sw.getLat())
				.setParameter("swLng", sw.getLng())
				.setParameter("neLat", ne.getLat())
				.setParameter("neLng", ne.getLng())
				.setParameter("level", level)
				.setParameter("vendor", mVendor.name())
				.setParameter("width", width)
				.setParameter("height", height)
				.setParameter("latest", false);
			 
		List result = query.list();

		return result;
	}

	@Override
	public boolean updatePhotoPanoramioIndex() {
		Query query = getSession().createSQLQuery(
				"CALL updatePhotoPanoramioIndex()");
		query.executeUpdate();
		return true;
	}

	@Override
	public List<PhotoPanoramio> getUserPhotoPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height, Long userId) {
		Query query = getSession().createSQLQuery(
				"CALL getUserPhotoPanoramio(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height, :userId)")
				.addEntity(PhotoPanoramio.class)
				.setParameter("swLat", swLat)
				.setParameter("swLng", swLng)
				.setParameter("neLat", neLat)
				.setParameter("neLng", neLng)
				.setParameter("level", level)
				.setParameter("vendor", vendor.name())
				.setParameter("width", width)
				.setParameter("height", height)
				.setParameter("userId", userId);
			 
		return query.list();
	}

	@Override
	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng,
			Double neLat, Double neLng, int level, MapVendor vendor, int width,
			int height, Long userId) {
		Query query = getSession().createSQLQuery(
				"CALL getUserFavPanoramio(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height, :userId)")
				.addEntity(PhotoPanoramio.class)
				.setParameter("swLat", swLat)
				.setParameter("swLng", swLng)
				.setParameter("neLat", neLat)
				.setParameter("neLng", neLng)
				.setParameter("level", level)
				.setParameter("vendor", vendor.name())
				.setParameter("width", width)
				.setParameter("height", height)
				.setParameter("userId", userId);
			 
		return query.list();
	}

	@Override
	public List<PhotoPanoramio> getLatestPanoramio(Double swLat, Double swLng,
			Double neLat, Double neLng, int level, MapVendor vendor, int width,
			int height) {
		Query query = getSession().createSQLQuery(
				"CALL getPhotoPanoramioIndex(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height, :latest)")
				.addEntity(PhotoPanoramio.class)
				.setParameter("swLat", swLat)
				.setParameter("swLng", swLng)
				.setParameter("neLat", neLat)
				.setParameter("neLng", neLng)
				.setParameter("level", level)
				.setParameter("vendor", vendor.name())
				.setParameter("width", width)
				.setParameter("height", height)
				.setParameter("latest", true);
			 
		return query.list();
	}

	@Override
	public boolean updatePhotoLatestIndex() {
		Query query = getSession().createSQLQuery("CALL updatePhotoLatestIndex()");
		query.executeUpdate();
		return true;
	}
}
