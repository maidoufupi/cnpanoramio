package com.cnpanoramio.dao.impl.hibernate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.PhotoPanoramioIndexDao;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;

@Repository("photoPanoramioIndexDao")
public class PhotoPanoramioIndexDaoImpl extends GenericDaoHibernate<PhotoPanoramioIndex,  PhotoPanoramioIndex.PhotoPanoramioIndexPK> implements PhotoPanoramioIndexDao {

	public PhotoPanoramioIndexDaoImpl() {
		super(PhotoPanoramioIndex.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<PhotoPanoramio> getPhotoPanoramio(Point sw, Point ne,
			int level, MapVendor mVendor, int width, int height) {
		String vendor = "gps";
		if(mVendor.equals(MapVendor.gaode)) {
			vendor = "gaode";
		}else if(mVendor.equals(MapVendor.baidu)) {
			vendor = "baidu";
		}else if(mVendor.equals(MapVendor.qq)) {
			vendor = "qq";
		}else if(mVendor.equals(MapVendor.ali)) {
			vendor = "ali";
		}else if(mVendor.equals(MapVendor.sogou)) {
			vendor = "sogou";
		}else if(mVendor.equals(MapVendor.mapbar)) {
			vendor = "mapbar";
		}
		Query query = getSession().createSQLQuery(
				"CALL getPhotoPanoramioIndex(:swLat, :swLng, :neLat, :neLng, :level, :vendor, :width, :height)")
				.addEntity(PhotoPanoramio.class)
				.setParameter("swLat", sw.getLat())
				.setParameter("swLng", sw.getLng())
				.setParameter("neLat", ne.getLat())
				.setParameter("neLng", ne.getLng())
				.setParameter("level", level)
				.setParameter("vendor", vendor)
				.setParameter("width", width)
				.setParameter("height", height);
			 
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

}
