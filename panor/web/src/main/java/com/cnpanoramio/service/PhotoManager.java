package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.imaging.ImageReadException;
import org.appfuse.model.User;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.Tags;

public interface PhotoManager {
	
	/**
	 * 上传图片
	 * 
	 * @param lat
	 * @param lng
	 * @param address
	 * @param vendor
	 * @param file
	 * @return
	 */
	public PhotoProperties upload(String lat,
								  String lng,
								  String address,
								  MapVendor vendor,
								  MultipartFile file) throws Exception;
	
	/**
	 * 
	 * @param photo
	 * @param file
	 * @return
	 * @throws ImageReadException
	 */
	public Photo save(Photo photo, MultipartFile file) throws ImageReadException, IOException;

	/**
	 * 根据ID获取图片
	 * 
	 * @param id
	 * @return
	 */
	public Photo getPhoto(Long id);

	/**
	 * Load the photos for the user.
	 * 
	 * @return The photos for the current user.
	 */
	public Collection<Photo> getPhotosForUser(String username);
	
	/**
	 * Load the photos for the user.
	 * 
	 * @return The photos for the current user.
	 */
	public Collection<Photo> getPhotosForUser(User user);
	
	/**
	 * Load the photos for the user.
	 * 
	 * @return The photos for the current user.
	 */
	public Collection<PhotoProperties> getPhotosForUser(User user, int pageSize, int pageNo);
	
	/**
	 * Load the photos for the user.
	 * 
	 * @return The photos for the user.
	 */
	public Collection<PhotoProperties> getPhotosForUser(String id, int pageSize, int pageNo);

	/**
	 * Get the photos count for the user.
	 * 
	 * @return The photos count for the user.
	 */
	public Long getPhotoCount(User user);
	
	/**
	 * 获取用户图片在用户全部图片中的位置（按时间由大到小排序）
	 * 
	 * @param user
	 * @return
	 */
	public int getUserPhotoNum(User user, Long photoId);
	
	/**
	 * 获取用户图片分页（按时间由大到小排序）
	 * 
	 * @param user
	 * @param photoId
	 * @return
	 */
	public Collection<PhotoProperties> getUserPhotosWithPhoto(User user, Long photoId);
	
	/**
	 * Load a photo by id.
	 * 
	 * @param id
	 *            The id of the photo.
	 * @return The photo that was read.
	 */
//	public InputStream loadPhoto(Long id);
	
	/**
	 * 对图片加星 favorite
	 * 
	 * @param photoId
	 * @param best
	 * @return
	 */
	public Boolean markBest(Long photoId, Long userId, boolean best);
	
	/**
	 * 设置图片属性信息
	 * 
	 * @param photoId
	 * @param properties
	 * @return
	 */
	public PhotoProperties properties(Long photoId, PhotoProperties properties);
	
	/**
	 * 获取图片相机信息
	 * 
	 * @param photoId
	 * @return
	 */
	public PhotoCameraInfo getCameraInfo(Long photoId);
	
	/**
	 * 删除图片
	 * 
	 * @param id
	 * @return
	 */
	public PhotoProperties delete(Long id);
	
	/**
	 * 撤销删除图片
	 * 
	 * @param id
	 * @return
	 */
	public PhotoProperties cancelDelete(Long id);
	
	/**
	 * 实际删除图片
	 * 
	 * @param id
	 * @return
	 */
	public void removePhoto(Long id);
	
	/**
	 * 获取图片属性信息
	 * 
	 * @param id
	 * @return
	 */
	public PhotoProperties getPhotoProperties(Long id, User User);
	
	/**
	 * 为图片添加tag
	 * 
	 * @param id
	 * @param tags
	 * @return
	 */
	public Set<Tag> addTags(Long id, Tags tags);
	
	/**
	 * 获取图片GPS位置信息
	 * 
	 * @param id
	 * @return
	 */
	public List<PhotoGps> getGPSInfo(Long id, MapVendor vendor);
	
	/**
	 * 获取用户某个tag的图片总数
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Long getUserPhotoCountByTag(Long userId, String tag);
	
	/**
	 * 获取用户某个tag的所有图片
	 * 
	 * @param user
	 * @param tag
	 * @return
	 */
	public Collection<PhotoProperties> getUserPhotosByTag(Long userId, String tag);
	
	/**
	 * 获取用户某个tag的图片(分页)
	 * 
	 * @param userId
	 * @param tag
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public Collection<PhotoProperties> getUserPhotoPageByTag(Long userId, String tag, int pageSize, int pageNo);
}
