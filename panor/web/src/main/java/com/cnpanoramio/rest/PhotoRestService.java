package com.cnpanoramio.rest;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.PhotoResponse;
import com.cnpanoramio.json.Tags;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.ViewsManager;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/photo")
public class PhotoRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserManager userManager = null;

	@Autowired
	private PhotoManager photoService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ViewsManager viewsManager;

	@RequestMapping(value = "/{photoId}/favorite", method = RequestMethod.PUT)
	@ResponseBody
	public PhotoResponse markFavorite(@PathVariable String photoId) {

		PhotoResponse reponse = new PhotoResponse();

		Long id = null;
		User me;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
			return reponse;
		}
		try {
			id = Long.parseLong(photoId);
		} catch (NumberFormatException ex) {
			reponse.setStatus(PhotoResponse.Status.ID_FORMAT_ERROR.name());
			return reponse;
		}
		try {
			photoService.markBest(id, me.getId(), true);
			reponse.setStatus(PhotoResponse.Status.OK.name());
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return reponse;
	}

	@RequestMapping(value = "/{photoId}/favorite", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoResponse removeFvorite(@PathVariable String photoId) {
		PhotoResponse reponse = new PhotoResponse();
		Long id = null;

		User me;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
			return reponse;
		}
		try {
			id = Long.parseLong(photoId);
		} catch (NumberFormatException ex) {
			reponse.setStatus(PhotoResponse.Status.ID_FORMAT_ERROR.name());
			return reponse;
		}
		try {
			photoService.markBest(id, me.getId(), false);
			reponse.setStatus(PhotoResponse.Status.OK.name());
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}		
		return reponse;
	}

	@RequestMapping(value = "/{photoId}/properties", method = RequestMethod.POST)
	@ResponseBody
	public PhotoResponse properties(@PathVariable String photoId,
			@RequestBody final PhotoProperties properties) {
		PhotoResponse reponse = new PhotoResponse();
		
		Long id = null;
		
		User me;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
			return reponse;
		}
		try {
			id = Long.parseLong(photoId);
		} catch (NumberFormatException ex) {
			reponse.setStatus(PhotoResponse.Status.ID_FORMAT_ERROR.name());
			return reponse;
		}
		try {
			Photo photo = photoService.getPhoto(id);
			if (photo.getOwner().equals(me)) {
				photoService.properties(id, properties);
				reponse.setStatus(PhotoResponse.Status.OK.name());
			} else {
				reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
			}
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}		
		return reponse;
	}

	@RequestMapping(value = "/{photoId}/camerainfo", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse cameraInfo(@PathVariable String photoId) {

		Long id = null;
		id = Long.parseLong(photoId);
		PhotoResponse reponse = new PhotoResponse();
		try {
			PhotoCameraInfo camerainfo = photoService.getCameraInfo(id);
			reponse.setStatus(PhotoResponse.Status.OK.name());
			reponse.setCamerainfo(camerainfo);
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return reponse;
	}

	@RequestMapping(value = "/{photoId}/{level}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource getPhoto(@PathVariable String photoId,
			@PathVariable int level) {

		Long id = null;
		id = Long.parseLong(photoId);
		
		Photo photo = photoService.getPhoto(id);

		File file = fileService.readFile(FileService.TYPE_IMAGE, id, photo.getFileType(), level);

		return new FileSystemResource(file);
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoResponse delete(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		PhotoResponse reponse = new PhotoResponse();
		try {
			PhotoProperties prop = photoService.delete(id);
			reponse.setStatus(PhotoResponse.Status.OK.name());
			reponse.setProp(prop);
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return reponse;
	}

	/**
	 * 获取图片常用属性
	 * 
	 * @param photoId
	 * @return
	 */
	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse get(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		PhotoResponse reponse = new PhotoResponse();
		
		Long userId = null;
		try {
			User me = UserUtil.getCurrentUser(userManager);
			userId = me.getId();
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
		}
		
		try {
			PhotoProperties prop = photoService.getPhotoProperties(id, userId);

			// 设置图片总访问量
			prop.setViews(viewsManager.getViewsCount(id));
			reponse.setStatus(PhotoResponse.Status.OK.name());
			reponse.setProp(prop);
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return reponse;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody
	String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody
	PhotoResponse handleFileUpload(@RequestParam("lat") String lat,
			@RequestParam("lng") String lng,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam("vendor") String vendor,
			@RequestParam("files[]") MultipartFile file) {

		MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
		PhotoResponse reponse = new PhotoResponse();

		try {
			User me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_AUTHORIZE.name());
			return reponse;
		}
		
		if (!file.isEmpty()) {
			try {

				PhotoProperties prop = photoService.upload(lat, lng, address,
						mVendor, file);
				reponse.setProp(prop);
				reponse.setStatus(PhotoResponse.Status.OK.name());
				return reponse;
			} catch (Exception e) {
				reponse.setStatus(PhotoResponse.Status.EXCEPTION.name());
				reponse.setInfo(e.getMessage());
				return reponse;
			}
		} else {
			log.debug("file is empty");

			reponse.setStatus(PhotoResponse.Status.EXCEPTION.name());
			reponse.setInfo("file is empty");
			return reponse;
		}
	}

	@RequestMapping(value = "/{photoId}/tag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean addTags(@PathVariable String photoId,
			@RequestBody final Tags tags) {
		log.info(tags.size());
		Long id = Long.parseLong(photoId);
		photoService.addTags(id, tags);
		return true;
	}

	@RequestMapping(value = "/{photoId}/gps", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse getGPSInfo(@PathVariable String photoId,
			@RequestParam(required = false) String vendor) {

		MapVendor mVendor;
		if (StringUtils.hasText(vendor)) {
			mVendor = PhotoUtil.getMapVendor(vendor);
		} else {
			mVendor = null;
		}
		Long id = Long.parseLong(photoId);
		PhotoResponse reponse = new PhotoResponse();
		try {
			List<PhotoGps> gps = photoService.getGPSInfo(id, mVendor);
			reponse.setGps(gps);
			;
			reponse.setStatus(PhotoResponse.Status.OK.name());
		} catch (DataAccessException ex) {
			reponse.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return reponse;
	}

}
