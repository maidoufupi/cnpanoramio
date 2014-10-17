package com.cnpanoramio.service.imaging;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.lbs.GPSFormat;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.xmp.XmpDirectory;

public class ImageInfoExtractorTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	private InputStream ins;
	private ImageMetadataExtractor iier;

	@Before
	public void preMethodSetup() {

		// ins = getClass().getResourceAsStream("/image/IMAG2534.jpg");
		// ins =
		// getClass().getResourceAsStream("/image/PANO_20140508_135045.jpg");
//		ins = getClass().getResourceAsStream("/image/PANO_20131004_142351.jpg");
		ins = getClass().getResourceAsStream("/image/281.jpg");
		// ins =
		// getClass().getResourceAsStream("/image/IMG_20140531_120150.jpg");
		// ins =
		// getClass().getResourceAsStream("/image/PANO_20140601_102907.jpg");
		// ins = getClass().getResourceAsStream("/image/A.jpg");
		// ins = getClass().getResourceAsStream("/image/IMG_3264.JPG");
		// ins = getClass().getResourceAsStream("/image/17061_1314362667.jpg");
		// ins =
		// getClass().getResourceAsStream("/image/IMG_20141004_160248.jpg");
		 iier = new ImageMetadataExtractor(ins);
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testGetImageInfo() {

		Photo photo = iier.process();
		if (ObjectUtils.notEqual(null, photo)) {
			log.info("photo is not null");
		}

		PhotoDetails detail = photo.getDetails();
		log.info(photo.getFileType());
		log.info(photo.isIs360());
		log.info(detail.getgPanoCaptureSoftware());
		log.info(detail.getgPanoUsePanoramaViewer());
		log.info(detail.getgPanoFullPanoHeightPixels());

		log.info(detail.getDateTime());
		log.info(detail.getGPSLatitude());
		log.info(detail.getGPSLongitude());

		log.info(detail.getDateTime());
		log.info(detail.getDateTimeOriginal());
		log.info(detail.getDateTimeDigitized());

		log.info(detail.getPixelXDimension());
		log.info(detail.getPixelYDimension());
		log.info(detail.getOrientation());

	}
	
	@Test
	public void testJpgMetadataExtractor() {
		Photo photo = iier.process();
		displayPhotoDetails(photo);
	}

	@Test
	public void testMetadataExtractor() throws ImageProcessingException,
			IOException {
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(
					new BufferedInputStream(ins), true);
			extract(metadata);
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayPhotoDetails(Photo photo) {
		PhotoDetails detail = photo.getDetails();
		log.info("Date/Time : " + detail.getDateTime());
		log.info("Date/Time Original : " + detail.getDateTimeOriginal());
		log.info("Date/Time Digitized : " + detail.getDateTimeDigitized());
		log.info("X Resolution : " + detail.getxResolution());
		log.info("Y Resolution : " + detail.getyResolution());
		log.info("Resolution Unit : " + detail.getResolutionUnit());
		log.info("Compressed Bits Per Pixel : "
				+ detail.getCompressedBitsPerPixel());
		log.info("Make : " + detail.getMake());
		log.info("Model : " + detail.getModel());
		log.info("Orientation : " + detail.getOrientation());
		log.info("Exposure Time : " + detail.getExposureTime());
		log.info("光圈 F-Number : " + detail.getFNumber());
		log.info("曝光程序 Exposure Program : " + detail.getExposureProgram());
		log.info("Exposure Bias Value : " + detail.getExposureBias());
		log.info("Flash : " + detail.getFlash());
		log.info("Focal Length : " + detail.getFocalLength());
		log.info("Max Aperture Value : " + detail.getMaxApertureValue());
		log.info("测光模式 Metering Mode : " + detail.getMeteringMode());
		log.info("35mm等效焦距 Focal Length 35 : "
				+ detail.getFocalLengthIn35mmFilm());
		log.info("对比度 Contrast : " + detail.getContrast());
		log.info("亮度 Brightness Value : " + detail.getBrightnessValue());
		log.info("锐化 Sharpness : " + detail.getSharpness());
		log.info("光源 Light Source : " + detail.getLightSource());
		log.info("饱和度 Saturation : " + detail.getSaturation());
		log.info("白平衡 White Balance : " + detail.getWhiteBalance());
		log.info("数字变焦比率 Digital Zoom Ratio : " + detail.getDigitalZoomRatio());
		log.info("Exif Version : " + detail.getExifVersion());
		log.info("ISO : " + detail.getISO());
		log.info("GPSinfo : " + detail.getGPSInfo());
		log.info("GPS Date Stamp : " + detail.getGPSDateStamp());
		log.info("GPS Time Stamp : " + detail.getGPSTimeStamp());
		log.info("GPS Lat : " + detail.getGPSLatitude());
		log.info("GPS Lat Ref : " + detail.getGPSLatitudeRef());
		log.info("GPS Lng : " + detail.getGPSLongitude());
		log.info("GPS Lng Ref : " + detail.getGPSLongitudeRef());
		log.info("GPS Alt : " + detail.getGPSAltitude());
		log.info("GPS Alt Ref : " + detail.getGPSAltitudeRef());
		log.info("Width : " + detail.getPixelXDimension());
		log.info("Height : " + detail.getPixelYDimension());
		
		log.info("PanoramaViewer : " + detail.getgPanoUsePanoramaViewer());
	}
	
	public Photo extract(Metadata metadata) throws MetadataException {
		Photo photo = new Photo();
		PhotoDetails detail = new PhotoDetails();

		for (Directory directory : metadata.getDirectories()) {

			log.info(directory.getName());

			if (directory.getName().equalsIgnoreCase("Jpeg")) {
				for (Tag tag : directory.getTags()) {
					switch (tag.getTagType()) {
					case JpegDirectory.TAG_JPEG_IMAGE_WIDTH:
						detail.setPixelXDimension(directory
								.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH));
						break;
					case JpegDirectory.TAG_JPEG_IMAGE_HEIGHT:
						detail.setPixelYDimension(directory
								.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT));
						break;
					}
				}
			} else if (directory.getName().equalsIgnoreCase("GPS")) {
				GpsDirectory gpsDir = (GpsDirectory) directory;
				for (Tag tag : directory.getTags()) {
					switch (tag.getTagType()) {
					// gps
					case GpsDirectory.TAG_GPS_VERSION_ID:
						detail.setGPSInfo(directory
								.getString(GpsDirectory.TAG_GPS_VERSION_ID));
						break;
					case GpsDirectory.TAG_GPS_DATE_STAMP:
						detail.setGPSDateStamp(directory
								.getDate(GpsDirectory.TAG_GPS_DATE_STAMP));
						break;
					case GpsDirectory.TAG_GPS_TIME_STAMP:
						Rational[] ra = directory
								.getRationalArray(GpsDirectory.TAG_GPS_TIME_STAMP);
						if (null != ra) {
							detail.setGPSTimeStamp(ra[0].doubleValue() * 60
									* 60 + ra[1].doubleValue() * 60
									+ ra[2].doubleValue());
						}
						break;
					case GpsDirectory.TAG_GPS_LATITUDE:
						detail.setGPSLatitude(gpsDir.getGeoLocation()
								.getLatitude());
						break;
					case GpsDirectory.TAG_GPS_LATITUDE_REF:
						detail.setGPSLatitudeRef(tag.getDescription());
						break;
					case GpsDirectory.TAG_GPS_LONGITUDE:
						detail.setGPSLongitude(gpsDir.getGeoLocation()
								.getLongitude());
						break;
					case GpsDirectory.TAG_GPS_LONGITUDE_REF:
						detail.setGPSLongitudeRef(tag.getDescription());
						break;
					case GpsDirectory.TAG_GPS_ALTITUDE:
						detail.setGPSAltitude(directory
								.getDouble(GpsDirectory.TAG_GPS_ALTITUDE));
						break;
					case GpsDirectory.TAG_GPS_ALTITUDE_REF:
						detail.setGPSAltitudeRef(tag.getDescription());
						break;
					}
				}
			} else {

				for (Tag tag : directory.getTags()) {
					String tagName = tag.getTagName();
					String desc = tag.getDescription();

					log.info(tag.getDirectoryName() + " / " + tagName + " / "
							+ tag.getTagType() + " = " + desc);

					switch (tag.getTagType()) {
					/* Origin */
					case ExifIFD0Directory.TAG_DATETIME:
						detail.setDateTime(directory
								.getDate(ExifIFD0Directory.TAG_DATETIME));
						break;
					case ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL:
						detail.setDateTimeOriginal(directory
								.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
						break;
					case ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED:
						detail.setDateTimeDigitized(directory
								.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED));
						break;
					/* Image */
					case ExifIFD0Directory.TAG_X_RESOLUTION:
						detail.setxResolution(directory
								.getString(ExifIFD0Directory.TAG_X_RESOLUTION));
						break;
					case ExifIFD0Directory.TAG_Y_RESOLUTION:
						detail.setyResolution(directory
								.getString(ExifIFD0Directory.TAG_Y_RESOLUTION));
						break;
					case ExifIFD0Directory.TAG_RESOLUTION_UNIT:
						detail.setResolutionUnit(tag.getDescription());
						break;
					case ExifSubIFDDirectory.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL:
						detail.setCompressedBitsPerPixel(directory
								.getString(ExifSubIFDDirectory.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL));
						break;
					/* Camera */
					case ExifIFD0Directory.TAG_MAKE:
						detail.setMake(directory
								.getString(ExifIFD0Directory.TAG_MAKE));
						break;
					case ExifIFD0Directory.TAG_MODEL:
						detail.setModel(directory
								.getString(ExifIFD0Directory.TAG_MODEL));
						break;
					case ExifIFD0Directory.TAG_ORIENTATION:
						detail.setOrientation(directory.getRational(
								ExifIFD0Directory.TAG_ORIENTATION).shortValue());
						break;
					case ExifSubIFDDirectory.TAG_EXPOSURE_TIME:
						detail.setExposureTime(directory
								.getDouble(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
						break;
					case ExifSubIFDDirectory.TAG_FNUMBER:
						detail.setFNumber(directory
								.getDouble(ExifSubIFDDirectory.TAG_FNUMBER));
						break;
					case ExifSubIFDDirectory.TAG_EXPOSURE_PROGRAM:
						detail.setExposureProgram(tag.getDescription());
						break;
					case ExifSubIFDDirectory.TAG_EXPOSURE_BIAS:
						detail.setExposureBias(directory
								.getDouble(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS));
						break;
					case ExifSubIFDDirectory.TAG_FLASH:
						detail.setFlash(directory.getRational(
								ExifSubIFDDirectory.TAG_FLASH).shortValue());
						break;
					case ExifSubIFDDirectory.TAG_FOCAL_LENGTH:
						detail.setFocalLength(directory
								.getDouble(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
						break;
					case ExifSubIFDDirectory.TAG_MAX_APERTURE:
						detail.setMaxApertureValue(directory
								.getDouble(ExifSubIFDDirectory.TAG_MAX_APERTURE));
						break;
					case ExifSubIFDDirectory.TAG_METERING_MODE:
						detail.setMeteringMode(tag.getDescription());
						break;
					case ExifSubIFDDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH:
						detail.setFocalLengthIn35mmFilm(directory
								.getDouble(ExifSubIFDDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH));
						break;
					case ExifSubIFDDirectory.TAG_CONTRAST:
						detail.setContrast(directory
								.getString(ExifSubIFDDirectory.TAG_CONTRAST));
						break;
					case ExifSubIFDDirectory.TAG_BRIGHTNESS_VALUE:
						detail.setBrightnessValue(directory
								.getDouble(ExifSubIFDDirectory.TAG_BRIGHTNESS_VALUE));
						break;
					/* advance */
					case ExifSubIFDDirectory.TAG_SHARPNESS:
						detail.setSharpness(directory
								.getString(ExifSubIFDDirectory.TAG_SHARPNESS));
						break;
					case ExifSubIFDDirectory.TAG_SATURATION:
						detail.setSaturation(directory
								.getString(ExifSubIFDDirectory.TAG_SATURATION));
						break;
					case ExifSubIFDDirectory.TAG_WHITE_BALANCE:
						detail.setWhiteBalance(directory
								.getString(ExifSubIFDDirectory.TAG_WHITE_BALANCE));
						detail.setLightSource(directory
								.getString(ExifSubIFDDirectory.TAG_LIGHT_SOURCE));
						break;
					case ExifSubIFDDirectory.TAG_DIGITAL_ZOOM_RATIO:
						detail.setDigitalZoomRatio(directory
								.getInteger(ExifSubIFDDirectory.TAG_DIGITAL_ZOOM_RATIO));
						break;
					case ExifSubIFDDirectory.TAG_EXIF_VERSION:
						detail.setExifVersion(tag.getDescription());
						break;
					case ExifSubIFDDirectory.TAG_ISO_EQUIVALENT:
						detail.setISO(directory
								.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
						break;

					}

				}
			}

			for (String error : directory.getErrors()) {
				System.err.println("ERROR: " + error);
			}
		}

		try {
			XmpDirectory xmpDirectory = metadata
					.getDirectory(XmpDirectory.class);
			XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
			XMPIterator itr = xmpMeta.iterator();
			while (itr.hasNext()) {
				XMPPropertyInfo pi = (XMPPropertyInfo) itr.next();
				if (pi != null && pi.getPath() != null) {
					log.info("xml " + pi.getPath() + " / " + pi.getValue());
					String path = pi.getPath();
					if (path.equalsIgnoreCase("GPano:UsePanoramaViewer")) {
						detail.setgPanoUsePanoramaViewer(pi.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:CroppedAreaImageHeightPixels")) {
						detail.setgPanoCroppedAreaImageHeightPixels(pi
								.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:CroppedAreaImageWidthPixels")) {
						detail.setgPanoCroppedAreaImageWidthPixels(pi
								.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:CroppedAreaLeftPixels")) {
						detail.setgPanoCroppedAreaLeftPixels(pi.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:CroppedAreaTopPixels")) {
						detail.setgPanoCroppedAreaTopPixels(pi.getValue());
					}

					else if (path
							.equalsIgnoreCase("GPano:FullPanoHeightPixels")) {
						detail.setgPanoFullPanoHeightPixels(pi.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:FullPanoWidthPixels")) {
						detail.setgPanoFullPanoWidthPixels(pi.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:LargestValidInteriorRectHeight")) {
						detail.setgPanoLargestValidInteriorRectHeight(pi
								.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:LargestValidInteriorRectLeft")) {
						detail.setgPanoLargestValidInteriorRectLeft(pi
								.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:LargestValidInteriorRectTop")) {
						detail.setgPanoLargestValidInteriorRectTop(pi
								.getValue());
					} else if (path
							.equalsIgnoreCase("GPano:LargestValidInteriorRectWidth")) {
						detail.setgPanoLargestValidInteriorRectWidth(pi
								.getValue());
					} else if (path.equalsIgnoreCase("GPano:ProjectionType")) {
						detail.setgPanoProjectionType(pi.getValue());
					}
				}
			}
		} catch (final NullPointerException npe) {
			// ignore
		} catch (XMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("Date/Time : " + detail.getDateTime());
		log.info("Date/Time Original : " + detail.getDateTimeOriginal());
		log.info("Date/Time Digitized : " + detail.getDateTimeDigitized());
		log.info("X Resolution : " + detail.getxResolution());
		log.info("Y Resolution : " + detail.getyResolution());
		log.info("Resolution Unit : " + detail.getResolutionUnit());
		log.info("Compressed Bits Per Pixel : "
				+ detail.getCompressedBitsPerPixel());
		log.info("Make : " + detail.getMake());
		log.info("Model : " + detail.getModel());
		log.info("Orientation : " + detail.getOrientation());
		log.info("Exposure Time : " + detail.getExposureTime());
		log.info("光圈 F-Number : " + detail.getFNumber());
		log.info("曝光程序 Exposure Program : " + detail.getExposureProgram());
		log.info("Exposure Bias Value : " + detail.getExposureBias());
		log.info("Flash : " + detail.getFlash());
		log.info("Focal Length : " + detail.getFocalLength());
		log.info("Max Aperture Value : " + detail.getMaxApertureValue());
		log.info("测光模式 Metering Mode : " + detail.getMeteringMode());
		log.info("35mm等效焦距 Focal Length 35 : "
				+ detail.getFocalLengthIn35mmFilm());
		log.info("对比度 Contrast : " + detail.getContrast());
		log.info("亮度 Brightness Value : " + detail.getBrightnessValue());
		log.info("锐化 Sharpness : " + detail.getSharpness());
		log.info("光源 Light Source : " + detail.getLightSource());
		log.info("饱和度 Saturation : " + detail.getSaturation());
		log.info("白平衡 White Balance : " + detail.getWhiteBalance());
		log.info("数字变焦比率 Digital Zoom Ratio : " + detail.getDigitalZoomRatio());
		log.info("Exif Version : " + detail.getExifVersion());
		log.info("ISO : " + detail.getISO());
		log.info("GPSinfo : " + detail.getGPSInfo());
		log.info("GPS Date Stamp : " + detail.getGPSDateStamp());
		log.info("GPS Time Stamp : " + detail.getGPSTimeStamp());
		log.info("GPS Lat : " + detail.getGPSLatitude());
		log.info("GPS Lat Ref : " + detail.getGPSLatitudeRef());
		log.info("GPS Lng : " + detail.getGPSLongitude());
		log.info("GPS Lng Ref : " + detail.getGPSLongitudeRef());
		log.info("GPS Alt : " + detail.getGPSAltitude());
		log.info("GPS Alt Ref : " + detail.getGPSAltitudeRef());
		log.info("Width : " + detail.getPixelXDimension());
		log.info("Height : " + detail.getPixelYDimension());
		
		log.info("PanoramaViewer : " + detail.getgPanoUsePanoramaViewer());

		return photo;
	}
}
