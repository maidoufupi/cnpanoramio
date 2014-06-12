package com.cnpanoramio.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.service.lbs.GpsConverter;
import com.cnpanoramio.service.lbs.GpsConverter.Point;

@Controller
@RequestMapping("/api/rest/gps")
public class GPSRestService extends AbstractRestService {

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
	
	@RequestMapping(value="/geo", method = RequestMethod.GET)
	@ResponseBody
	public String geocode(@RequestParam("address") String address, HttpServletResponse hsResponse) throws ClientProtocolException, IOException {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(
			"http://restapi.amap.com/v3/geocode/geo?key=feb4691f59f30a28499c2a6197ba2773&address=" + address);
		getRequest.addHeader("accept", "application/json");
		
		HttpResponse response = httpClient.execute(getRequest);
 
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			   + response.getStatusLine().getStatusCode());
		}
 
		BufferedReader br = new BufferedReader(
                         new InputStreamReader((response.getEntity().getContent())));
		StringBuffer strBuffer = new StringBuffer();
		String output;
		while ((output = br.readLine()) != null) {
			output = new String(output.getBytes("GBK"), "UTF-8");
			strBuffer.append(output);
		}
		log.debug(strBuffer.toString());
		httpClient.getConnectionManager().shutdown();
		return strBuffer.toString();
	}
}
