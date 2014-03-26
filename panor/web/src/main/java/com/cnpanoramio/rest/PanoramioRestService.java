package com.cnpanoramio.rest;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.PhotoPanoramio;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.json.PanoramioResponse;
import com.cnpanoramio.service.PhotoPanoramioIndexService;
import com.cnpanoramio.utils.PhotoUtil;

@Controller
@RequestMapping("/api/rest/panoramio")
public class PanoramioRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PhotoPanoramioIndexService panorIndexService;

	@RequestMapping(value = "/photo", method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse getPanoramio(
			@RequestParam("swlat") String swLat,
			@RequestParam("swlng") String swLng,
			@RequestParam("nelat") String neLat,
			@RequestParam("nelng") String neLng,
			@RequestParam("level") String level,
			@RequestParam("vendor") String vendor,
			@RequestParam("width") String width,
			@RequestParam("height") String height,
			@RequestParam(value="userId", required=false) String userId,
			@RequestParam(value="favorite", required=false) String favorite,
			@RequestParam(value="tag", required=false) String tag,
			@RequestParam(value="latest", required=false) String latest) {
		
		PanoramioResponse response = new PanoramioResponse();
		
		try {
			boolean latestb = Boolean.parseBoolean(latest);
			Double swLatD = Double.parseDouble(swLat);
			Double swLngD = Double.parseDouble(swLng);
			Double neLatD = Double.parseDouble(neLat);
			Double neLngD = Double.parseDouble(neLng);
			int levelI = Integer.parseInt(level);
			int widthI = Integer.parseInt(width);
			int heightI = Integer.parseInt(height);
			MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
			List<PhotoPanoramio> pps = null;
			Long userIdL = null;
			log.debug("getPanoramio [" + swLatD + ", " + swLngD + ", " + neLatD
					+ ", " + neLngD + ", " + levelI + ", " + mVendor + ", "
					+ widthI + ", " + heightI + ", " + userId + ", " + favorite + ", " + tag + ", " + latestb + "]");
			if(latestb) {
				pps = panorIndexService.getLatestPanoramio(swLatD, swLngD, neLatD, neLngD, levelI, mVendor, widthI, heightI);
			}else {
				if(StringUtils.hasText(tag)) {
					
				}else {
					if(StringUtils.hasText(userId)) {
						userIdL = Long.parseLong(userId);
						if(StringUtils.hasText(favorite)) {
							pps = panorIndexService.getUserFavPanoramio(swLatD, swLngD, neLatD, neLngD, levelI, mVendor, widthI, heightI, userIdL);
						}else {
							pps = panorIndexService.getUserPhotoPanoramio(swLatD, swLngD, neLatD, neLngD, levelI, mVendor, widthI, heightI, userIdL);
						}
					}else {
						pps = panorIndexService
								.getPanoramio(new Point(swLatD, swLngD), new Point(neLatD,
										neLngD), levelI, mVendor, widthI, heightI);
					}				
				}
			}			
			
			response.setStatus(PanoramioResponse.Status.OK.name());
			response.setPhotos(pps);
		} catch (NumberFormatException ex) {
			response.setStatus(PanoramioResponse.Status.FORMAT_ERROR.name());
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse updatePanoramio() {
		PanoramioResponse response = new PanoramioResponse();
		response.setStatus(PanoramioResponse.Status.OK.name());
		panorIndexService.updatePanoramioIndex();
		panorIndexService.updatePhotoLatestIndex();
		return response;
	}
	
	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse updateLatestPanoramio() {
		PanoramioResponse response = new PanoramioResponse();
		response.setStatus(PanoramioResponse.Status.OK.name());
	    panorIndexService.updatePhotoLatestIndex();
		return response;
	}
}
