package com.cnpanoramio.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.LabelValue;
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
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.MessageResponse;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.UserOpenInfo;
import com.cnpanoramio.json.UserResponse;
import com.cnpanoramio.json.UserTransfer;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
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
	private UserSettingsManager userSettingsManager;
	
	@Autowired
	private UserSettingsService userSettingsService;

	@Autowired
	private PhotoManager photoService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private TravelManager travelManager;
	
	@Autowired
	private TravelService travelService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserTransfer getMe() {

		User me = UserUtil.getCurrentUser(userManager);
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (LabelValue labelValue : me.getRoleList()) {
			roles.put(labelValue.getValue(), Boolean.TRUE);
		}
		return new UserTransfer(me.getId(), me.getUsername(), true, roles);
	}
	
	@RequestMapping(value = "/{userId}/settings", method = RequestMethod.POST)
	@ResponseBody
	public UserResponse save(@RequestBody final UserResponse.Settings settings) {
		UserResponse reponse = new UserResponse();
		UserSettings userSettings = UserUtil.transformSettings(settings);
		userSettingsManager.save(userSettings);
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/settings", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getSettings() {
		UserResponse reponse = new UserResponse();
//		User me = UserUtil.getCurrentUser(userManager);
		UserResponse.Settings settings = userSettingsManager.getCurrentUserSettings();
		reponse.setStatus(UserResponse.Status.OK.name());
		reponse.setSettings(settings);
		return reponse;
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

		UserResponse reponse = new UserResponse();
		
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
		
		reponse.setStatus(UserResponse.Status.OK.name());
		reponse.setPhotos(photos);

		return reponse;
	}

	@RequestMapping(value = "/{userId}/photos/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotosWithPhoto(@PathVariable String userId,
			@PathVariable String photoId) {
		UserResponse reponse = new UserResponse();

		try {
			Long photoIdL = Long.parseLong(photoId);
			User user = userManager.getUser(userId);
			int num = photoService.getUserPhotoNum(user, photoIdL);
			Collection<PhotoProperties> pps = photoService
					.getUserPhotosWithPhoto(user, photoIdL);
			reponse.setStatus(UserResponse.Status.OK.name());
			reponse.setPhotoInfo(new UserResponse.PhotoInfo(photoService.getPhotoCount(user), num));
			reponse.setPhotos(pps);
		} catch (NumberFormatException ex) {
			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
			return reponse;
		}

		return reponse;
	}

	@RequestMapping(value = "/{userId}/openinfo", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getOpenInfo(@PathVariable String userId) {
		UserResponse reponse = new UserResponse();
//		try {
			Long id = Long.parseLong(userId);
			UserOpenInfo openInfo = userSettingsManager.getOpenInfo(id);
			reponse.setStatus(UserResponse.Status.OK.name());
			reponse.setOpenInfo(openInfo);
//		} catch (NumberFormatException ex) {
//			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
//			return reponse;
//		}

		return reponse;
	}

	@RequestMapping(value = "/{userId}/photos/tag/{tag}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotoCountByTag(@PathVariable String userId,
			@PathVariable String tag) {
		UserResponse reponse = new UserResponse();

		try {
			tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");

			Long userIdL = Long.parseLong(userId);
			Long count = photoService.getUserPhotoCountByTag(userIdL, tag);
			// Collection<PhotoProperties> pps =
			// photoService.getUserPhotosByTag(userIdL, tag);
			reponse.setStatus(UserResponse.Status.OK.name());
			reponse.setPhotoInfo(new UserResponse.PhotoInfo(count));
			// reponse.setPhotos(pps);
		} catch (NumberFormatException ex) {
			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		} catch (UnsupportedEncodingException e) {
			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		}

		return reponse;
	}

	@RequestMapping(value = "/{userId}/photos/tag/{tag}/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getPhotoPageByTag(@PathVariable String userId,
			@PathVariable String tag, @PathVariable String pageSize,
			@PathVariable String pageNo) {
		UserResponse reponse = new UserResponse();

		try {
			tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
			Long userIdL = Long.parseLong(userId);
			int pageSizeI, pageNoI;
			pageSizeI = Integer.valueOf(pageSize).intValue();
			pageNoI = Integer.valueOf(pageNo).intValue();

			Collection<PhotoProperties> pps = photoService
					.getUserPhotoPageByTag(userIdL, tag, pageSizeI, pageNoI);
			reponse.setStatus(UserResponse.Status.OK.name());
			reponse.setPhotos(pps);
		} catch (NumberFormatException ex) {
			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		} catch (UnsupportedEncodingException e) {
			reponse.setStatus(UserResponse.Status.ID_FORMAT_ERROR.name());
		}

		return reponse;
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	UserResponse avatarUpload(@RequestParam("file") MultipartFile file) {

		UserResponse reponse = new UserResponse();
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
			// me = userManager.get(1L);
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(UserResponse.Status.NO_AUTHORIZE.name());
			return reponse;
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
				reponse.setOpenInfo(openInfo);
				reponse.setStatus(UserResponse.Status.OK.name());
				return reponse;
			} catch (Exception e) {
				reponse.setStatus(UserResponse.Status.EXCEPTION.name());
				reponse.setInfo(e.getMessage());
				return reponse;
			}
		} else {
			log.debug("file is empty");

			reponse.setStatus(UserResponse.Status.EXCEPTION.name());
			reponse.setInfo("file is empty");
			return reponse;
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
		UserResponse reponse = new UserResponse();
		User user = userManager.getUser(userId);
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(user.getId());
		openInfo.setTags(userSettingsManager.getUserTags(user));
		reponse.setOpenInfo(openInfo);
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/tag/{tag}", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse createUserTag(@PathVariable String userId, @PathVariable String tag) throws UnsupportedEncodingException {
		UserResponse reponse = new UserResponse();
		User me = UserUtil.getCurrentUser(userManager);
		if(!me.getId().toString().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(me.getId());
		openInfo.setTags(userSettingsManager.createTag(me, tag));		
		reponse.setOpenInfo(openInfo);
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/tag/{tag}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse deleteUserTag(@PathVariable String userId, @PathVariable String tag) throws UnsupportedEncodingException {
		UserResponse reponse = new UserResponse();
		User me = UserUtil.getCurrentUser(userManager);
		if(!me.getId().toString().equals(userId)) {
			throw new AccessDeniedException("Access Denied!");
		}
		tag = new String(tag.getBytes("ISO-8859-1"), "UTF-8");
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(me.getId());
		openInfo.setTags(userSettingsManager.deleteTag(me, tag));		
		reponse.setOpenInfo(openInfo);
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/travel", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getUserTravels(@PathVariable String userId) {
		UserResponse reponse = new UserResponse();
		User user = userManager.getUser(userId);
		
		UserOpenInfo openInfo = new UserOpenInfo();
		openInfo.setId(user.getId());
		openInfo.setTravels(travelService.getTravels(user));		
		reponse.setOpenInfo(openInfo);
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}

	@RequestMapping(value = "/{userId}/recycle", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse emptyRecycleBin(@PathVariable String userId) {
		UserResponse reponse = new UserResponse();

		userSettingsManager.emptyRecycleBin(Long.parseLong(userId));
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/recycle", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse getRecycleBin(@PathVariable String userId) {
		UserResponse reponse = new UserResponse();
		
		reponse.setRecycles(userSettingsService.getRecycleBin(Long.parseLong(userId)));
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/recycle/{recycleId}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse removeRecycle(@PathVariable String userId, @PathVariable String recycleId) {
		UserResponse reponse = new UserResponse();

		userSettingsManager.removeRecycle(Long.parseLong(userId), Long.parseLong(recycleId));
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/recycle/{recycleId}/cancel", method = RequestMethod.GET)
	@ResponseBody
	public UserResponse cancelRecycle(@PathVariable String userId, @PathVariable String recycleId) {
		UserResponse reponse = new UserResponse();
		
		userSettingsManager.cancelRecycle(Long.parseLong(userId), Long.parseLong(recycleId));
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/message", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse getMessages(@PathVariable String userId) {
		MessageResponse reponse = new MessageResponse();
		
		
		
		reponse.setStatus(MessageResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/following/{followingId}", method = RequestMethod.POST)
	@ResponseBody
	public UserResponse following(@PathVariable String userId, @PathVariable String followingId) {
		UserResponse reponse = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			User following = userManager.getUser(followingId);
			userSettingsManager.following(user, following, true);
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
	
	@RequestMapping(value = "/{userId}/following/{followingId}", method = RequestMethod.DELETE)
	@ResponseBody
	public UserResponse cancelFollowing(@PathVariable String userId, @PathVariable String followingId) {
		UserResponse reponse = new UserResponse();
		User user = userManager.getUser(userId);
		User me = UserUtil.getCurrentUser(userManager);
		if(me.equals(user)) {
			User following = userManager.getUser(followingId);
			userSettingsManager.following(user, following, false);
		}else {
			throw new AccessDeniedException("用户 " + userId + " 未授权");
		}
		
		reponse.setStatus(UserResponse.Status.OK.name());
		return reponse;
	}
}
