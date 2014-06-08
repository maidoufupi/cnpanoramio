package com.cnpanoramio.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoLatestIndex;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
import com.cnpanoramio.domain.Point;

@Repository("photoPanoramioIndexDao")
public class PhotoPanoramioIndexDaoImpl extends
		GenericDaoHibernate<PhotoPanoramioIndex, PhotoPanoramioIndexPK>
		implements PhotoPanoramioIndexDao {

	private Double conMeasure = 40D;
	private Integer minPix = 34;

	public PhotoPanoramioIndexDaoImpl() {
		super(PhotoPanoramioIndex.class);
		// TODO Auto-generated constructor stub
	}

	// @Override
	// public List<PhotoPanoramio> getPhotoPanoramio(Point sw, Point ne,
	// int level, MapVendor mVendor, int width, int height) {
	//
	// Query query = getSession().createSQLQuery(
	// "CALL getPhotoPanoramioIndex(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height, :latest)")
	// .addEntity(PhotoPanoramio.class)
	// .setParameter("swLat", sw.getLat())
	// .setParameter("swLng", sw.getLng())
	// .setParameter("neLat", ne.getLat())
	// .setParameter("neLng", ne.getLng())
	// .setParameter("level", level)
	// .setParameter("vendor", mVendor.name())
	// .setParameter("width", width)
	// .setParameter("height", height)
	// .setParameter("latest", false);
	//
	// List result = query.list();
	//
	// return result;
	// }

	@Override
	public List<Photo> getPhotoPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng,
			int level, MapVendor mVendor, int width, int height) {

		Double heightRate = height / (neLat - swLat),
				widthRate = width / (neLng - swLng);
		Double lSouth, lWest, lNorth, lEast;

		Double lMeasure;
		lMeasure = conMeasure / Math.pow(2D, level);
		lSouth = swLat - (swLat % lMeasure);
		lWest = swLng - (swLng % lMeasure);
		lNorth = neLat - (neLat % lMeasure);
		lEast = neLng - (neLng % lMeasure);

		log.debug("getPhotoPanoramio [" + lSouth + ", " + lWest + "; " + lNorth + ", " + lEast + "]");
		Criteria criteria = getSession()
				.createCriteria(PhotoPanoramioIndex.class)
				.add(Restrictions.eq("pk.level", level))
				.add(Restrictions.le("pk.south", lNorth))
				.add(Restrictions.ge("pk.south", lSouth));

		if (lEast > lWest) {
			criteria.add(Restrictions.le("pk.west", lEast)).add(
					Restrictions.ge("pk.west", lWest));
		} else {
			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("pk.west", lWest),
					Restrictions.le("pk.west", 180D)), Restrictions.and(
					Restrictions.ge("pk.west", -180D),
					Restrictions.le("pk.west", lEast))));
		}

		return filterPanoramioIndex(criteria.list(), widthRate, heightRate);

	}

	@Override
	public boolean updatePhotoPanoramioIndex() {
		Query query = getSession().createSQLQuery(
				"CALL updatePhotoPanoramioIndex()");
		query.executeUpdate();
		return true;
	}

	@Override
	public List<Photo> getUserPhotoPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height, Long userId, boolean favorite) {
		
		Double heightRate = height / (neLat - swLat),
				widthRate = width / (neLng - swLng);
			
		Criteria criteria = getSession()
				.createCriteria(Photo.class, "photo")
				.add(Restrictions.ge("gpsPoint.lat", swLat))
				.add(Restrictions.le("gpsPoint.lat", neLat))
				;
		if(favorite) {
			criteria.createAlias("photo.favorites", "favorite")
				.add(Restrictions.eq("favorite.pk.userId", userId));
		}else {
			criteria.add(Restrictions.eq("owner.id", userId));
		}
		if(swLng < neLng) {
			criteria.add(Restrictions.gt("gpsPoint.lng", swLng))
					.add(Restrictions.lt("gpsPoint.lng", neLng));
		}else {
			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.ge("gpsPoint.lng", swLng), Restrictions.le("gpsPoint.lng", 180D)), 
					Restrictions.and(Restrictions.ge("gpsPoint.lng", -180D), Restrictions.le("gpsPoint.lng", neLng))));
		}
		
		List<Photo> photos = criteria.list();
		return filter(photos, widthRate, heightRate);
	}
	
	private List<Photo> filterPanoramioIndex(List<PhotoPanoramioIndex> ppis, Double widthRate, Double heightRate) {
		List<Photo> photos = new ArrayList<Photo>();
		for(PhotoPanoramioIndex ppi : ppis ) {
			photos.add(ppi.getPhoto());
		}
		return filter(photos, widthRate, heightRate) ;
	}
	
	private List<Photo> filterLatestIndex(List<PhotoLatestIndex> plis, Double widthRate, Double heightRate) {
		List<Photo> photos = new ArrayList<Photo>();
		for(PhotoLatestIndex ppl : plis ) {
			photos.add(ppl.getPhoto());
		}
		return filter(photos, widthRate, heightRate) ;
	}
	
	private List<Photo> filter(List<Photo> photos, Double widthRate, Double heightRate) {
		List<Photo> phs = new ArrayList<Photo>();
		boolean allow = true;
		for(Photo photo : photos) {
			allow = true;
			for(Photo p : phs) {
				if(Math.abs((photo.getGpsPoint().getLat() - p.getGpsPoint().getLat()) * heightRate) < minPix
						&& Math.abs((photo.getGpsPoint().getLng() - p.getGpsPoint().getLng()) * widthRate) < minPix) {
					allow = false;
					break;
				}
			}
			if(allow) {
				phs.add(photo);
			}
		} 
		return phs;
	}

