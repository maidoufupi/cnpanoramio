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
								  MultipartFile file);
	
	/**
	 * Store photo
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 * @throws ImageReadException
	 */
	public Photo save(Photo photo, InputStream ins) throws ImageReadException;

	/**
	 * 根据ID获取图片
	 * 
	 * @param id
	 * @return
	 */
	public Photo getPhoto(Long id);

	/**
	 * 获取文件标签信息
	 * 
	 * @param ins
	 * @param photo
	 * @return
	 * @throws ImageReadException
	 * @throws IOException
	 */
	Photo fillPhotoDetail(InputStream ins, Photo photo)
			throws ImageReadException, IOException;

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
	public Collection<Photo> getPhotosForUser(User user, int pageSize, int pageNo);
	
	/**
	 * Load the photos for the user.
	 * 
	 * @return The photos for the user.
	 */
	public Collection<Photo> getPhotosForUser(String id, int pageSize, int pageNo);

	/**
	 * Get the photos count for the user.
	 * 
	 * @return The photos count for the user.
	 */
	public int getPhotoCount(User user);
	
	/**
	 * Load a photo by id.
	 * 
	 * @param id
	 *            The id of the photo.
	 * @return The photo that was read.
	 */
	public InputStream loadPhoto(Long id);
	
	/**
	 * 对图片加星 favorite
	 * 
	 * @param photoId
	 * @param best
	 * @return
	 */
	public boolean markBest(Long photoId, Long userId, boolean best);
	
	/**
	 * 设置图片属性信息
	 * 
	 * @param photoId
	 * @param properties
	 * @return
	 */
	public boolean properties(Long photoId, PhotoProperties properties);
	
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
	 * 获取图片属性信息
	 * 
	 * @param id
	 * @return
	 */
	public PhotoProperties getPhotoProperties(Long id, Long userId);
	
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
}
