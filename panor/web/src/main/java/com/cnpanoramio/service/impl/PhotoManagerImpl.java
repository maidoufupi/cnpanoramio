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
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.dao.LikeDao;
import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.dao.PhotoGpsDao;
import com.cnpanoramio.dao.TagDao;
import com.cnpanoramio.dao.UserSettingsDao;
import com.cnpanoramio.domain.Favorite;
import com.cnpanoramio.domain.Like;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.domain.Recycle;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.Tags;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.ViewsManager;
import com.cnpanoramio.service.imaging.ImageMetadataExtractor;
import com.cnpanoramio.service.lbs.BDConverter;
import com.cnpanoramio.service.lbs.GpsConverter;
import com.cnpanoramio.service.lbs.LatLng;
import com.cnpanoramio.utils.PhotoUtil;
import com.cnpanoramio.utils.UserUtil;

@Service
@Transactional
public class PhotoManagerImpl extends GenericManagerImpl<Photo, Long> implements PhotoManager {

	protected final Log log = LogFactory.getLog(getClass());

	private PhotoDao photoDao;
	private FileService fileService;
	private UserManager userManager = null;

	private GpsConverter gpsc = new GpsConverter();

	@Autowired
	private UserSettingsDao userSettingsDao = null;
	
	@Autowired
	private TagDao tagDao;

	@Autowired
	private PhotoGpsDao photoGpsDao = null;

	@Autowired
	private FavoriteDao favoriteDao = null;
	
	@Autowired
	private CommentDao commentDao = null;
	
	@Autowired
	private LikeDao likeDao = null;
	
	@Autowired
	private RecycleManager recycleManager;
	
