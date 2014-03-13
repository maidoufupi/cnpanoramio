package com.cnpanoramio.service;

import java.util.List;

import javax.ws.rs.Path;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.Point;

public interface PhotoPanoramioIndexService {

	public boolean updatePanoramioIndex();
	
	public List<PhotoPanoramio> getPanoramio(Point sw, Point ne, int level, MapVendor vendor, int width, int height);
}
