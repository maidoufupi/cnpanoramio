package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.imaging.ImageReadException;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.cnpanoramio.domain.Photo;

public interface PhotoManager {
	
	/**
	 * Store photo
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 * @throws ImageReadException
	 */
	public Photo save(String fileName, InputStream ins) throws ImageReadException;

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
	 * Load the photos for the current user.
	 * 
	 * @return The photos for the current user.
	 */
	public Collection<Photo> getPhotosForUser(String username);

	/**
	 * Load a photo by id.
	 * 
	 * @param id
	 *            The id of the photo.
	 * @return The photo that was read.
	 */
	public InputStream loadPhoto(Long id);
	
}
