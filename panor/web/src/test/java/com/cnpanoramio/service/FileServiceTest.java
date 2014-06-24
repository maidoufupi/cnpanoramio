package com.cnpanoramio.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.service.impl.FileServiceImpl;


@RunWith(BlockJUnit4ClassRunner.class)
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
public class FileServiceTest {
	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private FileService fileService;

	private InputStream ins;

	@Before
	public void preMethodSetup() {
		FileServiceImpl fileServiceImpl = new FileServiceImpl();
//		fileServiceImpl.setEndpoint("http://panor-image.oss-cn-qingdao.aliyuncs.com");
//		fileServiceImpl.setAccessKeyId("eKJUbg7d49ojjYCf");
//		fileServiceImpl.setAccessKeySecret("70URxtirPN2sFtiEAu2nlwVS59RMUZ");
		fileServiceImpl.setParentPath("D:/dev/source/images");
		fileService = fileServiceImpl;
		ins = getClass().getResourceAsStream("/image/photo.jpg");
	}

	@After
	public void postMethodTearDown() {
		try {
			ins.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveFile() throws IOException, ImageReadException {
        String fileName = "IMAGE0001.jpg";
        log.info(ins);
		fileService.saveFile(FileService.TYPE_IMAGE, 1001L, "jpg", ins);
		
		File file = fileService.readFile(FileService.TYPE_IMAGE, 1001L, "jpg", String.valueOf(FileService.THUMBNAIL_LEVEL_0));
		log.info(file.getAbsoluteFile());
		log.info(file.length());
		
		Assert.assertTrue(file.isFile());

		Assert.assertTrue(fileService.deleteFile(FileService.TYPE_IMAGE, 1001L ,"jpg"));
		
		Assert.assertFalse(file.isFile());
	}

}
