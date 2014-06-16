package com.cnpanoramio.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Favorite;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.PhotoGps.PhotoGpsPK;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.Tags;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.imaging.ImageInfoExtractor;
import com.cnpanoramio.service.lbs.GpsConverter;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Service("photoService")
@Transactional
public class PhotoServiceImpl implements PhotoManager {

	protected final Log log = LogFactory.getLog(getClass());

	private PhotoDao photoDao;
	private FileService fileService;
	private UserManager userManager = null;

//	private SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	private GpsConverter gpsc = new GpsConverter();

	@Autowired
	private UserSettingsDao userSettingsDao = null;

	@Autowired
	private PhotoGpsDao photoGpsDao = null;

	@Autowired
	private FavoriteDao favoriteDao = null;
	
	@Autowired
	private CommentDao commentDao = null;

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public PhotoDao getPhotoDao() {
		return photoDao;
	}

	@Autowired
	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	public FileService getFileService() {
		return fileService;
	}

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	public Photo getPhoto(Long id) {
		return photoDao.get(id);
	}

	@Override
	public Collection<Photo> getPhotosForUser(String username) {
		User user = userManager.getUserByUsername(username);
		return photoDao.getUserPhotos(user);
	}

	public Collection<Photo> getPhotosForUser(User user) {
		return photoDao.getUserPhotos(user);
	}

	public Collection<PhotoProperties> getPhotosForUser(User user,
			int pageSize, int pageNo) {
		List<Photo> photos = photoDao.getUserPhotos(user, pageSize, pageNo);
		Collection<PhotoProperties> pps = new ArrayList<PhotoProperties>();
		
		for (Photo photo : photos) {
			PhotoProperties pp = PhotoUtil.transformProperties(photo);
			pps.add(pp);
		}
		return pps;
	}

	public Collection<PhotoProperties> getPhotosForUser(String id,
			int pageSize, int pageNo) {
		User user = userManager.getUser(id);
		return this.getPhotosForUser(user, pageSize, pageNo);
	}

	/**
	 * 获取图片文件名称
	 * 
	 * @param photo
	 * @return
	 */
	public String getName(Photo photo) {
		return photo.getId() + "." + photo.getFileType();
	}

	@Override
	public Photo save(Photo photo, MultipartFile file) throws ImageReadException, IOException {

		InputStream ins = file.getInputStream();
		
		photo.setFileType(FilenameUtils.getExtension(photo.getName()));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			IOUtils.copy(ins, baos);
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] content = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream is1 = new ByteArrayInputStream(content);

		photo.setCreateDate(new Date());
		
		User user = UserUtil.getCurrentUser(userManager);

//		Object principal = SecurityContextHolder.getContext()
//				.getAuthentication().getPrincipal();
//		String username;
//		if (principal instanceof UserDetails) {
//			username = ((UserDetails) principal).getUsername();
//		} else {
//			username = principal.toString();
//		}
//
//		User user = userManager.getUserByUsername(username);
		// 先、保存photo table record
		photo.setOwner(user);
		photo = photoDao.save(photo);
		
		// 再、保存原始photo file
		fileService.saveFile(FileService.TYPE_IMAGE, photo.getId(),
				photo.getFileType(), is1);		

		// 第三、获取图片exif详细信息
		is1 = new ByteArrayInputStream(content);
		try {
//			fillPhotoDetail(is1, photo);
			new ImageInfoExtractor(is1, photo).process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 第四、新线程运行生成图片缩略图
		is1 = new ByteArrayInputStream(content);
		new Thread(new SaveThumbnailsRunnable(is1, photo.getId(),
				photo.getFileType())).start();

		return photo;
	}

	/**
	 * 新线程运行生成图片缩略图
	 * 
	 * @author any
	 * 
	 */
	private class SaveThumbnailsRunnable implements Runnable {

		private InputStream ins;
		private Long id;
		private String type;

		public SaveThumbnailsRunnable(InputStream ins, Long id, String type) {
			this.ins = ins;
			this.id = id;
			this.type = type;
		}

		@Override
		public void run() {
			fileService.saveThumbnails(FileService.TYPE_IMAGE, id, type, ins);
		}

	}

	@Override
	public int getPhotoCount(User user) {
		return photoDao.getPhotoCount(user);
	}

	@Override
	public boolean markBest(Long photoId, Long userId, boolean best) {
		Photo photo = photoDao.get(photoId);
		if (best) {
			Favorite f = new Favorite(userId);
			f.setDate(Calendar.getInstance().getTime());
			photo.addFavorite(f);
		} else {
			photo.removeFavorite(userId);
		}
		return true;
	}

