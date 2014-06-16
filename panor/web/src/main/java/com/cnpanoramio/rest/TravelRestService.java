package com.cnpanoramio.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.json.TravelResponse;
import com.cnpanoramio.json.TravelResponse.TravelSpot;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/travel")
public class TravelRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager = null;
	
	@Autowired
	private TravelManager travelManager;
	@Autowired
	private TravelService travelService;
	
	@Autowired
	private PhotoManager photoManager;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public TravelResponse create(@RequestParam("travel") String travel) {
		TravelResponse response = new TravelResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		response.setTravels(travelService.createMyTravel(me, travel));
		response.setStatus(TravelResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{travelId}", method = RequestMethod.GET)
	@ResponseBody
	public TravelResponse getTravel(@PathVariable String travelId) {
		TravelResponse response = responseFactory();
		response.setTravel(travelService.getTravel(Long.parseLong(travelId)));
		return response;
	}
	
	@RequestMapping(value = "/{travelId}", method = RequestMethod.POST)
	@ResponseBody
	public TravelResponse changeTravel(@PathVariable String travelId, @RequestParam("description") String description) {
		TravelResponse response = responseFactory();
		response.setTravel(travelService.changeTravelDesc(Long.parseLong(travelId), description));
		return response;
	}
	
	@RequestMapping(value = "/{travelId}/spot/{spotId}", method = RequestMethod.GET)
	@ResponseBody
	public TravelResponse getTravelSpot(@PathVariable String travelId, @PathVariable String spotId) {
		TravelResponse response = responseFactory();
		response.setSpot(travelService.getSpot(Long.parseLong(spotId)));
		return response;
	}
	
	@RequestMapping(value = "/{travelId}/spot/{spotId}", method = RequestMethod.POST)
	@ResponseBody
	public TravelResponse changeTravelSpot(@PathVariable String travelId, 
			@PathVariable String spotId, 
			@RequestParam(value="address", required=false) String address,
			@RequestParam(value="title", required=false) String title,
			@RequestParam(value="description", required=false) String description) {
		TravelResponse response = responseFactory();
		TravelSpot travelSpot = new TravelSpot();
		travelSpot.setAddress(address);
		travelSpot.setTitle(title);
		travelSpot.setDescription(description);
		response.setSpot(travelService.changeSpot(Long.parseLong(spotId), travelSpot));;
		return response;
	}
	
	@RequestMapping(value = "/{travelId}/photo", method = RequestMethod.POST)
	@ResponseBody
	public TravelResponse addPhotos(@PathVariable String travelId, @RequestParam("photos") String photos) {
		TravelResponse response = responseFactory();
		String[] ps = photos.split(",");
		List<Long> photoIds = new ArrayList<Long>();
		for(String id : ps) {
			photoIds.add(Long.parseLong(id));
		}
		
		response.setTravel(travelService.addTravelPhotos(Long.parseLong(travelId), photoIds));
			
		return response;
	}
	
	@RequestMapping(value = "/{travelId}/photo/{photoId}", method = RequestMethod.DELETE)
	@ResponseBody
	public TravelResponse removePhotos(@PathVariable String travelId, @PathVariable String photoId) {
		TravelResponse response = responseFactory();
		
		List<Long> photoIds = new ArrayList<Long>();
		photoIds.add(Long.parseLong(photoId));
		
		response.setTravel(travelService.removeTravelPhotos(Long.parseLong(travelId), photoIds));
		
		return response;
	}
	
	
	/**
	 * 创建范围对象的工厂方法，默认为OK状态
	 * 
	 * @return
	 */
	protected TravelResponse responseFactory() {
		TravelResponse response = new TravelResponse();
		response.setStatus(TravelResponse.Status.OK.name());
		return response;
	}
}
