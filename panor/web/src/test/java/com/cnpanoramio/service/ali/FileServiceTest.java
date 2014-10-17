package com.cnpanoramio.service.ali;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.impl.ali.FileServiceImpl;

@RunWith(BlockJUnit4ClassRunner.class)
public class FileServiceTest {
	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private FileService fileService;
	
	private InputStream ins;
	
	@Before
	public void preMethodSetup() {
		FileServiceImpl fileServiceImpl = new FileServiceImpl();
		fileServiceImpl.setEndpoint("http://oss-cn-qingdao.aliyuncs.com/");
		fileServiceImpl.setAccessKeyId("eKJUbg7d49ojjYCf");
		fileServiceImpl.setAccessKeySecret("70URxtirPN2sFtiEAu2nlwVS59RMUZ");
		fileServiceImpl.setBucket("panor-image");
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
	public void testSaveFile() throws IOException {
		fileService.saveFile(FileService.TYPE_IMAGE, 1L, "jpg", ins);
	}
}
