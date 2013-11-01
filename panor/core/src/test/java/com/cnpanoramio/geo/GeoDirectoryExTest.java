package com.cnpanoramio.geo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.cnpanoramio.domain.Point;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.lang.GeoLocation;

public class GeoDirectoryExTest {
	protected transient final Log log = LogFactory.getLog(getClass());

	@Before
	public void preMethodSetup() {

	}

	@After
	public void postMethodTearDown() {

	}

	@Test
	public void testPhotoStore() throws ImageProcessingException, IOException {
		BufferedInputStream bstream = new BufferedInputStream(getClass().getResourceAsStream("/image/IMAG1340.jpg"));
		// Read all metadata from the image
		Metadata metadata = ImageMetadataReader.readMetadata(bstream, true);
		// See whether it has GPS data
		GpsDirectory gpsDirectory = metadata
				.getOrCreateDirectory(GpsDirectory.class);

		Point point = new Point();
		if (gpsDirectory != null) {
			// Try to read out the location, making sure it's non-zero
			GeoLocation geoLocation = gpsDirectory.getGeoLocation();

			if (geoLocation != null && !geoLocation.isZero()) {
				point.setGeoLat(geoLocation.getLatitude());
				point.setGeoLong(geoLocation.getLongitude());
			}
		}
		
		for (Directory directory : metadata.getDirectories()) {  
            for (Tag tag : directory.getTags()) {  
                String tagName = tag.getTagName();  
                String desc = tag.getDescription();  
//                if (tagName.equals("Image Height")) {  
//                    //图片高度  
//                    imgInfoBean.setImgHeight(desc);  
//                } else if (tagName.equals("Image Width")) {  
//                    //图片宽度  
//                    imgInfoBean.setImgWidth(desc);  
                if (tagName.equals("Date/Time Original")) {  
                    //拍摄时间  
                	Assert.notNull(desc);
                	log.info(desc);
                } else if (tagName.equals("GPS Altitude")) {
                    //海拔  
                	Assert.notNull(desc);
                	log.info(desc);
                }
            }  
        }  
		bstream.close();
	}
}
