package com.cnpanoramio.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoLatestIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;

@Repository("photoPanoramioIndexDao")
public class PhotoPanoramioIndexDaoImpl extends
		GenericDaoHibernate<PhotoPanoramioIndex, PhotoPanoramioIndexPK>
		implements PhotoPanoramioIndexDao {

	private Double conMeasure = 40D;
	private Integer bigPix = 34;
//	private Integer miniPix = 10;

	public PhotoPanoramioIndexDaoImpl() {
		super(PhotoPanoramioIndex.class);
		// TODO Auto-generated constructor stub
	}

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
	
	public List<PhotoPanoramioIndex> getPhotoPanoramioIndexList(int level, Double west, Double south, Double measure) {
		Criteria criteria = getSession().createCriteria(PhotoPanoramioIndex.class)
				.add(Restrictions.eq("pk.level", level+1))
				.add(Restrictions.ge("pk.south", south))
				.add(Restrictions.lt("pk.south", south+measure))
				.add(Restrictions.ge("pk.west", west))
				.add(Restrictions.lt("pk.west", west+measure))
				.addOrder(Order.desc("photoRating"));
				;
		List<PhotoPanoramioIndex> ppiList = criteria.list();
		return ppiList;
	}
	
	public List<Photo> getPhotoList(int level, Double west, Double south, Double measure) {
		Criteria criteria = getSession()
				.createCriteria(Photo.class, "photo")
				.add(Restrictions.ge("gpsPoint.lat", south))
				.add(Restrictions.lt("gpsPoint.lat", south+measure))
				.add(Restrictions.ge("gpsPoint.lng", west))
				.add(Restrictions.lt("gpsPoint.lng", west+measure))
				;
		List<Photo> photos = criteria.list();
		return photos;
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
				if(Math.abs((photo.getGpsPoint().getLat() - p.getGpsPoint().getLat()) * heightRate) < bigPix
						&& Math.abs((photo.getGpsPoint().getLng() - p.getGpsPoint().getLng()) * widthRate) < bigPix) {
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

		log.info("getLatestPanoramio level = " + level);
		log.info("getLatestPanoramio lNorth = " + lNorth);
		log.info("getLatestPanoramio lSouth = " + lSouth);
		log.info("getLatestPanoramio lEast = " + lEast);
		log.info("getLatestPanoramio lWest = " + lWest);
		
//		Criteria criteria1 = getSession().createCriteria(PhotoPanoramioIndex.class)
//				.add(Restrictions.eq("pk.level", level))
//				.add(Restrictions.le("pk.south", lNorth))
//				.add(Restrictions.ge("pk.south", lSouth));
//
//		if (lEast > lWest) {
//			criteria1.add(Restrictions.le("pk.west", lEast))
//					.add(Restrictions.ge("pk.west", lWest));
//		} else {
//			criteria1.add(Restrictions.or(Restrictions.and(
//					Restrictions.ge("pk.west", lWest),
//					Restrictions.le("pk.west", 180D)), Restrictions.and(
//					Restrictions.ge("pk.west", -180D),
//					Restrictions.le("pk.west", lEast))));
//		}
//		List list1 = criteria1.list();
//		log.info("getIndexPanoramio size = " + list1.size());
		
		Criteria criteria = getSession().createCriteria(PhotoLatestIndex.class)
				.add(Restrictions.eq("pk.level", level))
				.add(Restrictions.le("pk.south", lNorth))
				.add(Restrictions.ge("pk.south", lSouth));

		if (lEast > lWest) {
			criteria.add(Restrictions.le("pk.west", lEast))
					.add(Restrictions.ge("pk.west", lWest));
		} else {
			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("pk.west", lWest),
					Restrictions.le("pk.west", 180D)), Restrictions.and(
					Restrictions.ge("pk.west", -180D),
					Restrictions.le("pk.west", lEast))));
		}
		List list = criteria.list();
		log.info("getLatestPanoramio size = " + list.size());
		return filterLatestIndex(list, widthRate, heightRate);
	}

	@Override
	public boolean updatePhotoLatestIndex() {
		Query query = getSession().createSQLQuery(
				"CALL updatePhotoLatestIndex()");
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean clearPhotoIndex() {
		Query deleteQuery = getSession().createQuery("delete PhotoPanoramioIndex");
		deleteQuery.executeUpdate();
		return true;
	}
}
