package com.cnpanoramio.service.imaging;

import java.io.InputStream;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoDetails;

public class ImageInfoExtractorTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	private InputStream ins;
	private ImageInfoExtractor iier;
	
	@Before
	public void preMethodSetup() {
		
//		ins = getClass().getResourceAsStream("/image/IMAG2534.jpg");
//		ins = getClass().getResourceAsStream("/image/PANO_20140508_135045.jpg");
//		ins = getClass().getResourceAsStream("/image/PANO_20131004_142351.jpg");
//		ins = getClass().getResourceAsStream("/image/IMG_20140531_120150.jpg");
		ins = getClass().getResourceAsStream("/image/PANO_20140601_102907.jpg");
		iier = new ImageInfoExtractor(ins);
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testGetImageInfo() {
		
		Photo photo = iier.process();
		if(ObjectUtils.notEqual(null, photo)) {
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
		
		
	}
}
