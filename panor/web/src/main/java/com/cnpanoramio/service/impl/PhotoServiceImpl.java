package com.cnpanoramio.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.IImageMetadata.IImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
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
import com.cnpanoramio.domain.PhotoDetails;
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
// 返回json时从Photo里去掉PhotoDetials
        photo.setDetails(null);
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
		PhotoDetails photoDetails = new PhotoDetails();
		
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
			
/* Origin */
			final TiffField dateTimeOriginalField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			if(null != dateTimeOriginalField) {
				photoDetails.setDateTimeOriginal(dateTimeOriginalField.getValueDescription());
			}
			
			final TiffField dateTimeDigitizedField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
			if(null != dateTimeDigitizedField) {
				photoDetails.setDateTimeDigitized(dateTimeDigitizedField.getValueDescription());
			}
			
			final TiffField dateTimeField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME);
			if(null != dateTimeField) {
				photoDetails.setDateTime(dateTimeField.getValueDescription());
			}
			
/* Image */		
			photoDetails.setPixelXDimension(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH));
			photoDetails.setPixelYDimension(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH));
			photoDetails.setxResolution(getTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_XRESOLUTION));
			photoDetails.setyResolution(getTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_YRESOLUTION));
			photoDetails.setResolutionUnit(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_FOCAL_PLANE_RESOLUTION_UNIT_EXIF_IFD));
			photoDetails.setCompressedBitsPerPixel(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_COMPRESSED_BITS_PER_PIXEL));
			
/* Camera */
			final TiffField makeField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_MAKE);
			if(null != makeField) {
				photoDetails.setMake(makeField.getValueDescription());
			}
			
			final TiffField modelField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_MODEL);
			if(null != modelField) {
				photoDetails.setModel(modelField.getValueDescription());
			}
			
			final TiffField exposureTimeField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXPOSURE_TIME);
			if(null != exposureTimeField) {
				BigDecimal b = new BigDecimal(exposureTimeField.getDoubleValue());  
			    b = b.setScale(4, BigDecimal.ROUND_FLOOR);  
				photoDetails.setExposureTime(String.valueOf(b) + " sec");
			}
			
			final TiffField fnumberField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FNUMBER);
			if(null != fnumberField) {
				BigDecimal b = new BigDecimal(fnumberField.getDoubleValue());  
			    b = b.setScale(1, BigDecimal.ROUND_FLOOR);  
				photoDetails.setFNumber(b.doubleValue());
			}
//			photoDetails.setExposureBias();
			final TiffField focalLengthField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FOCAL_LENGTH);
			if(null != focalLengthField) {
				photoDetails.setFocalLength(focalLengthField.getDoubleValue());
			}
			
			final TiffField maxApertureValueField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_MAX_APERTURE_VALUE);
			if(null != maxApertureValueField) {
				photoDetails.setMaxApertureValue(maxApertureValueField.getDoubleValue());
			}
			
			final TiffField meteringModeField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_METERING_MODE);
			if(null != meteringModeField) {
				photoDetails.setMeteringMode(meteringModeField.getValueDescription());
			}
			
			final TiffField focalLengthIn35mmFilmField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FOCAL_LENGTH_IN_35MM_FORMAT);
			if(null != focalLengthIn35mmFilmField) {
				photoDetails.setFocalLengthIn35mmFilm(focalLengthIn35mmFilmField.getDoubleValue());
			}
			
