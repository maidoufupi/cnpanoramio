package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.PhotoPanoramioIndex;
import com.cnpanoramio.domain.Point;

public interface PhotoPanoramioIndexDao extends GenericDao<PhotoPanoramioIndex, PhotoPanoramioIndex.PhotoPanoramioIndexPK> {
	public List<PhotoPanoramio> getPhotoPanoramio(Point sw, Point ne, int level, MapVendor vendor, int width, int height);
	
	public boolean updatePhotoPanoramioIndex();
}
