package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.imaging.ImageReadException;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.cnpanoramio.domain.Photo;

@Path("/photos")
public interface PhotoService {

	/**
	 * Store photo
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 * @throws ImageReadException
	 */
	@Produces("application/json")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Photo store(
	 @Multipart(value = "lat", type = "text/plain") String lat,
     @Multipart(value = "lng", type = "text/plain") String lng,
     @Multipart(value = "address", type = "text/plain") String address,
     @Multipart(value = "image", type = "image/jpeg") Attachment image) throws ImageReadException;;

	/**
	 * 根据ID获取图片
	 * 
	 * @param id
	 * @return
	 */
	@GET
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
	@Path("/{id}/{level}")
	@Produces("image/*")
	@GET
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response read(@PathParam("id") Long id, @PathParam("level") int level);

}
