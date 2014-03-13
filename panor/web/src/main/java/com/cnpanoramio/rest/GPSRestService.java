package com.cnpanoramio.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.utils.GpsConverter;
import com.cnpanoramio.utils.GpsConverter.Point;

@Controller
@RequestMapping("/api/rest/gps")
public class GPSRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	private GpsConverter gpsc = new GpsConverter();
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public com.cnpanoramio.domain.Point convert(@RequestParam("lat") String lat,
			@RequestParam("lng") String lng,
			@RequestParam(value="source", required=false, defaultValue="gps") String source,
			@RequestParam(value="dest", required=false, defaultValue="mars") String dest) {
		
		try {
			Double latD = Double.parseDouble(lat);
			Double lngD = Double.parseDouble(lng);
			if(source.equalsIgnoreCase("gps") && dest.equalsIgnoreCase("mars")) {
				Point p = gpsc.getEncryPoint(lngD, latD);
				return new com.cnpanoramio.domain.Point(p.getY(), p.getX());
			}
			return new com.cnpanoramio.domain.Point();
		} catch (NumberFormatException ex) {
			throw ex;
		}
		
	}
}
