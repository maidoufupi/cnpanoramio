package com.cnpanoramio.service.imaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.lbs.GPSFormat;

public class ImageInfoExtractor {

	protected transient final Log log = LogFactory.getLog(getClass());

	private SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	
	private Photo photo;
	private PhotoDetails detail;

	private ByteSourceInputStream bsIns;

	public ImageInfoExtractor(InputStream ins) {
		super();
		this.bsIns = new ByteSourceInputStream(ins, "");
		this.photo = new Photo();
		detail = new PhotoDetails();
		this.photo.setDetails(detail);
	}

	public ImageInfoExtractor(InputStream ins, Photo photo) {
		super();
		this.bsIns = new ByteSourceInputStream(ins, "");
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

		try {
			final ImageFormat imageFormat = Imaging.guessFormat(bsIns);
			if (imageFormat.equals(ImageFormats.JPEG)) {
				extraImageInfo(new JpegImageParser());
			} else if (imageFormat.equals(ImageFormats.PNG)) {
				extraImageInfo(new PngImageParser());
			} else if (imageFormat.equals(ImageFormats.TIFF)) {
				extraImageInfo(new TiffImageParser());
			} else if (imageFormat.equals(ImageFormats.BMP)) {
				extraImageInfo(new BmpImageParser());
			} else if (imageFormat.equals(ImageFormats.GIF)) {
				extraImageInfo(new GifImageParser());
			}
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bsIns.getInputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.photo;
	}

	public void extraImageInfo(ImageParser parser) {

//		try {
//			ImageInfo iInfo = parser.getImageInfo(bsIns);
//
//			log.debug("Parser Image Info start:");
//			log.debug(iInfo.getFormat());
//			photo.setFileType(iInfo.getFormat().toString());
//
//			log.debug(iInfo.getColorTypeDescription());
//			log.debug("Parser Image Info end!");
//
//		} catch (ImageReadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
//			Dimension size = parser.getImageSize(bsIns.getAll());
//
//			log.debug(size);
//		} catch (ImageReadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			extraExifInfo(parser);
		} catch (ImageReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			extraXmpInfo(parser);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void extraExifInfo(ImageParser parser) throws ImageReadException, IOException {
		
		PhotoDetails photoDetails = detail;
		Point point = null;
		
		IImageMetadata metadata = parser.getMetadata(bsIns);
		if (metadata instanceof JpegImageMetadata) {
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

//			List<IImageMetadataItem> items = jpegMetadata.getItems();
//			for (IImageMetadataItem item : items) {
//				log.debug(item.toString("PhotoDetail IImageMetadataItem: "));
//			}

			/* Origin */
			final TiffField dateTimeOriginalField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			if (null != dateTimeOriginalField) {
				log.debug(dateTimeOriginalField.getFieldTypeName());
				try {
					log.debug((String) dateTimeOriginalField
							.getFieldType().getValue(dateTimeOriginalField));
					log.debug(format.parse((String) dateTimeOriginalField
							.getFieldType().getValue(dateTimeOriginalField)));
					photoDetails.setDateTimeOriginal(format.parse((String) dateTimeOriginalField
							.getFieldType().getValue(dateTimeOriginalField)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			final TiffField dateTimeDigitizedField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
			if (null != dateTimeDigitizedField) {
				try {
					photoDetails
							.setDateTimeDigitized(format.parse((String) dateTimeDigitizedField
									.getFieldType()
									.getValue(dateTimeDigitizedField)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			final TiffField dateTimeField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME);
			if (null != dateTimeField) {
				try {
					photoDetails.setDateTime(format.parse((String) dateTimeField.getFieldType()
							.getValue(dateTimeField)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/* Image */
			TiffField imageWidthField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH);

			if (null == imageWidthField) {
				imageWidthField = jpegMetadata
						.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH);
			}

			if (null != imageWidthField) {
				photoDetails.setPixelXDimension(imageWidthField.getIntValue());
			}

			TiffField imageHeightField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH);
			if (null == imageHeightField) {
				imageHeightField = jpegMetadata
						.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
			}
			if (null != imageHeightField) {
				photoDetails.setPixelYDimension(imageHeightField.getIntValue());
			}

			photoDetails.setxResolution(getTagValue(jpegMetadata,
					TiffTagConstants.TIFF_TAG_XRESOLUTION));
			photoDetails.setyResolution(getTagValue(jpegMetadata,
					TiffTagConstants.TIFF_TAG_YRESOLUTION));
			photoDetails
					.setResolutionUnit(getTagValue(
							jpegMetadata,
							ExifTagConstants.EXIF_TAG_FOCAL_PLANE_RESOLUTION_UNIT_EXIF_IFD));
			photoDetails.setCompressedBitsPerPixel(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_COMPRESSED_BITS_PER_PIXEL));

			/* Camera */
			final TiffField makeField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_MAKE);
			if (null != makeField) {
				Object out = makeField.getFieldType().getValue(makeField);
				if (out instanceof String) {
					photoDetails.setMake((String) out);
				} else if (out instanceof String[]) {
					photoDetails.setMake(((String[]) out)[0]);
				}
			}

			final TiffField modelField = jpegMetadata
					.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_MODEL);
			if (null != modelField) {
				Object out = modelField.getFieldType().getValue(modelField);
				if (out instanceof String) {
					photoDetails.setModel((String) out);
				} else if (out instanceof String[]) {
					photoDetails.setModel(((String[]) out)[0]);
				}
			}

			final TiffField exposureTimeField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXPOSURE_TIME);
			if (null != exposureTimeField) {
				// log.info(exposureTimeField.getValueDescription());
				// BigDecimal b = new
				// BigDecimal(exposureTimeField.getDoubleValue());
				// b = b.setScale(4, BigDecimal.ROUND_FLOOR);
				photoDetails
						.setExposureTime(((RationalNumber) exposureTimeField
								.getFieldType().getValue(exposureTimeField))
								.doubleValue());
			}

			// FNumber
			final TiffField fnumberField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FNUMBER);
			if (null != fnumberField) {
				photoDetails.setFNumber(((RationalNumber) fnumberField
						.getFieldType().getValue(fnumberField)).doubleValue());
			}

			final TiffField exposureCompensationField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXPOSURE_COMPENSATION);
			if (null != exposureCompensationField) {
				// log.info(((RationalNumber)exposureCompensationField.getFieldType().getValue(exposureCompensationField)).doubleValue());
				photoDetails
						.setExposureBias(((RationalNumber) exposureCompensationField
								.getFieldType().getValue(
										exposureCompensationField))
								.doubleValue());
			}

			final TiffField flashField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FLASH);
			if (null != flashField) {
				photoDetails.setFlash((Short) flashField.getFieldType()
						.getValue(flashField));
			}

			final TiffField focalLengthField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FOCAL_LENGTH);
			if (null != focalLengthField) {
				photoDetails.setFocalLength(focalLengthField.getDoubleValue());
			}

			final TiffField maxApertureValueField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_MAX_APERTURE_VALUE);
			if (null != maxApertureValueField) {
				photoDetails.setMaxApertureValue(maxApertureValueField
						.getDoubleValue());
			}

			final TiffField meteringModeField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_METERING_MODE);
			if (null != meteringModeField) {
				photoDetails.setMeteringMode(meteringModeField
						.getValueDescription());
			}

			final TiffField focalLengthIn35mmFilmField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_FOCAL_LENGTH_IN_35MM_FORMAT);
			if (null != focalLengthIn35mmFilmField) {
				photoDetails
						.setFocalLengthIn35mmFilm(focalLengthIn35mmFilmField
								.getDoubleValue());
			}

			/* Advanced photo */
			photoDetails.setContrast(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_CONTRAST_1));

