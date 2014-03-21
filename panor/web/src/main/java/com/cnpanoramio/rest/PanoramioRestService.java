package com.cnpanoramio.rest;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
			@RequestParam(value="tag", required=false) String tag) {
		
		PanoramioResponse response = new PanoramioResponse();
		
		try {
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
					+ widthI + ", " + heightI + ", " + userId + ", " + favorite + ", " + tag + "]");
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
			
			response.setStatus(PanoramioResponse.Status.OK.name());
			response.setPhotos(pps);
		} catch (NumberFormatException ex) {
			response.setStatus(PanoramioResponse.Status.FORMAT_ERROR.name());
		}
		return response;
	}
	
//	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
//	@ResponseBody
//	public PanoramioResponse getUserPhotoPanoramio(
//			@RequestParam("swlat") String swLat,
//			@RequestParam("swlng") String swLng,
//			@RequestParam("nelat") String neLat,
//			@RequestParam("nelng") String neLng,
//			@RequestParam("level") String level,
//			@RequestParam("vendor") String vendor,
//			@RequestParam("width") String width,
//			@RequestParam("height") String height,
//			@PathVariable("userId") String userId) {
//		
//		PanoramioResponse response = new PanoramioResponse();
//		 
//		try {
//			Double swLatD = Double.parseDouble(swLat);
//			Double swLngD = Double.parseDouble(swLng);
//			Double neLatD = Double.parseDouble(neLat);
//			Double neLngD = Double.parseDouble(neLng);
//			int levelI = Integer.parseInt(level);
//			int widthI = Integer.parseInt(width);
//			int heightI = Integer.parseInt(height);
//			Long userIdL = Long.parseLong(userId);
//			MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
//			
//			log.debug("getUserPhotoPanoramio [" + swLatD + ", " + swLngD + ", " + neLatD
//					+ ", " + neLngD + ", " + levelI + ", " + mVendor + ", "
//					+ widthI + ", " + heightI + "]");
//			List<PhotoPanoramio> 
//			response.setStatus(PanoramioResponse.Status.OK.name());
//			response.setPhotos(pps);
//		} catch (NumberFormatException ex) {
//			response.setStatus(PanoramioResponse.Status.FORMAT_ERROR.name());
//		}
//		return response;
//	}
//	
//	@RequestMapping(value = "/user/{userId}/favorite", method = RequestMethod.GET)
//	@ResponseBody
//	public PanoramioResponse getUserFavPanoramio(
//			@RequestParam("swlat") String swLat,
//			@RequestParam("swlng") String swLng,
//			@RequestParam("nelat") String neLat,
//			@RequestParam("nelng") String neLng,
//			@RequestParam("level") String level,
//			@RequestParam("vendor") String vendor,
//			@RequestParam("width") String width,
//			@RequestParam("height") String height,
//			@PathVariable("userId") String userId) {
//		
//		PanoramioResponse response = new PanoramioResponse();
//		
//		try {
//			Double swLatD = Double.parseDouble(swLat);
//			Double swLngD = Double.parseDouble(swLng);
//			Double neLatD = Double.parseDouble(neLat);
//			Double neLngD = Double.parseDouble(neLng);
//			int levelI = Integer.parseInt(level);
//			int widthI = Integer.parseInt(width);
//			int heightI = Integer.parseInt(height);
//			Long userIdL = Long.parseLong(userId);
//			MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
//			
//			log.debug("getUserFavPanoramio [" + swLatD + ", " + swLngD + ", " + neLatD
//					+ ", " + neLngD + ", " + levelI + ", " + mVendor + ", "
//					+ widthI + ", " + heightI + "]");
//			
//			response.setStatus(PanoramioResponse.Status.OK.name());
//			response.setPhotos(pps);
//		} catch (NumberFormatException ex) {
//			response.setStatus(PanoramioResponse.Status.FORMAT_ERROR.name());
//		}
//		return response;
//	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public boolean updatePanoramio() {
		return panorIndexService.updatePanoramioIndex();
	}
}
