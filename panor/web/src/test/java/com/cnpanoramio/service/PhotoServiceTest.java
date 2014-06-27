package com.cnpanoramio.service;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.IImageMetadata.IImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.cnpanoramio.domain.Photo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = true,
        locations = { 
				"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-service.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "classpath*:/applicationContext-test.xml",
                "/WEB-INF/spring-security.xml",
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"})
public class PhotoServiceTest {
	
	protected transient final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private PhotoManager photoService;

	private InputStream ins;
	URL url;
	private Collection<Photo> photosForUser;

	@Before
	public void preMethodSetup() {
//		url = getClass().getResource("/image/IMAG1340.jpg");
		
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testPhotoStore() throws IOException, ImageReadException {
//		photoService.store(getMultipartBody());
	}

	@Test
	public void testFillPhotoDetail() throws ImageReadException, IOException {
//		ins = getClass().getResourceAsStream("/image/photo.jpg");
//		ins = getClass().getResourceAsStream("/image/IMAG2290.jpg");
//		ins = getClass().getResourceAsStream("/image/IMAG2534.jpg");
		ins = getClass().getResourceAsStream("/image/IMG_20140531_120150.jpg");
		
		Photo photo = new Photo();
//		photoService.fillPhotoDetail(ins, photo);
		log.info(photo.getDetails().getExifVersion());
		log.info(photo.getDetails().getMeteringMode());
		log.info(photo.getDetails().getMake());
		log.info(photo.getDetails().getModel());
		log.info(photo.getDetails().getExposureTime());
		log.info(photo.getDetails().getFocalLength());
		log.info(photo.getDetails().getFNumber());
		log.info(photo.getDetails().getISO());
		log.info(photo.getDetails().getPixelXDimension());
		log.info(photo.getDetails().getPixelYDimension());
				
		log.info(photo.getGpsPoint().getLat());
//		log.info(photo.getGpsPoint().getLng());
		log.info(photo.getGpsPoint().getAlt());
//		Assert.notNull(photo.getGpsPoint());
//		Assert.notNull(photo.getGpsPoint().getAlt());
		
		log.info(photo.getDetails().getDateTime());
	}
	
	@Test
	public void testNoDetails() throws ImageReadException, IOException {
		ins = getClass().getResourceAsStream("/image/image2.png");
		Photo photo = new Photo();
//		photoService.fillPhotoDetail(ins, photo);
		log.info(photo.getDetails().getExifVersion());
	}

	@Test
	public void testGetPhotosForUser() {
		photosForUser = photoService.getPhotosForUser("admin");
		Assert.isTrue(photosForUser.isEmpty());
	}
	
	@Test
	public void testCommonImageXmp() throws ImageReadException, IOException {
		final File imageFile = new File(getClass().getResource("/image/IMAG2534.jpg").getFile());
		 final ImageFormat imageFormat = Imaging.guessFormat(imageFile);
         String xmpXml = Imaging.getXmpXml(imageFile);
         
         log.debug(imageFormat);
         log.debug(xmpXml);
         
         if (null == xmpXml
                 && imageFormat.equals(ImageFormats.GIF)) {
             xmpXml = "temporary test until I can locate a GIF with XMP in the wild.";
         }
         if (null == xmpXml) {
         }
         
         assertNotNull(xmpXml);
         if (imageFormat.equals(ImageFormats.PNG)) { /*
                                                                  * do
                                                                  * nothing
                                                                  */
         } else if (imageFormat.equals(ImageFormats.TIFF)) { /*
                                                                          * do
                                                                          * nothing
                                                                          */
         } else if (imageFormat.equals(ImageFormats.GIF)) { /*
                                                                         * do
                                                                         * nothing
                                                                         */
         } else {
         }
         
//         final File tempFile = this.createTempFile(imageFile.getName() + ".", "."
//                 + imageFormat.getExtension());
//         
//         final BufferedImage image = Imaging.getBufferedImage(imageFile);
//         // ----
//         final Map<String,Object> params = new HashMap<String,Object>();
//         params.put(PARAM_KEY_XMP_XML, xmpXml);
//         
//         Imaging.writeImage(image, tempFile, imageFormat, params);
//         
//         final String xmpXmlOut = Imaging.getXmpXml(tempFile);
//         assertNotNull(xmpXmlOut);
//         assertEquals(xmpXmlOut, xmpXml);
         return;
         
//		IImageMetadata metadata = null;
//		try {
//			metadata = Imaging.getMetadata(ins, "");
//			ins.close();
//		} catch (ImageReadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (metadata instanceof JpegImageMetadata) {
//			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
//			List<IImageMetadataItem> items = jpegMetadata.getItems();
//			for (IImageMetadataItem item : items) {
//				log.debug(item.toString("PhotoDetail IImageMetadataItem: "));
//			}
//		}
	}

	private MultipartBody getMultipartBody() {
		List<Attachment> atts = new LinkedList<Attachment>();
		atts.add(new Attachment("image", "application/octet-stream", getClass().getResourceAsStream("/image/IMAG1340.jpg")));
		return new MultipartBody(atts, true);
	}
}
