package com.cnpanoramio.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
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

import com.cnpanoramio.Constant;
import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.PhotoResponse;
import com.cnpanoramio.json.Tags;
import com.cnpanoramio.service.CommentService;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.LikeManager;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;
import com.cnpanoramio.service.ViewsManager;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Controller
@RequestMapping("/api/rest/photo")
public class PhotoRestService extends AbstractRestService {

	private transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserManager userManager = null;

	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private PhotoService photoService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ViewsManager viewsManager;

	@Autowired
	private CommentService commentService;

	@Autowired
	private LikeManager likeManager;

	@Override
	protected PhotoResponse responseFactory() {
		PhotoResponse response = new PhotoResponse();
		response.setStatus(PhotoResponse.Status.OK.name());
		return response;
	}

	@RequestMapping(value = "/{photoId}/favorite", method = RequestMethod.PUT)
	@ResponseBody
	public PhotoResponse markFavorite(@PathVariable String photoId) {

		PhotoResponse response = new PhotoResponse();

		User me = UserUtil.getCurrentUser(userManager);
		Long id = Long.parseLong(photoId);

		try {
			photoManager.markBest(id, me.getId(), true);
			response.setStatus(PhotoResponse.Status.OK.name());
		} catch (DataAccessException ex) {
			response.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return response;
	}

	@RequestMapping(value = "/{photoId}/favorite", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoResponse removeFvorite(@PathVariable String photoId) {
		PhotoResponse response = new PhotoResponse();
		User me = UserUtil.getCurrentUser(userManager);
		Long id = Long.parseLong(photoId);
		try {
			photoManager.markBest(id, me.getId(), false);
			response.setStatus(PhotoResponse.Status.OK.name());
		} catch (DataAccessException ex) {
			response.setStatus(PhotoResponse.Status.NO_ENTITY.name());
		}
		return response;
	}

	@RequestMapping(value = "/{photoId}/properties", method = RequestMethod.POST)
	@ResponseBody
	public PhotoResponse properties(@PathVariable String photoId,
			@RequestBody final PhotoProperties properties) {

		// debug
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		try {
			log.debug("input param:" + ow.writeValueAsString(properties));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PhotoResponse response = responseFactory();

		Long id = Long.parseLong(photoId);

		User me = UserUtil.getCurrentUser(userManager);

		Photo photo = photoManager.get(id);
		if (photo.getOwner().equals(me)) {
			response.setProp(photoManager.properties(id, properties));
		} else {
			throw new AccessDeniedException("Access Denied! Photo Id: "
					+ photo.getId());
		}
		return response;
	}

	@RequestMapping(value = "/{photoId}/camerainfo", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse cameraInfo(@PathVariable String photoId) {

		Long id = Long.parseLong(photoId);
		PhotoResponse response = new PhotoResponse();

		PhotoCameraInfo camerainfo = photoManager.getCameraInfo(id);

		// 图片被查看
		viewsManager.view(id, Constant.C_APP_MAIN);
		response.setStatus(PhotoResponse.Status.OK.name());
		response.setCamerainfo(camerainfo);

		return response;
	}

	@RequestMapping(value = "/{photoId}/oss", method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public void getOssPhoto(@PathVariable String photoId,
			HttpServletResponse response) throws IOException {

		Long id = Long.parseLong(photoId);
		Photo photo = null;
		try {
			photo = photoManager.get(id);
		} catch (DataAccessException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		InputStream is = fileService.readAsInputStream(FileService.TYPE_IMAGE,
				id, photo.getFileType(), "");

		if (null != is) {
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			// 设置http请求的缓存，让浏览器缓存此图片(一个月期限)
			response.setHeader("Cache-Control", "public, max-age=2629000");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 1);
			response.setDateHeader("Expires", c.getTimeInMillis());
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			// response.sendError(404);
		}
	}

	@RequestMapping(value = "/{photoId}/{level}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource getPhoto(@PathVariable String photoId,
			@PathVariable int level, HttpServletResponse response)
			throws IOException {

		Long id = Long.parseLong(photoId);
		Photo photo = null;
		try {
			photo = photoManager.get(id);
		} catch (DataAccessException ex) {
			return null;
		}

		File file = fileService.readFile(FileService.TYPE_IMAGE, id,
				photo.getFileType(), String.valueOf(level));

		// 找不到
		if (null == file && 0 != level) {
			file = fileService.readFile(FileService.TYPE_IMAGE, id,
					photo.getFileType(), "0");
		}
		if (null != file) {
			// 设置http请求的缓存，让浏览器缓存此图片(一个月期限)
			response.setHeader("Cache-Control", "public, max-age=2629000");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 1);
			response.setDateHeader("Expires", c.getTimeInMillis());

			return new FileSystemResource(file);
		} else {
			// response.sendError(404);
			return null;
		}
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoResponse delete(@PathVariable String photoId) {
		Long id = Long.parseLong(photoId);
		PhotoResponse response = new PhotoResponse();

		PhotoProperties prop = photoManager.delete(id);
		response.setStatus(PhotoResponse.Status.OK.name());
		response.setProp(prop);

		return response;
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
		Long id = Long.parseLong(photoId);
		PhotoResponse response = responseFactory();

		User me = null;

		// 如果查看图片的是登录用户，则查询图片相对于此用户的信息
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
		}

		PhotoProperties prop = photoService.getPhotoProperties(id, me);

		// 设置图片总访问量
		prop.setViews(viewsManager.getViewsCount(id));

		response.setStatus(PhotoResponse.Status.OK.name());
		response.setProp(prop);

		return response;
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
			@RequestParam("files[]") MultipartFile file) throws Exception {

		MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
		PhotoResponse response = new PhotoResponse();

		User me = UserUtil.getCurrentUser(userManager);

		if (!file.isEmpty()) {
			PhotoProperties prop = photoManager.upload(lat, lng, address,
					mVendor, file);
			response.setProp(prop);
			response.setStatus(PhotoResponse.Status.OK.name());
		} else {
			log.debug("file is empty");
			response.setStatus(PhotoResponse.Status.EXCEPTION.name());
			response.setInfo("file is empty");
		}
		return response;
	}

	@RequestMapping(value = "/{photoId}/tag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PhotoResponse addTags(@PathVariable String photoId,
			@RequestBody final Tags tags) {
		log.info("add " + tags.size() + " tags to " + photoId);
		PhotoResponse response = responseFactory();
		Long id = Long.parseLong(photoId);
		photoManager.addTags(id, tags);
		return response;
	}

	@RequestMapping(value = "/{photoId}/gps", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse getGPSInfo(@PathVariable String photoId,
			@RequestParam(required = false) String vendor) {

		MapVendor mVendor;
		if (StringUtils.hasText(vendor)) {
			mVendor = PhotoUtil.getMapVendor(vendor);
		} else {
			mVendor = MapVendor.gaode;
		}
		PhotoResponse response = new PhotoResponse();
		PhotoGps gps = photoManager.getGPSInfo(Long.parseLong(photoId), mVendor);
		if(null != gps) {
			gps.setPhoto(null);
			response.setGps(gps);
			response.setStatus(PhotoResponse.Status.OK.name());
		}else {
			throw new EmptyResultDataAccessException(0);
		}
		
		return response;
	}

	@RequestMapping(value = "/{photoId}/comment", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse getComments(@PathVariable String photoId) {
		PhotoResponse response = PhotoResponse.getInstance();
		Long photoIdL = Long.parseLong(photoId);
		
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
		}

		response.setComments(commentService.getPhotoComments(photoIdL, me));
		return response;
	}
	
	@RequestMapping(value = "/{photoId}/comment/{pageSize}/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse getComments(@PathVariable String photoId,
			@PathVariable String pageSize, @PathVariable String pageNo) {
		PhotoResponse response = PhotoResponse.getInstance();
		Long photoIdL = Long.parseLong(photoId);
		Integer pageSizeI = Integer.parseInt(pageSize);
		Integer pageNoI = Integer.parseInt(pageNo);
		
		User me = null;
		try {
			me = UserUtil.getCurrentUser(userManager);
		} catch (UsernameNotFoundException ex) {
		}
		
		response.setComments(commentService.getComments(photoIdL, pageSizeI,
				pageNoI, me));
		return response;
	}

	@RequestMapping(value = "/{photoId}/like", method = RequestMethod.GET)
	@ResponseBody
	public PhotoResponse likePhoto(@PathVariable String photoId) {
		PhotoResponse response = PhotoResponse.getInstance();
		
		User me = UserUtil.getCurrentUser(userManager);
		
		likeManager.likePhoto(me, Long.parseLong(photoId));
		return response;
	}

	@RequestMapping(value = "/{photoId}/like", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoResponse unLikePhoto(@PathVariable String photoId) {
		PhotoResponse response = PhotoResponse.getInstance();
		
		User me = UserUtil.getCurrentUser(userManager);
		
		likeManager.likePhoto(me, Long.parseLong(photoId));
		return response;
	}

}
