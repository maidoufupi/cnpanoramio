package com.cnpanoramio.service.imaging;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
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

public class ImageMetadataExtractor {

	private Photo photo;
	private PhotoDetails detail;
	private InputStream ins;

	public ImageMetadataExtractor(InputStream ins) {
		super();
		this.ins = ins;
		this.photo = new Photo();
		detail = new PhotoDetails();
		this.photo.setDetails(detail);
	}

	public ImageMetadataExtractor(InputStream ins, Photo photo) {
		super();
		this.ins = ins;
		this.photo = photo;
		if (null != this.photo.getDetails()) {
			detail = this.photo.getDetails();
		} else {
			detail = new PhotoDetails();
			this.photo.setDetails(detail);
			detail.setPhoto(photo);
		}
	}

	public Photo process() {
		Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(
					new BufferedInputStream(ins), true);
			extraImageInfo(metadata);
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 不在此处转换，detail只存储原始信息，在输出photo信息时进行转换
//		if(detail.getOrientation() == 5 ||
//				detail.getOrientation() == 6 ||
//				detail.getOrientation() == 7 ||
//				detail.getOrientation() == 8) {
//			// 根据图片附加信息进行大小旋转
//			Integer width = detail.getPixelXDimension();
//			detail.setPixelXDimension(detail.getPixelYDimension());
//			detail.setPixelYDimension(width);
//		}

		return this.photo;
	}

	public void extraImageInfo(Metadata metadata) throws MetadataException {

		for (Directory directory : metadata.getDirectories()) {

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
			} else if (directory.getName().equalsIgnoreCase("Xmp")) {
				try {
					XmpDirectory xmpDirectory = metadata
							.getDirectory(XmpDirectory.class);
					XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
					XMPIterator itr = xmpMeta.iterator();
					while (itr.hasNext()) {
						XMPPropertyInfo pi = (XMPPropertyInfo) itr.next();
						if (pi != null && pi.getPath() != null) {
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
								detail.setgPanoCroppedAreaLeftPixels(pi
										.getValue());
							} else if (path
									.equalsIgnoreCase("GPano:CroppedAreaTopPixels")) {
								detail.setgPanoCroppedAreaTopPixels(pi
										.getValue());
							}

							else if (path
									.equalsIgnoreCase("GPano:FullPanoHeightPixels")) {
								detail.setgPanoFullPanoHeightPixels(pi
										.getValue());
							} else if (path
									.equalsIgnoreCase("GPano:FullPanoWidthPixels")) {
								detail.setgPanoFullPanoWidthPixels(pi
										.getValue());
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
							} else if (path
									.equalsIgnoreCase("GPano:ProjectionType")) {
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
			} else {

				for (Tag tag : directory.getTags()) {

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
		}
	}

}
