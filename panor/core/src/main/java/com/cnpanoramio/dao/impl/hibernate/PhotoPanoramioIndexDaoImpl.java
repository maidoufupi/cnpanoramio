package com.cnpanoramio.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoLatestIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
import com.cnpanoramio.domain.Point;

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
	public List<Photo> getPhotoPanoramio(Point sw, Point ne,
			int level, MapVendor mVendor, int width, int height) {

		Double heightRate = height / (ne.lat - sw.lat),
				widthRate = width / (ne.lng - sw.lng);
		Double lSouth, lWest, lNorth, lEast;

		Double lMeasure;
		lMeasure = conMeasure / Math.pow(2D, level);
		lSouth = sw.lat - (sw.lat % lMeasure);
		lWest = sw.lng - (sw.lng % lMeasure);
		lNorth = ne.lat - (ne.lat % lMeasure);
		lEast = ne.lng - (ne.lng % lMeasure);

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
	public List<Photo> getUserPhotoPanoramio(Point sw, Point ne, int level,
			MapVendor vendor, int width, int height, Long userId, boolean favorite) {
		
		Double heightRate = height / (ne.lat - sw.lat),
				widthRate = width / (ne.lng - sw.lng);
			
		Criteria criteria = getSession()
				.createCriteria(Photo.class, "photo")
				.add(Restrictions.ge("gpsPoint.lat", sw.lat))
				.add(Restrictions.le("gpsPoint.lat", ne.lat))
				;
		if(favorite) {
			criteria.createAlias("photo.favorites", "favorite")
				.add(Restrictions.eq("favorite.pk.userId", userId));
		}else {
			criteria.add(Restrictions.eq("owner.id", userId));
		}
		if(sw.lng < ne.lng) {
			criteria.add(Restrictions.gt("gpsPoint.lng", sw.lng))
					.add(Restrictions.lt("gpsPoint.lng", ne.lng));
		}else {
			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.ge("gpsPoint.lng", sw.lng), Restrictions.le("gpsPoint.lng", 180D)), 
					Restrictions.and(Restrictions.ge("gpsPoint.lng", -180D), Restrictions.le("gpsPoint.lng", ne.lng))));
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

	@Override
	public List<Photo> getLatestPanoramio(Point sw, Point ne, int level,
			MapVendor vendor, int width, int height) {
		
		Double heightRate = height / (ne.lat - sw.lat),
				widthRate = width / (ne.lng - sw.lng);
		Double lSouth, lWest, lNorth, lEast;
		
		Double lMeasure;
		lMeasure = conMeasure / Math.pow(2D, level);
		lSouth = sw.lat - (sw.lat % lMeasure);
		lWest = sw.lng - (sw.lng % lMeasure);
		lNorth = ne.lat - (ne.lat % lMeasure);
		lEast = ne.lng - (ne.lng % lMeasure);
			
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
		
//		createIndexer();
		
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
	
	private void createIndexer() {
		FullTextSession fullTextSession = Search.getFullTextSession(getSession());
		try {
			fullTextSession.createIndexer(PhotoPanoramioIndex.class)
			.batchSizeToLoadObjects( 25 )
			 .cacheMode( CacheMode.NORMAL )
			 .threadsToLoadObjects( 5 )
			 .idFetchSize( 150 )
			 .threadsForSubsequentFetching( 20 ).startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Photo> search(Point sw, Point ne, int level, int width, int height, String term, String type) {
		
		String termStr = term.trim();
		termStr = termStr.replace(" ", "%");
		termStr = "%" + termStr + "%";
		
//		log.info("search dao [" + swLat + ", " + swLng + ", " + neLat
//				+ ", " + neLng + ", " + level  + ", "
//				+ width + ", " + height + ", " + term + ", " + type + "]");
				
		Double lSouth, lWest, lNorth, lEast;
		
		Double lMeasure;
		lMeasure = conMeasure / Math.pow(2D, level);
		lSouth = sw.lat - (sw.lat % lMeasure);
		lWest = sw.lng - (sw.lng % lMeasure);
		lNorth = ne.lat - (ne.lat % lMeasure);
		lEast = ne.lng - (ne.lng % lMeasure);
		
		Criteria criteria = getSession().createCriteria(PhotoPanoramioIndex.class, "photoIndex")
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

		Criterion photoName = null;
		Criterion photoTitle = null;
		Criterion photoDesc = null;
		Criterion travelSpotTitle = null;
		Criterion travelSpotDesc = null;
		Criterion travelSpotAddress = null;
		Criterion travelTitle = null;
		Criterion travelDesc = null;
		Criterion travelAddress = null;
		
		if(null == type || type.equalsIgnoreCase("") || type.equalsIgnoreCase("all") || type.equalsIgnoreCase("photo")) {
			criteria.createAlias("photoIndex.photo", "photo");
			photoName = Restrictions.ilike("photo.name", termStr);
			photoTitle = Restrictions.ilike("photo.title", termStr);
			photoDesc = Restrictions.ilike("photo.description", termStr);
		}
		if(null == type || type.equalsIgnoreCase("") || type.equalsIgnoreCase("all") || type.equalsIgnoreCase("travel")){
			criteria.createAlias("photoIndex.photo.travelSpot", "travelSpot")
				.createAlias("travelSpot.travel", "travel");
			
			travelSpotTitle = Restrictions.ilike("travelSpot.title", termStr);
			travelSpotDesc = Restrictions.ilike("travelSpot.description", termStr);
			travelSpotAddress = Restrictions.ilike("travelSpot.address", termStr);
			travelTitle = Restrictions.ilike("travel.title", termStr);
			travelDesc = Restrictions.ilike("travel.description", termStr);
			travelAddress = Restrictions.ilike("travel.address", termStr);
		}
		if(null == type || type.equalsIgnoreCase("") || type.equalsIgnoreCase("all")) {
			criteria.add(Restrictions.or(photoName, photoTitle, photoDesc, travelSpotTitle, travelSpotDesc, travelSpotAddress,
					travelTitle, travelDesc, travelAddress));
		}else if(type.equalsIgnoreCase("photo")) {
			criteria.add(Restrictions.or(photoName, photoTitle, photoDesc));
		}else if(type.equalsIgnoreCase("travel")) {
			criteria.add(Restrictions.or(travelSpotTitle, travelSpotDesc, travelSpotAddress,
					travelTitle, travelDesc, travelAddress));
		}		
		
		List<PhotoPanoramioIndex> photoIndexs = criteria.list();
        
		log.info("search res size: " + photoIndexs.size() );
	    Double heightRate = height / (ne.lat - sw.lat),
					widthRate = width / (ne.lng - sw.lng);
	    return filterPanoramioIndex(photoIndexs, widthRate, heightRate);
	}
	
//	public List<Photo> search(Double swLat,
//			Double swLng, Double neLat, Double neLng, int level, int width, int height, String term) {
//		FullTextSession fullTextSession = Search.getFullTextSession(getSession());
//        final QueryBuilder mythQB = fullTextSession.getSearchFactory()
//        	    .buildQueryBuilder().forEntity( PhotoPanoramioIndex.class ).get();
//        
//        org.apache.lucene.search.Query luceneQuery = mythQB
//        		.bool()
//        			.must(mythQB.keyword().onField("pk.level").matching(String.valueOf(level)).createQuery())
//        			.must(mythQB.range().onField("pk.south").from(swLat).to(neLat).createQuery())
//        			.must(mythQB.range().onField("pk.west").from(swLng).to(neLng).createQuery())
//        			.should(mythQB.keyword()
//        					.onFields("photo.name",
//        							  "photo.title",
//        							  "photo.description",
//        							  "photo.travelSpot.title",
//        							  "photo.travelSpot.description",
//        							  "photo.travelSpot.address",
//        							  "photo.travelSpot.travel.title",
//        							  "photo.travelSpot.travel.description",
//        							  "photo.travelSpot.travel.address"
//        							  ).matching(term).createQuery())
//        			.createQuery();
//        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery, PhotoPanoramioIndex.class );
//        List<PhotoPanoramioIndex> photoIndexs = fullTextQuery.list();
//        
//        Double heightRate = height / (neLat - swLat),
//				widthRate = width / (neLng - swLng);
//        return filterPanoramioIndex(photoIndexs, widthRate, heightRate);
//	}
}
