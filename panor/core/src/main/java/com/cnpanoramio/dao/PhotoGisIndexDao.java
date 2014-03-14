package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;

@Deprecated
public interface PhotoGisIndexDao extends GenericDao<PhotoGisIndex, Long>{

	public List<PhotoGisIndex> getBoundPhotos(Point sw, Point ne, int zoomLevel, int width, int height);
	
	public boolean updateGisIndex();
	
}