	@Override
	public PhotoProperties properties(Long photoId, PhotoProperties properties) {
		Photo photo = photoDao.get(photoId);
		if (null != properties.getTitle()) {
			photo.setTitle(properties.getTitle());
		}

		if (null != properties.getDescription()) {
			photo.setDescription(properties.getDescription());
		}
		if (null != properties.getPoint()) {
			if ((null != properties.getPoint().getLat() && properties
					.getPoint().getLat() != 0)
					|| (null != properties.getPoint().getLng() && properties
							.getPoint().getLng() != 0)) {
				updatePhotoGps(photo, properties.getPoint().getLat(),
						properties.getPoint().getLng(), properties.getPoint()
								.getAddress(),
						PhotoUtil.getMapVendor(properties.getVendor()));
			}
		}
		if(null != properties.getFileSize()) {
			photo.setFileSize(properties.getFileSize());
		}
		// 是否是360°全景照片
		if(null != properties.isIs360()) {
			photo.setIs360(properties.isIs360());
		}		
		
		photoDao.save(photo);
		return getPhotoProperties(photoId, photo.getOwner().getId());
	}

	@Override
	public PhotoCameraInfo getCameraInfo(Long photoId) {
		Photo photo = photoDao.get(photoId);

		PhotoCameraInfo cameraInfo = PhotoUtil.transformCameraInfo(photo);

		return cameraInfo;
	}

	@Override
	public PhotoProperties delete(Long id) {
		PhotoProperties pp = PhotoUtil.transformProperties(photoDao.get(id));
		photoDao.remove(id);
		return pp;
	}

	@Override
	public PhotoProperties getPhotoProperties(Long id, Long userId) {
		PhotoProperties prop = PhotoUtil.transformProperties(getPhoto(id));
		// 如果用户ID不为空，则返回此图片是否被他收藏
		if (null != userId) {
			Favorite f = favoriteDao.get(id, userId);
			if (null != f) {
				prop.setFavorite(true);
			}
		}
		// 图片的评论总数
		Long count = commentDao.getCommentSize(id);
		prop.setCommentCount(count.intValue());
		return prop;
	}

	@Override
	public PhotoProperties upload(String lat, String lng, String address,
			MapVendor vendor, MultipartFile file) throws Exception {
//		InputStream ins;
		Photo photo = new Photo();

		photo.setName(file.getOriginalFilename());

//		ins = file.getInputStream();
		photo = this.save(photo, file);
	
		updatePhotoGps(photo, lat, lng, address, vendor);
		PhotoDetails detail = photo.getDetails();
		if(null != detail) {
			if(null != detail.getgPanoUsePanoramaViewer()) {
				if(detail.getgPanoUsePanoramaViewer().equalsIgnoreCase("true")) {
					photo.setIs360(true);
				}
			}
		}
		
		return PhotoUtil.transformProperties(photo);
	}

	/**
	 * 更新图片GPS信息
	 * 
	 * @param photo
	 * @param lat
	 * @param lng
	 * @param address
	 * @param vendor
	 */
	private void updatePhotoGps(Photo photo, String lat, String lng,
			String address, MapVendor vendor) {
		Double latDouble = 0D;
		Double lngDouble = 0D;
		if (StringUtils.hasText(lat) && StringUtils.hasText(lng)) {
			latDouble = Double.valueOf(lat);
			lngDouble = Double.valueOf(lng);
			updatePhotoGps(photo, latDouble, lngDouble, address, vendor);
		}
	}

