package com.cnpanoramio.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;

@Service("photoService")
@Transactional
public class PhotoServiceImpl implements PhotoService, PhotoManager {

	protected final Log log = LogFactory.getLog(getClass());

	private PhotoDao photoDao;
	private FileService fileService;
	private UserManager userManager = null;

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
	public Photo store(String lat, String lng, String address, Attachment image) throws ImageReadException {

		ContentDisposition content = image.getContentDisposition();
		String fileName = content.getParameter("filename");
		DataHandler dh = image.getDataHandler();
		InputStream ins = null;
		try {
			ins = dh.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Photo photo = this.save(fileName, ins);
		
		Double latDouble = Double.valueOf(lat);
		Double lngDouble = Double.valueOf(lng);
		Point point;
		if (latDouble != 0 || lngDouble != 0) {
			point = new Point(latDouble, lngDouble);
			address = address.trim();
			if (address != null && address != "") {
				point.setAddress(address);
			}
			photo.setGpsPoint(point);
		}

		return photo;
	}
	
//	@Override
//	public Photo store(MultipartBody body) throws ImageReadException {
//
//		Attachment attachment = body.getAttachment(contentId);
//		ContentDisposition content = attachment.getContentDisposition();
//		String fileName = content.getParameter("filename");
//		
//		String lat = null;
//		String lng = null;
//		String address = null;
//		DataHandler dh = body.getAttachment(CON_LAT).getDataHandler();
//		try {
//			lat = dh.getContent().toString();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}		
//
//		dh = body.getAttachment(CON_LNG).getDataHandler();
//		try {
//			lng = dh.getContent().toString();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		dh = body.getAttachment(CON_ADDRESS).getDataHandler();
//		try {
//			address = dh.getContent().toString();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		dh = attachment.getDataHandler();
//		InputStream ins = null;
//		try {
//			ins = dh.getInputStream();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Photo photo = this.save(fileName, ins);
//		
//		Double latDouble = Double.valueOf(lat);
//		Double lngDouble = Double.valueOf(lng);
//		Point point;
//		if (latDouble != 0 || lngDouble != 0) {
//			point = new Point(latDouble, lngDouble);
//			address = address.trim();
//			if (address != null && address != "") {
//				point.setAddress(address);
//			}
//			photo.setGpsPoint(point);
//		}
//
//		return photo;
//	}

	@Override
	public Photo fillPhotoDetail(InputStream is, Photo photo)
			throws ImageReadException, IOException {
		Point point = null;
		IImageMetadata metadata = null;
		try {
			metadata = Imaging.getMetadata(is, "");
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (metadata instanceof JpegImageMetadata) {
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

			// Jpeg EXIF metadata is stored in a TIFF-based directory structure
			// and is identified with TIFF tags.
			// Here we look for the "x resolution" tag, but
			// we could just as easily search for any other tag.
			//
			// see the TiffConstants file for a list of TIFF tags.

			// simple interface to GPS data
			final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
			if (null != exifMetadata) {
				final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();

				if (null != gpsInfo) {
					point = new Point();
					// final String gpsDescription = gpsInfo.toString();
					final double longitude = gpsInfo
							.getLongitudeAsDegreesEast();
					final double latitude = gpsInfo.getLatitudeAsDegreesNorth();

					point.setGeoLat(latitude);
					point.setGeoLng(longitude);

				}
			}

			// get 海拔高度
			final TiffField altitudeField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
			if (null != altitudeField) {
				if (null == point) {
					point = new Point();
				}
				point.setGeoAlti(altitudeField.getDoubleValue());
			}

			photo.setGpsPoint(point);

		}
		return photo;
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
	public Photo save(String fileName, InputStream ins)
			throws ImageReadException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			IOUtils.copy(ins, baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// IOUtils.copy(new FileReader(image), baos);

		byte[] content = baos.toByteArray();

		InputStream is1 = new ByteArrayInputStream(content);

		Photo photo = new Photo();
		photo.setName(fileName);
		photo.setFileType(FilenameUtils.getExtension(fileName));

		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		User user = userManager.getUserByUsername(username);
		photo.setOwner(user);

		try {
			fillPhotoDetail(is1, photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		photoDao.save(photo);

		is1 = new ByteArrayInputStream(content);
		fileService.saveFile(FileService.TYPE_IMAGE, getName(photo), is1);

		return photo;
	}

	@Override
	public Response read(Long id) {
		Photo photo = photoDao.get(id);
		File file = fileService.readFile(FileService.TYPE_IMAGE, getName(photo));
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
				"attachment; filename=" + getName(photo));
		return response.build();

	}

	@Override
	public InputStream loadPhoto(Long id) {
		Photo photo = photoDao.get(id);
		File file = fileService.readFile(FileService.TYPE_IMAGE, getName(photo));
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			return fis;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
