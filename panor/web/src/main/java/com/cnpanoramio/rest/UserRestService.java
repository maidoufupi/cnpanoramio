package com.cnpanoramio.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.domain.Avatar;
import com.cnpanoramio.json.MessageResponse;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserResponse;
import com.cnpanoramio.json.UserSettings;
import com.cnpanoramio.service.CircleManager;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.MessageManager;
import com.cnpanoramio.service.MessageQueueManager;
import com.cnpanoramio.service.MessageService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.RecycleService;
import com.cnpanoramio.service.TagManager;
import com.cnpanoramio.service.TravelManager;
import com.cnpanoramio.service.TravelService;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/user")
public class UserRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserManager userManager = null;

	@Autowired
	private UserSettingsService userSettingsService;
	
	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private PhotoManager photoService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private TravelManager travelManager;
	
	@Autowired
	private TravelService travelService;
	
	@Autowired
	private CircleManager circleManager;
	
	@Autowired
	private MessageQueueManager messageQueueManager;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private TagManager tagManager;
	
	@Autowired
	private RecycleManager recycleManager;
	
	@Autowired
	private RecycleService recycleService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getMe() {
		UserResponse response = new UserResponse();
		
		User me = UserUtil.getCurrentUser(userManager);
		UserOpenInfo openInfo = userSettingsService.getOpenInfo(me, null);
		
		response.setStatus(UserResponse.Status.OK.name());
		response.setOpenInfo(openInfo);

		return response;
	}
	
	@RequestMapping(value = "/{userId}/settings", method = RequestMethod.POST)
	@ResponseBody
	public UserResponse save(@RequestBody final UserSettings settings) {
		UserResponse response = new UserResponse();
		userSettingsService.save(settings);
//		UserSettings userSettings = UserUtil.transformSettings(settings);
//		userSettingsManager.save(userSettings);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/settings", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getSettings() {
		UserResponse response = new UserResponse();
		User me = UserUtil.getCurrentUser(userManager);
//		UserSettings settings = userSettingsManager.getCurrentUserSettings();
		UserSettings settings = userSettingsService.getUserSettings(me);
		response.setSettings(settings);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}

	@RequestMapping(value = "/{userId}/photos/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotos(@PathVariable String userId,
								  @PathVariable String pageSize, 
								  @PathVariable String pageNo,
								  @RequestParam(value = "swlat", required = false) String swLat,
								  @RequestParam(value = "swlng", required = false) String swLng,
								  @RequestParam(value = "nelat", required = false) String neLat,
								  @RequestParam(value = "nelng", required = false) String neLng) {

		UserResponse response = new UserResponse();
		
		int pageSizeI = Integer.valueOf(pageSize).intValue();
		int pageNoI = Integer.valueOf(pageNo).intValue();

		Collection<PhotoProperties> photos = null;
		
		if(null != swLat &&
		   null != swLng &&
		   null != neLat &&
		   null != neLng) {
			
			Double swLatD = Double.parseDouble(swLat);
			Double swLngD = Double.parseDouble(swLng);
			Double neLatD = Double.parseDouble(neLat);
			Double neLngD = Double.parseDouble(neLng);
			
			photos = userSettingsManager.getPhotosForUserBounds(userId, pageSizeI, pageNoI, swLatD, swLngD, neLatD, neLngD);
		}else {
			photos = photoService.getPhotosForUser(
					userId, pageSizeI, pageNoI);
		}
		
		response.setStatus(UserResponse.Status.OK.name());
		response.setPhotos(photos);

		return response;
	}

	@RequestMapping(value = "/{userId}/photos/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotosWithPhoto(@PathVariable String userId,
			@PathVariable String photoId) {
		UserResponse response = new UserResponse();

		try {
			Long photoIdL = Long.parseLong(photoId);
			User user = userManager.getUser(userId);
			int num = photoService.getUserPhotoNum(user, photoIdL);
			Collection<PhotoProperties> pps = photoService
					.getUserPhotosWithPhoto(user, photoIdL);
			response.setStatus(UserResponse.Status.OK.name());
			response.setPhotoInfo(new UserResponse.PhotoInfo(photoService.getPhotoCount(user), num));
			response.setPhotos(pps);
		} catch (NumberFormatException ex) {
			response.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
			return response;
		}

		return response;
	}

	@RequestMapping(value = "/{userId}/openinfo", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getOpenInfo(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
		}catch(Exception ex) {
		}
		 
		User user = userManager.getUser(userId);

		UserOpenInfo openInfo = userSettingsService.getOpenInfo(user, me);
		
		response.setStatus(UserResponse.Status.OK.name());
		response.setOpenInfo(openInfo);

		return response;
	}

	@RequestMapping(value = "/{userId}/photos/tag/{tag}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotoCountByTag(@PathVariable String userId,
			@PathVariable String tag) {
		UserResponse response = new UserResponse();

		try {
			tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");

			Long userIdL = Long.parseLong(userId);
			Long count = photoService.getUserPhotoCountByTag(userIdL, tag);
			// Collection<PhotoProperties> pps =
			// photoService.getUserPhotosByTag(userIdL, tag);
			response.setStatus(UserResponse.Status.OK.name());
			response.setPhotoInfo(new UserResponse.PhotoInfo(count));
			// response.setPhotos(pps);
		} catch (NumberFormatException ex) {
			response.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		} catch (UnsupportedEncodingException e) {
			response.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		}

		return response;
	}

	@RequestMapping(value = "/{userId}/photos/tag/{tag}/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotoPageByTag(@PathVariable String userId,
			@PathVariable String tag, @PathVariable String pageSize,
			@PathVariable String pageNo) {
		UserResponse response = new UserResponse();

		try {
			tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
			Long userIdL = Long.parseLong(userId);
			int pageSizeI, pageNoI;
			pageSizeI = Integer.valueOf(pageSize).intValue();
			pageNoI = Integer.valueOf(pageNo).intValue();

			Collection<PhotoProperties> pps = photoService
					.getUserPhotoPageByTag(userIdL, tag, pageSizeI, pageNoI);
			response.setStatus(UserResponse.Status.OK.name());
			response.setPhotos(pps);
		} catch (NumberFormatException ex) {
			response.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		} catch (UnsupportedEncodingException e) {
			response.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		}

		return response;
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	UserResponse avatarUpload(@RequestParam("file") MultipartFile file) {

		UserResponse response = new UserResponse();
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
			// me = userManager.get(1L);
		} catch (UsernameNotFoundException ex) {
			response.setStatus(UserResponse.Status.NO_AUTHORIZE.name());
			return response;
		}
		if (!file.isEmpty()) {
			try {
				String completeImageData = new String(file.getBytes());
				String imageDataBytes = completeImageData
						.substring(completeImageData.indexOf(",") + 1);

				ByteArrayInputStream ins = new ByteArrayInputStream(
						Base64.decode(imageDataBytes.getBytes()));

				Avatar avatar = userSettingsManager.saveAvatar(me, ins);
				UserOpenInfo openInfo = new UserOpenInfo();
				openInfo.setId(me.getId());
				openInfo.setAvatar(avatar.getId());
				response.setOpenInfo(openInfo);
				response.setStatus(UserResponse.Status.OK.name());
				return response;
			} catch (Exception e) {
				response.setStatus(UserResponse.Status.EXCEPTION.name());
				response.setInfo(e.getMessage());
				return response;
			}
		} else {
			log.debug("file is empty");

			response.setStatus(UserResponse.Status.EXCEPTION.name());
			response.setInfo("file is empty");
			return response;
		}
	}

	@RequestMapping(value = "/{userId}/avatar", method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource avatar(@PathVariable String userId, HttpServletResponse response) {

		Long id = Long.parseLong(userId);
		Long avatarId = 1L;
		Avatar avatar = userSettingsManager.getUserAvatar(id);

		if(null != avatar) {
			avatarId = avatar.getId();
		}
		File file = fileService.readFile(FileService.TYPE_AVATAR,
				avatarId, UserSettingsManager.AVATAR_FILE_TYPE,
				String.valueOf(FileService.THUMBNAIL_LEVEL_0));
		
		response.setHeader("Cache-Control","public, max-age=2629000");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.MONTH, 1);
	    response.setDateHeader("Expires", c.getTimeInMillis());
	    
		return new FileSystemResource(file);
	}
	
	@RequestMapping(value = "/{userId}/tag", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getUserTags(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(user.getId());
		openInfo.setTags(userSettingsManager.getUserTags(user));
		response.setOpenInfo(openInfo);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/tag/{tag}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse createUserTag(@PathVariable String userId, @PathVariable String tag) throws UnsupportedEncodingException {
		UserResponse response = new UserResponse();
		User me = UserUtil.getCurrentUser(userManager);
		if(!me.getId().toString().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(me.getId());
		// 创建tag
		userSettingsManager.createTag(me, tag);
		// 重新获取tags
		openInfo.setTags(userSettingsManager.getUserTags(me));		
		response.setOpenInfo(openInfo);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/tag/{tag}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse deleteUserTag(@PathVariable String userId, @PathVariable String tag) throws UnsupportedEncodingException {
		UserResponse response = new UserResponse();
		User me = UserUtil.getCurrentUser(userManager);
		if(!me.getId().toString().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
		// 删除用户tag
		tagManager.deleteUserTag(me, tag);
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(me.getId());
		// 重新获取tags
		openInfo.setTags(userSettingsManager.getUserTags(me));		
		response.setOpenInfo(openInfo);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/travel", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getUserTravels(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(user.getId());
		openInfo.setTravels(travelService.getTravels(user));		
		response.setOpenInfo(openInfo);
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}

//	@RequestMapping(value = "/{userId}/recycle", method = RequestMethod.DELETE)
//	@ResponseBody
//	public UserResponse emptyRecycleBin(@PathVariable String userId) {
//		UserResponse response = new UserResponse();
//
//		recycleManager.emptyRecycleBin(Long.parseLong(userId));
//		
//		response.setStatus(UserResponse.Status.OK.name());
//		return response;
//	}
	
//	@RequestMapping(value = "/{userId}/recycle", method = RequestMethod.GET)
//	@ResponseBody
//	public UserResponse getRecycleBin(@PathVariable String userId) {
//		UserResponse response = new UserResponse();
//		
//		response.setRecycles(recycleService.getUserRecycleBin(Long.parseLong(userId)));
//		
//		response.setStatus(UserResponse.Status.OK.name());
//		return response;
//	}
	
//	@RequestMapping(value = "/{userId}/recycle/{recycleId}", method = RequestMethod.DELETE)
//	@ResponseBody
//	public UserResponse removeRecycle(@PathVariable String userId, @PathVariable String recycleId) {
//		UserResponse response = new UserResponse();
//
//		userSettingsManager.removeRecycle(Long.parseLong(userId), Long.parseLong(recycleId));
//		
//		response.setStatus(UserResponse.Status.OK.name());
//		return response;
//	}
//	
//	@RequestMapping(value = "/{userId}/recycle/{recycleId}/cancel", method = RequestMethod.GET)
//	@ResponseBody
//	public UserResponse cancelRecycle(@PathVariable String userId, @PathVariable String recycleId) {
//		UserResponse response = new UserResponse();
//		
//		userSettingsManager.cancelRecycle(Long.parseLong(userId), Long.parseLong(recycleId));
//		
//		response.setStatus(UserResponse.Status.OK.name());
//		return response;
//	}
	
	@RequestMapping(value = "/{userId}/mymessages/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse getMyMessages(@PathVariable String userId,
			@PathVariable String pageSize,
			@PathVariable String pageNo) {
		MessageResponse response = new MessageResponse();
		User user = userManager.getUser(userId);
		response.setMessages(messageService.getUserMessages(user, Integer.parseInt(pageSize), Integer.parseInt(pageNo)));
		
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/messages/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse getMessages(@PathVariable String userId,
			@PathVariable String pageSize,
			@PathVariable String pageNo) {
		MessageResponse response = new MessageResponse();
		User user = userManager.getUser(userId);
		// TODO debug
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			response.setMessages(messageService.getMessages(user, Integer.parseInt(pageSize), Integer.parseInt(pageNo)));
//			response.setMessages(messageQueueManager.getMessages(user, Integer.parseInt(pageSize), Integer.parseInt(pageNo)));
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		
		response.setStatus(MessageResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/following/{followingId}", method = RequestMethod.POST)
	@ResponseBody
	public UserResponse following(@PathVariable String userId, @PathVariable String followingId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			User following = userManager.getUser(followingId);
			userSettingsManager.following(user, following, true);
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/following/{followingId}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse cancelFollowing(@PathVariable String userId, @PathVariable String followingId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			User following = userManager.getUser(followingId);
			userSettingsManager.following(user, following, false);
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		
		response.setStatus(UserResponse.Status.OK.name());
		return response;
	}
	
	@RequestMapping(value = "/{userId}/circle", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getCircles(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
//		User me = UserUtil.getCurrentUser(userManager);
//		if(me.equals(user)) {
			response.setStatus(UserResponse.Status.OK.name());
			response.setCircles(circleManager.getCircles(user));
//		}else {
//			throw new AccessDeniedException("用户 " + userId + " 未授权");
//		}	
		
		return response;
	}
	
	@RequestMapping(value = "/{userId}/circle/{circleId}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getCircle(@PathVariable String userId, @PathVariable String circleId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			response.setStatus(UserResponse.Status.OK.name());
			response.setCircle(circleManager.getCircle(user, Long.parseLong(circleId)));
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		return response;
	}
	
	@RequestMapping(value = "/{userId}/follower", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getFollowers(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
//		User me = UserUtil.getCurrentUser(userManager);
//		if(me.equals(user)) {
			response.setStatus(UserResponse.Status.OK.name());
			response.setFollowers(circleManager.getUserFollowers(user));
//		}else {
//			throw new AccessDeniedException("用户 " + userId + " 未授权");
//		}
		return response;
	}
	
	@RequestMapping(value = "/{userId}/follower/{followerId}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse deleteFollower(@PathVariable String userId, @PathVariable String followerId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			response.setStatus(UserResponse.Status.OK.name());
			circleManager.deleteUserFollower(user, Long.parseLong(followerId));
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		return response;
	}
	
	@RequestMapping(value = "/{userId}/follow/suggested", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getFollowSuggested(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			response.setStatus(UserResponse.Status.OK.name());
			
			response.setFollow(circleManager.getFollowSuggested(user, 10, 1));
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		return response;
	}
}