	/**
	 * 更新图片GPS信息
	 * 
	 * @param photo
	 * @param latDouble
	 * @param lngDouble
	 * @param address
	 * @param vendor
	 */
	private void updatePhotoGps(Photo photo, Double latDouble,
			Double lngDouble, String address, MapVendor vendor) {

		PhotoGps gps = null;

		PhotoDetails detail = photo.getDetails();
		Point point = null;
		if (latDouble != 0 || lngDouble != 0) {
			if (vendor.equals(MapVendor.gps)) {
				// 用户设置GPS为标准GPS坐标
				// 转化GPS坐标为高德地图坐标
				GpsConverter.Point p = gpsc.getEncryPoint(lngDouble, latDouble);
				point = new Point(p.getY(), p.getX(), 0D);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				photo.setGpsPoint(point);
				photoDao.save(photo);

				gps = new PhotoGps();
				gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gaode));
				gps.setGps(point);
				photoGpsDao.save(gps);

				// 保存原始GPS坐标
				point = new Point(latDouble, lngDouble);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				gps = new PhotoGps();
				gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gps));
				gps.setGps(point);
				photoGpsDao.save(gps);
			} else {
				// 用户设置位置不为GPS，则默认为高德地图坐标
				point = new Point(latDouble, lngDouble);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				photo.setGpsPoint(point);
				photoDao.save(photo);

				gps = new PhotoGps();
				gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gaode));
				gps.setGps(point);
				photoGpsDao.save(gps);

				// 保存图片原有GPS信息为GPS坐标
				if (null != detail
						&& (null != detail.getGPSLatitude()
								&& detail.getGPSLatitude() != 0
								&& null != detail.getGPSLongitude() && detail
								.getGPSLongitude() != 0)) {
					gps = new PhotoGps();
					gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gps));
					point = new Point(detail.getGPSLatitude(),
							detail.getGPSLongitude(), detail.getGPSAltitude());
					gps.setGps(point);
					photoGpsDao.save(gps);
				}
			}
		} else {
			// 如果用户没有设置位置，则使用图片自带GPS信息
			if (null != detail
					&& (null != detail.getGPSLatitude()
							&& detail.getGPSLatitude() != 0
							&& null != detail.getGPSLongitude() && detail
							.getGPSLongitude() != 0)) {
				lngDouble = detail.getGPSLongitude();
				latDouble = detail.getGPSLatitude();

				// 转化GPS坐标为高德地图坐标
				GpsConverter.Point p = gpsc.getEncryPoint(lngDouble, latDouble);
				point = new Point(p.getY(), p.getX());
				photo.setGpsPoint(point);
				photoDao.save(photo);

				gps = new PhotoGps();
				gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gaode));
				gps.setGps(point);
				photoGpsDao.save(gps);

				// 保存原始GPS坐标
				point = new Point(latDouble, lngDouble);
				gps = new PhotoGps();
				gps.setPk(new PhotoGpsPK(photo.getId(), MapVendor.gps));
				gps.setGps(point);
				photoGpsDao.save(gps);
			}
		}
	}

	@Override
	public Set<Tag> addTags(Long id, Tags tags) {
		Photo photo = photoDao.get(id);
		// 权限检查
		checkIsMyPhoto(photo);

		UserSettings user = userSettingsDao.get(photo.getOwner().getId());

		photo.getTags().clear();
		for (Tag tag : tags) {
			Tag t = userSettingsDao.getOrCreateUserTag(user, tag.getTag());
			photo.addTag(t);
			t.getPhoto().add(photo);
		}
		return photo.getTags();
	}

	@Override
	public List<PhotoGps> getGPSInfo(Long id, MapVendor vendor) {
		if (null == vendor) {
			return photoGpsDao.getAll(id);
		} else {
			PhotoGps gps = photoGpsDao.get(new PhotoGps.PhotoGpsPK(id, vendor));
			List<PhotoGps> gpss = new ArrayList<PhotoGps>();
			gpss.add(gps);
			return gpss;
		}
	}

	@Override
	public int getUserPhotoNum(User user, Long photoId) {

		int num = 0;
		boolean exist = false;

		Collection<Photo> photos = this.getPhotosForUser(user);
		for (Photo photo : photos) {
			num++;
			if (photo.getId().equals(photoId)) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			num = 0;
		}
		return num;
	}

	@Override
	public Collection<PhotoProperties> getUserPhotosWithPhoto(User user,
			Long photoId) {

		Collection<PhotoProperties> pps = new ArrayList<PhotoProperties>();
		int num = 0;
		boolean exist = false;
		Photo p1 = null;
		Photo p2 = null;
		Photo p4 = null;
		Photo p5 = null;
		Photo p6 = null;

		Collection<Photo> photos = this.getPhotosForUser(user);
		for (Photo photo : photos) {
			if (exist) {
				num++;
				p4 = p5;
				p5 = p6;
				p6 = photo;
			}

			if (photo.getId().equals(photoId)) {
				exist = true;
				if (null != p1) {
					pps.add(PhotoUtil.transformProperties(p1));
				}

				if (null != p2) {
					pps.add(PhotoUtil.transformProperties(p2));
				}

				pps.add(PhotoUtil.transformProperties(photo));
			}
			if (num > 2) {
				if (null != p4) {
					pps.add(PhotoUtil.transformProperties(p4));
				}

				if (null != p5) {
					pps.add(PhotoUtil.transformProperties(p5));
				}
				if (null != p6) {
					pps.add(PhotoUtil.transformProperties(p6));
				}
				break;
			}
			p1 = p2;
			p2 = photo;
		}
		return pps;
	}

	@Override
	public Long getUserPhotoCountByTag(Long userId, String tag) {
		User user = userManager.get(userId);
		return photoDao.getUserPhotoCountBytag(user, tag);
	}

	@Override
	public Collection<PhotoProperties> getUserPhotosByTag(Long userId,
			String tag) {
		User user = userManager.get(userId);
		List<Photo> photos = photoDao.getUserPhotosByTag(user, tag);
		Collection<PhotoProperties> pps = new ArrayList<PhotoProperties>();
		;
		for (Photo photo : photos) {
			PhotoProperties pp = PhotoUtil.transformProperties(photo);
			pps.add(pp);
		}
		return pps;
	}

	@Override
	public Collection<PhotoProperties> getUserPhotoPageByTag(Long userId,
			String tag, int pageSize, int pageNo) {
		User user = userManager.get(userId);
		List<Photo> photos = photoDao.getUserPhotoPageByTag(user, tag,
				pageSize, pageNo);
		Collection<PhotoProperties> pps = new ArrayList<PhotoProperties>();
		;
		for (Photo photo : photos) {
			PhotoProperties pp = PhotoUtil.transformProperties(photo);
			pps.add(pp);
		}
		return pps;
	}

	/**
	 * 权限检查：是否是操作属于自己的图片
	 * 
	 * @param photo
	 * @return
	 */
	private boolean checkIsMyPhoto(Photo photo) {
		User me = UserUtil.getCurrentUser(userManager);
		if (photo.getOwner().equals(me)) {
			return true;
		} else {
			throw new AccessDeniedException("Access Denied! Photo Id: "
					+ photo.getId());
		}
	}

}