//	@Override
//	public List<PhotoPanoramio> getUserFavPanoramio(Double swLat, Double swLng,
//			Double neLat, Double neLng, int level, MapVendor vendor, int width,
//			int height, Long userId) {
//		
//		Double heightRate = height / (neLat - swLat),
//				widthRate = width / (neLng - swLng);
//			
//		Criteria criteria = getSession()
//				.createCriteria(Photo.class)
//				.add(Restrictions.ge("gpsPoint.lat", swLat))
//				.add(Restrictions.le("gpsPoint.lat", neLat))
//				.add(Restrictions.eq("owner.id", userId));
//		if(swLng < neLng) {
//			criteria.add(Restrictions.gt("gpsPoint.lng", swLng))
//					.add(Restrictions.lt("gpsPoint.lng", neLng));
//		}else {
//			criteria.add(Restrictions.or(
//					Restrictions.and(Restrictions.ge("gpsPoint.lng", swLng), Restrictions.le("gpsPoint.lng", 180D)), 
//					Restrictions.and(Restrictions.ge("gpsPoint.lng", -180D), Restrictions.le("gpsPoint.lng", neLng))));
//		}
//	}

	@Override
	public List<Photo> getLatestPanoramio(Double swLat,
			Double swLng, Double neLat, Double neLng, int level,
			MapVendor vendor, int width, int height) {
		
		Double heightRate = height / (neLat - swLat),
				widthRate = width / (neLng - swLng);
		Double lSouth, lWest, lNorth, lEast;
		
		Double lMeasure;
		lMeasure = conMeasure / Math.pow(2D, level);
		lSouth = swLat - (swLat % lMeasure);
		lWest = swLng - (swLng % lMeasure);
		lNorth = neLat - (neLat % lMeasure);
		lEast = neLng - (neLng % lMeasure);

		Criteria criteria = getSession().createCriteria(PhotoLatestIndex.class)
				.add(Restrictions.eq("pk.level", level))
				.add(Restrictions.le("pk.south", lNorth))
				.add(Restrictions.ge("pk.south", lSouth));

		if (lEast > lWest) {
			criteria.add(Restrictions.le("pk.west", lEast)).add(
					Restrictions.ge("pk.west", lWest));
		} else {
			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("pk.west", lWest),
					Restrictions.le("pk.west", 180D)), Restrictions.and(
					Restrictions.ge("pk.west", -180D),
					Restrictions.le("pk.west", lEast))));
		}
		return filterLatestIndex(criteria.list(), widthRate, heightRate);
	}

	@Override
	public boolean updatePhotoLatestIndex() {
		Query query = getSession().createSQLQuery(
				"CALL updatePhotoLatestIndex()");
		query.executeUpdate();
		return true;
	}
}
