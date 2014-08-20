package com.cnpanoramio.rest;

import java.io.UnsupportedEncodingException;
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
import com.cnpanoramio.json.PanoramioResponse;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.service.PhotoPanoramioIndexService;
import com.cnpanoramio.service.lbs.LatLng;
import com.cnpanoramio.utils.PhotoUtil;

@Controller
@RequestMapping("/api/rest/panoramio")
public class PanoramioRestService extends AbstractRestService {

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
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "favorite", required = false) String favorite,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "latest", required = false) String latest) {

		PanoramioResponse response = new PanoramioResponse();

		boolean latestb = Boolean.parseBoolean(latest);
		Double swLatD = Double.parseDouble(swLat);
		Double swLngD = Double.parseDouble(swLng);
		Double neLatD = Double.parseDouble(neLat);
		Double neLngD = Double.parseDouble(neLng);
		int levelI = Integer.parseInt(level);
		int widthI = Integer.parseInt(width);
		int heightI = Integer.parseInt(height);
		MapVendor mVendor = PhotoUtil.getMapVendor(vendor);

		List<PhotoProperties> pps = null;
		Long userIdL = null;
		log.debug("getPanoramio [" + swLatD + ", " + swLngD + ", " + neLatD
				+ ", " + neLngD + ", " + levelI + ", " + mVendor + ", "
				+ widthI + ", " + heightI + ", " + userId + ", " + favorite
				+ ", " + tag + ", " + latestb + "]");
		if (latestb) {
			pps = panorIndexService.getLatestPanoramio(new LatLng(swLatD, swLngD), new LatLng(neLatD, neLngD), 
					levelI, mVendor, widthI, heightI);
		} else {
			if (StringUtils.hasText(tag)) {

			} else {
				if (StringUtils.hasText(userId)) {
					userIdL = Long.parseLong(userId);
					if (StringUtils.hasText(favorite)) {
						pps = panorIndexService.getUserFavPanoramio(new LatLng(swLatD, swLngD), new LatLng(neLatD, neLngD), 
								levelI, mVendor, widthI, heightI, userIdL);
					} else {
						pps = panorIndexService.getUserPhotoPanoramio(new LatLng(swLatD, swLngD), new LatLng(neLatD, neLngD), 
								levelI, mVendor, widthI, heightI, userIdL);
					}
				} else {
					pps = panorIndexService.getPanoramio(new LatLng(swLatD, swLngD), new LatLng(neLatD, neLngD), 
							levelI, mVendor, widthI, heightI);
				}
			}
		}

		response.setStatus(PanoramioResponse.Status.OK.name());
		response.setPhotos(pps);

		return response;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse updatePanoramio() {
		log.debug("update panoramio index");
		PanoramioResponse response = new PanoramioResponse();
		response.setStatus(PanoramioResponse.Status.OK.name());
		panorIndexService.updatePanoramioIndex();
		return response;
	}

	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse updateLatestPanoramio() {
		log.debug("update panoramio latest index");
		PanoramioResponse response = new PanoramioResponse();
		response.setStatus(PanoramioResponse.Status.OK.name());
		panorIndexService.updatePhotoLatestIndex();
		return response;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public PanoramioResponse search(
			@RequestParam("swlat") String swLat,
			@RequestParam("swlng") String swLng,
			@RequestParam("nelat") String neLat,
			@RequestParam("nelng") String neLng,
			@RequestParam("level") String level,
			@RequestParam("width") String width,
			@RequestParam("height") String height,
			@RequestParam("term") String term,
			@RequestParam(value="type", required = false) String type,
			@RequestParam(value="vendor", required = false) String vendor) {
		
		PanoramioResponse response = new PanoramioResponse();
		
		Double swLatD = Double.parseDouble(swLat);
		Double swLngD = Double.parseDouble(swLng);
		Double neLatD = Double.parseDouble(neLat);
		Double neLngD = Double.parseDouble(neLng);
		int levelI = Integer.parseInt(level);
		int widthI = Integer.parseInt(width);
		int heightI = Integer.parseInt(height);
		MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
		
		try {
			term = new String(term.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("search [" + swLatD + ", " + swLngD + ", " + neLatD
				+ ", " + neLngD + ", " + levelI + ", " + mVendor + ", "
				+ widthI + ", " + heightI + ", " + term + ", " + type + "]");
		
		List<PhotoProperties> pps = panorIndexService.search(new LatLng(swLatD, swLngD), new LatLng(neLatD, neLngD), 
				levelI, mVendor, widthI, heightI, term, type);
		response.setStatus(PanoramioResponse.Status.OK.name());
		response.setPhotos(pps);
		return response;
	}
}