/* Advanced photo */
			photoDetails.setContrast(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_CONTRAST_1));
			
			final TiffField brightnessValueField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
			if(null != brightnessValueField) {
				photoDetails.setBrightnessValue(brightnessValueField.getDoubleValue());
			}		
			
			photoDetails.setLightSource(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_LIGHT_SOURCE));
			photoDetails.setExposureProgram(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_EXPOSURE_PROGRAM));
			photoDetails.setSaturation(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_SATURATION_1));
			photoDetails.setSharpness(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_SHARPNESS_1));
			photoDetails.setWhiteBalance(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_WHITE_BALANCE_1));
			photoDetails.setDigitalZoomRatio(Integer.valueOf(getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DIGITAL_ZOOM_RATIO)));
			
			final TiffField exifVersionField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXIF_VERSION);
			if(null != exifVersionField) {
				photoDetails.setExifVersion(new String(exifVersionField.getByteArrayValue()));
			}
			
			// ISO
			final TiffField isoField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_ISO);
			if(null != isoField) {
				photoDetails.setISO(isoField.getValueDescription());
			}

/* GPS */
		
/********************************************************************************************/					
//			getTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_XRESOLUTION);
//            getTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_DATE_TIME);
//            getTagValue(jpegMetadata,
//                    ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
//            getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
//            getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_ISO);
//            getTagValue(jpegMetadata,
//                    ExifTagConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
//            getTagValue(jpegMetadata,
//                    ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
//            getTagValue(jpegMetadata,
//                    ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
//            getTagValue(jpegMetadata,
//                    GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
//            getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE);
//            getTagValue(jpegMetadata,
//                    GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
//            getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
	
//			List<IImageMetadataItem> items = jpegMetadata.getItems();
//			for(IImageMetadataItem item : items) {
//				log.info(item.toString("IImageMetadataItem: "));
//			}
/********************************************************************************************/			

			// Jpeg EXIF metadata is stored in a TIFF-based directory structure
			// and is identified with TIFF tags.
			// Here we look for the "x resolution" tag, but
			// we could just as easily search for any other tag.
			//
			// see the TiffConstants file for a list of TIFF tags.

			// simple interface to GPS data
			final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
/********************************************************************************************/				
//			List<TiffField> fields = exifMetadata.getAllFields();
//			for(TiffField field : fields) {
//				log.info(field.getTagName() + " : " + field.getTag());
//				String tagName = field.getTagName();
//				if(tagName == "ExifVersion") {
////					exifMetadata.getFieldValue((TagInfoLong) field.getTagInfo());
//				}
//				log.info(exifMetadata.getFieldValue(field.getTagInfo()));
//				log.info("TiffField: " + field.getDescriptionWithoutValue() + " = " + field.getValueDescription());
//			}
/********************************************************************************************/	
			if (null != exifMetadata) {
				final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();

				if (null != gpsInfo) {
					point = new Point();
					// final String gpsDescription = gpsInfo.toString();
					final double longitude = gpsInfo
							.getLongitudeAsDegreesEast();
					final double latitude = gpsInfo.getLatitudeAsDegreesNorth();

					point.setLat(latitude);
					point.setLng(longitude);
					
					photoDetails.setGPSLatitudeRef(gpsInfo.latitudeRef);
					photoDetails.setGPSLatitude(latitude);
					photoDetails.setGPSLongitudeRef(gpsInfo.longitudeRef);
					photoDetails.setGPSLongitude(longitude);
				}
			}

			// get 海拔高度
			final TiffField altitudeField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
			final TiffField altitudeRefField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_ALTITUDE_REF);
			if (null != altitudeField) {
				if (null == point) {
					point = new Point();
				}
				point.setAlt(altitudeField.getDoubleValue());
				if(null != altitudeRefField) {
					photoDetails.setGPSAltitudeRef(altitudeRefField.getValueDescription());
				}
				photoDetails.setGPSAltitude(altitudeField.getDoubleValue());
			}

			photo.setGpsPoint(point);
		}
		photoDetails.setPhoto(photo);
		photo.setDetails(photoDetails);
		return photo;
	}
	
	private static String getTagValue(final JpegImageMetadata jpegMetadata,
            final TagInfo tagInfo) {
        final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
        if (field == null) {
            return "";
        } else {
            return field.getValueDescription();
        }
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
		photo.setCreateDate(new Date());
		
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
