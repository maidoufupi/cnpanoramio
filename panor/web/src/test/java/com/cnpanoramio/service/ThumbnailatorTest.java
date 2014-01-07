package com.cnpanoramio.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class ThumbnailatorTest {
	
	private InputStream ins;
    URL url;
    
	@Before
	public void preMethodSetup() {
		ins = getClass().getResourceAsStream("/image/image1.jpg");
		
		url = getClass().getResource("/image");
	}
	
	@Test
	public void testCreateThumbnail() throws IOException {
		BufferedImage originalImage = ImageIO.read(ins);
		File outputfile = new File("C:\\Users\\tiwen.wang\\Pictures\\image1.jpg");
		FileUtils.touch(outputfile);
		BufferedImage thumbnail = Thumbnails.of(originalImage)
		        .size(240, 240)
		        .crop(Positions.CENTER)
		        .asBufferedImage();
		ImageIO.write(thumbnail, "jpg", outputfile);
	}
}
