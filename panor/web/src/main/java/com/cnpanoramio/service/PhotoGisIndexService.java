package com.cnpanoramio.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.cnpanoramio.domain.PhotoGisIndex;
import com.cnpanoramio.domain.Point;

@Path("/gisindex")
public interface PhotoGisIndexService {

	@GET
	public boolean updateGisIndex();
	
	public List<PhotoGisIndex> getBoundPhotos(Point sw, Point ne, int zoomLevel, int width, int height);
}