	@Autowired
	private ViewsManager viewsManager;
	
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
		this.dao = photoDao;
	}

	public FileService getFileService() {
		return fileService;
	}

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
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

		// 先、保存photo table record
		photo.setOwner(user);
		photo = photoDao.save(photo);
		
		// 再、保存原始photo file
		// TODO debug
		fileService.saveFile(FileService.TYPE_IMAGE, photo.getId(),
				photo.getFileType(), is1);		

		// 第三、获取图片exif详细信息
		is1 = new ByteArrayInputStream(content);
		try {
			new ImageMetadataExtractor(is1, photo).process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return photo;
	}

	@Override
	public Long getPhotoCount(User user) {
		return photoDao.getPhotoCount(user);
	}

	@Override
	public Boolean markBest(Long photoId, Long userId, boolean best) {
		Photo photo = photoDao.get(photoId);
		User user = userManager.get(userId);
		Favorite f = photo.getFavorites().get(user);
		if (best) {
			
			if(null == f) {
				f = new Favorite(user, photo);
				f.setDate(Calendar.getInstance().getTime());
				f = favoriteDao.save(f);
				photo.getFavorites().put(user, f);
			}
		} else {
			if(null != f) {
				photo.getFavorites().remove(user);
				favoriteDao.remove(f);
			}			
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

			updatePhotoGps(photo, properties.getPoint().getLat(),
					properties.getPoint().getLng(), properties.getPoint()
							.getAddress(),
					PhotoUtil.getMapVendor(properties.getVendor()));
			
		}

		// 是否是360°全景照片
		if(null != properties.isIs360()) {
			photo.setIs360(properties.isIs360());
		}		
		
		photoDao.save(photo);
		return getPhotoProperties(photoId, photo.getOwner());
	}

	@Override
	public PhotoCameraInfo getCameraInfo(Long photoId) {
		Photo photo = photoDao.get(photoId);

		PhotoCameraInfo cameraInfo = PhotoUtil.transformCameraInfo(photo);

		return cameraInfo;
	}

	@Override
	public PhotoProperties delete(Long id) {
		User me = UserUtil.getCurrentUser(userManager);
		Photo photo = photoDao.get(id);
		photo.setDeleted(true);
		
		recycleManager.saveRecycle(me, Recycle.RecycleType.photo, photo.getId());
						
		PhotoProperties pp = PhotoUtil.transformProperties(photo);
				
		return pp;
	}

	@Override
	public PhotoProperties getPhotoProperties(Long id, User user) {
		
		Photo photo = this.get(id);
		PhotoProperties prop = PhotoUtil.transformProperties(photo);
		// 返回此图片是否被他 收藏 赞
		if(null != user) {
			Like like = likeDao.getPhoto(photo, user);
			if(null != like) {
				prop.setLike(true);
			}
			Favorite f = favoriteDao.get(id, user.getId());
			if (null != f) {
				prop.setFavorite(true);
			}
		}
		return prop;
	}

	@Override
	public PhotoProperties upload(String lat, String lng, String address,
			MapVendor vendor, MultipartFile file) throws Exception {

		Photo photo = new Photo();

		// 文件名
		photo.setName(file.getOriginalFilename());
		// 文件大小
		photo.setFileSize(file.getSize());

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
		
		// 获取相应供应商的gps
		log.debug("upload photo vendor: " + vendor.name());
		PhotoProperties pp = PhotoUtil.transformProperties(photo);
		if(null != vendor) {
			pp.setVendor(vendor.name());
			PhotoGps gps = photo.getGps().get(vendor);
			log.debug("upload photo vendor's gps: " + gps);
			if(null != gps) {
				pp.setPoint(gps.getPoint());
			}
		}
		return pp;
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

		PhotoDetails detail = photo.getDetails();
		// 高度
		Double alt = 0D;
		if(null != detail) {
			alt = detail.getGPSAltitude();
		}
		Point point = null;
		if ((null != latDouble && latDouble != 0) || (null!=lngDouble && lngDouble != 0)) {
			if (vendor == MapVendor.gps) {
				// 用户设置GPS为标准GPS坐标
				// 转化GPS坐标为火星坐标
				LatLng p = gpsc.getEncryPoint(lngDouble, latDouble);
				point = new Point(p.getLat(), p.getLng(), alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				photo.setGpsPoint(point);
				photoDao.save(photo);
				
				saveOrUpdatePhotoGps(photo, MapVendor.gaode, point);
				
				// 转化火星坐标到百度摩卡拖坐标
				p = BDConverter.bd_encrypt(p.lat, p.lng);
				point = new Point(p.getLat(), p.getLng(), alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				saveOrUpdatePhotoGps(photo, MapVendor.baidu, point);

				// 保存原始GPS坐标
				point = new Point(latDouble, lngDouble, alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				saveOrUpdatePhotoGps(photo, MapVendor.gps, point);

			} else if(vendor == MapVendor.baidu) {
				// 用户设置为baidu，则默认为baidu坐标
				point = new Point(latDouble, lngDouble, alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				saveOrUpdatePhotoGps(photo, MapVendor.baidu, point);
				
				// 转化百度摩卡拖坐标 为 火星坐标
				LatLng p = BDConverter.bd_decrypt(latDouble, lngDouble);
				point = new Point(p.lat, p.lng, alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				photo.setGpsPoint(point);
				photoDao.save(photo);

				saveOrUpdatePhotoGps(photo, MapVendor.gaode, point);
				
				// 保存图片原有GPS信息为GPS坐标
				if (null != detail
						&& (null != detail.getGPSLatitude()
								&& detail.getGPSLatitude() != 0
								&& null != detail.getGPSLongitude() && detail
								.getGPSLongitude() != 0)) {
					point = new Point(detail.getGPSLatitude(),
							detail.getGPSLongitude(), detail.getGPSAltitude());
					saveOrUpdatePhotoGps(photo, MapVendor.gps, point);
				}
				
			}else {
				// 用户设置位置不为GPS和baidu，则默认为火星坐标
				point = new Point(latDouble, lngDouble, alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				photo.setGpsPoint(point);
				photoDao.save(photo);

				saveOrUpdatePhotoGps(photo, MapVendor.gaode, point);
				
				// 转化火星坐标到百度摩卡拖坐标
				LatLng p = BDConverter.bd_encrypt(latDouble, lngDouble);
				point = new Point(p.getLat(), p.getLng(), alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				saveOrUpdatePhotoGps(photo, MapVendor.baidu, point);

				// 保存图片原有GPS信息为GPS坐标
				if (null != detail
						&& (null != detail.getGPSLatitude()
								&& detail.getGPSLatitude() != 0
								&& null != detail.getGPSLongitude() && detail
								.getGPSLongitude() != 0)) {
					point = new Point(detail.getGPSLatitude(),
							detail.getGPSLongitude(), detail.getGPSAltitude());
					saveOrUpdatePhotoGps(photo, MapVendor.gps, point);
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

				// 转化GPS坐标为火星坐标
				LatLng p = gpsc.getEncryPoint(lngDouble, latDouble);
				point = new Point(p.lat, p.lng, alt);
				photo.setGpsPoint(point);
				photoDao.save(photo);

				saveOrUpdatePhotoGps(photo, MapVendor.gaode, point);
				
				// 转化火星坐标到百度摩卡拖坐标
				p = BDConverter.bd_encrypt(p.lat, p.lng);
				point = new Point(p.getLat(), p.getLng(), alt);
				if (StringUtils.hasText(address)) {
					point.setAddress(address.trim());
				}
				saveOrUpdatePhotoGps(photo, MapVendor.baidu, point);

				// 保存原始GPS坐标
				point = new Point(latDouble, lngDouble, alt);
				saveOrUpdatePhotoGps(photo, MapVendor.gps, point);
			}
		}
	}
	
	/**
	 * 保存图片相应标准的gps信息
	 * 
	 * @param photo
	 * @param vendor
	 * @param lat
	 * @param lng
	 */
	private void saveOrUpdatePhotoGps(Photo photo, MapVendor vendor, Point point) {
		PhotoGps gps = photo.getGps().get(vendor);
		if(null == gps) {
			gps = new PhotoGps(photo, vendor, point);
			gps = photoGpsDao.save(gps);
			photo.getGps().put(vendor, gps);
		}else {
			Point pointOrign = gps.getPoint();
			// 如果点没有高度信息，则保留原有高度属性值
			if(null == point.getAlt()) {
				point.setAlt(pointOrign.getAlt());
			}
			gps.setPoint(point);
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
			Tag t = tagDao.getOrCreateUserTag(user, tag.getContent());
			photo.addTag(t);
		}
		return photo.getTags();
	}

	@Override
	public PhotoGps getGPSInfo(Long id, MapVendor vendor) {
		return photoDao.get(id).getGps().get(vendor);		
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

	@Override
	public PhotoProperties cancelDelete(Long id) {
		Photo photo = photoDao.get(id);
		checkIsMyPhoto(photo);
		photo.setDeleted(false);
		
		// 如果图片有旅行相册并被删除，则恢复相册
		if(null != photo.getTravelSpot()) {
			Travel travel = photo.getTravelSpot().getTravel();
			if(travel.isDeleted()) {
				travel.setDeleted(false);
			}
		}		
						
		PhotoProperties pp = PhotoUtil.transformProperties(photo);
				
		return pp;
	}

	@Override
	public void removePhoto(Long id) {
		Photo photo = this.get(id);
		
		// 删除oss图片文件 TODO DEBUG
		fileService.deleteFile(FileService.TYPE_IMAGE, photo.getId(), photo.getFileType());

		// 删除对应的views
		viewsManager.removePhotoViews(photo);
		
		// 删除对应的gps
		List<PhotoGps> gpss = photoGpsDao.getAll(id);
		for(PhotoGps gps : gpss) {
			photoGpsDao.remove(gps);
		}
				
		// 删除数据库记录
		photoDao.remove(photo);
	}

	@Override
	public List<Photo> getNoTravel(User user) {
		return photoDao.getNoTravel(user);
	}
}
