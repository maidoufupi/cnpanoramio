package com.cnpanoramio.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.service.impl.FileServiceImpl;

@RunWith(BlockJUnit4ClassRunner.class)
public class ThumbnailatorTest {
	
	private InputStream ins;
    URL url;
    
    @Autowired
    private FileService fileService;
    
	@Before
	public void preMethodSetup() {
		fileService = new FileServiceImpl();
		ins = getClass().getResourceAsStream("/image/image1.jpg");		
		url = getClass().getResource("/image");
	}
	
	@Test
	public void testCreateThumbnail() throws IOException {
		
		File outputfile1 = new File("C:\\Users\\tiwen.wang\\Pictures\\image1.jpg");
		FileUtils.touch(outputfile1);
		
		BufferedImage originalImage = ImageIO.read(ins);
		
		BufferedImage thumbnail = Thumbnails.of(originalImage)
		        .size(240, 240)
		        .crop(Positions.CENTER)
		        .asBufferedImage();
		ImageIO.write(thumbnail, "jpg", outputfile1);
		
		File outputfile2 = new File("C:\\Users\\tiwen.wang\\Pictures\\image2.jpg");
		FileUtils.touch(outputfile2);
		thumbnail = Thumbnails.of(originalImage)
		        .size(100, 100)
		        .crop(Positions.CENTER)
		        .asBufferedImage();
		ImageIO.write(thumbnail, "jpg", outputfile2);
	}
	
	@Test
	public void testCreateThumbnails() throws IOException {

		BufferedImage originalImage = ImageIO.read(ins);

		BufferedImage thumbnail = null;
		for (int i = 0; i < 200; i++) {
			File outputfile1 = new File(
					"C:\\Users\\tiwen.wang\\Pictures\\image" + i + ".jpg");
			FileUtils.touch(outputfile1);
			thumbnail = Thumbnails.of(originalImage)
			        .size(240, 240)
			        .crop(Positions.CENTER)
			        .asBufferedImage();
			ImageIO.write(thumbnail, "jpg", outputfile1);

			File outputfile2 = new File(
					"C:\\Users\\tiwen.wang\\Pictures\\imagec" + i + ".jpg");
			FileUtils.touch(outputfile2);
			thumbnail = Thumbnails.of(originalImage)
			        .size(100, 100)
			        .crop(Positions.CENTER)
			        .asBufferedImage();
			ImageIO.write(thumbnail, "jpg", outputfile2);
		}
	}
}