			final TiffField brightnessValueField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
			if (null != brightnessValueField) {
				photoDetails.setBrightnessValue(brightnessValueField
						.getDoubleValue());
			}

			photoDetails.setLightSource(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_LIGHT_SOURCE));
			photoDetails.setExposureProgram(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_EXPOSURE_PROGRAM));
			photoDetails.setSaturation(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_SATURATION_1));
			photoDetails.setSharpness(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_SHARPNESS_1));
			photoDetails.setWhiteBalance(getTagValue(jpegMetadata,
					ExifTagConstants.EXIF_TAG_WHITE_BALANCE_1));

			final TiffField digitalZoomRatioField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DIGITAL_ZOOM_RATIO);
			if (null != digitalZoomRatioField) {
				photoDetails.setDigitalZoomRatio(digitalZoomRatioField
						.getIntValue());
			}

			final TiffField exifVersionField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_EXIF_VERSION);
			if (null != exifVersionField) {
				photoDetails.setExifVersion(new String(exifVersionField
						.getByteArrayValue()));
			}

			// ISO
			final TiffField isoField = jpegMetadata
					.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_ISO);
			if (null != isoField) {
				photoDetails.setISO(isoField.getValueDescription());
			}

			/* GPS */
			// GPSInfo
			final TiffField GPSInfoField = jpegMetadata
					.findEXIFValue(ExifTagConstants.EXIF_TAG_GPSINFO);
			if (null != GPSInfoField) {
				photoDetails.setGPSInfo(GPSInfoField.getValueDescription());
			}
			// GPSDateStamp
			final TiffField GPSDateStampField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_DATE_STAMP);
			if (null != GPSDateStampField) {
				try {
					Date date = DateUtils.parseDate((String) GPSDateStampField
							.getFieldType().getValue(GPSDateStampField),
							new String[] { "yyyy:MM:dd" });
					photoDetails.setGPSDateStamp(date);
				} catch (DateParseException e) {
					// TODO Auto-generated catch block
					log.debug(e.getMessage());
				}
			}

			// GPSTimeStamp
			final TiffField GPSTimeStampField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_TIME_STAMP);
			if (null != GPSTimeStampField) {
				// TODO
			}
			final TiffField GPSProcessingMethodField = jpegMetadata
					.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_PROCESSING_METHOD);
			if (null != GPSProcessingMethodField) {
				Object out = GPSProcessingMethodField.getFieldType().getValue(
						GPSProcessingMethodField);
				if (out instanceof String[]) {
					photoDetails.setGPSProcessingMethod(((String[]) out)[0]);
				}
			}

			/********************************************************************************************/

			// Jpeg EXIF metadata is stored in a TIFF-based directory structure
			// and is identified with TIFF tags.
			// Here we look for the "x resolution" tag, but
			// we could just as easily search for any other tag.
			//
			// see the TiffConstants file for a list of TIFF tags.

			// simple interface to GPS data
			final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
			if (null != exifMetadata) {

				try {
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
				}catch(ClassCastException ex) {
					log.warn("photo " + photo.getId() + " getGPS() Exception: " + ex.getMessage());
					final TiffField latField = jpegMetadata
							.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
					final TiffField latRefField = jpegMetadata
							.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
					if (null != latField) {
						photoDetails.setGPSLatitudeRef(latRefField.getValueDescription());
						log.debug(GPSFormat.convert(latField.getStringValue()));
						photoDetails.setGPSLatitude(GPSFormat.convert(latField.getStringValue()));
					}
					
					final TiffField lngField = jpegMetadata
							.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
					final TiffField lngRefField = jpegMetadata
							.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
					if (null != latField) {
						photoDetails.setGPSLongitudeRef(lngRefField.getValueDescription());
						log.debug(GPSFormat.convert(lngField.getStringValue()));
						photoDetails.setGPSLongitude(GPSFormat.convert(lngField.getStringValue()));
					}
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
				if (null != altitudeRefField) {
					photoDetails.setGPSAltitudeRef(altitudeRefField
							.getValueDescription());
				}
				photoDetails.setGPSAltitude(altitudeField.getDoubleValue());
			}

			photo.setGpsPoint(point);
		}
	}
	
	private static String getTagValue(final JpegImageMetadata jpegMetadata,
			final TagInfo tagInfo) {
		final TiffField field = jpegMetadata
				.findEXIFValueWithExactMatch(tagInfo);
		if (field == null) {
			return "";
		} else {
			return field.getValueDescription();
		}
	}

	public void extraXmpInfo(ImageParser parser) throws Exception {

		String xmpStr = null;

		xmpStr = parser.getXmpXml(bsIns, null);

		log.debug(xmpStr);
		if (null == xmpStr) {
			return;
		}

		XPath xpath = XPathFactory.newInstance().newXPath();

		String splitor = "";
		Node node = (Node) xpath
				.evaluate(
						"//*[local-name()='Description']/*[local-name()='UsePanoramaViewer']",
						new InputSource(new StringReader(xmpStr)),
						XPathConstants.NODE);
		if (null != node) {
			detail.setgPanoUsePanoramaViewer(node.getTextContent());
		} else {
			node = (Node) xpath
					.evaluate(
							"//*[local-name()='Description']/@*[local-name()='UsePanoramaViewer']",
							new InputSource(new StringReader(xmpStr)),
							XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoUsePanoramaViewer(node.getTextContent());
				splitor = "@";
			}
		}
		if (null != node) {
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='CaptureSoftware']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoCaptureSoftware(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='StitchingSoftware']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoStitchingSoftware(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='UsePanoramaViewer']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoUsePanoramaViewer(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='ProjectionType']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoProjectionType(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='CroppedAreaLeftPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoCroppedAreaLeftPixels(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='CroppedAreaTopPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoCroppedAreaTopPixels(node.getTextContent());
			}
			node = (Node) xpath.evaluate(
					"//*[local-name()='Description']/" + splitor
							+ "*[local-name()='CroppedAreaImageWidthPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoCroppedAreaImageWidthPixels(node
						.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor
					+ "*[local-name()='CroppedAreaImageHeightPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoCroppedAreaImageHeightPixels(node
						.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='FullPanoWidthPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoFullPanoWidthPixels(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='FullPanoHeightPixels']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoFullPanoHeightPixels(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor + "*[local-name()='NorthPosInX']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoNorthPosInX(node.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor
					+ "*[local-name()='LargestValidInteriorRectLeft']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoLargestValidInteriorRectLeft(node
						.getTextContent());
			}
			node = (Node) xpath.evaluate(
					"//*[local-name()='Description']/" + splitor
							+ "*[local-name()='LargestValidInteriorRectTop']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoLargestValidInteriorRectTop(node
						.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor
					+ "*[local-name()='LargestValidInteriorRectWidth']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoLargestValidInteriorRectWidth(node
						.getTextContent());
			}
			node = (Node) xpath.evaluate("//*[local-name()='Description']/"
					+ splitor
					+ "*[local-name()='LargestValidInteriorRectHeight']",
					new InputSource(new StringReader(xmpStr)),
					XPathConstants.NODE);
			if (null != node) {
				detail.setgPanoLargestValidInteriorRectHeight(node
						.getTextContent());
			}
		}
	}

	public Photo getPhoto() {
		return photo;
	}

}
