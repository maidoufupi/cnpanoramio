package com.cnpanoramio.service.impl.ali;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.cnpanoramio.service.FileService;


public class FileServiceImpl implements FileService {

	private transient final Log log = LogFactory.getLog(FileService.class);
	
	private final static String BUCKET_IMAGE = "panor-image";
	
	@Autowired
	private Environment env;
	
	@Value(value = "${aliyun.oss.key}")
	private String accessKeyId;
	
	@Value(value = "${aliyun.oss.secret}")
	private String accessKeySecret;
	
	@Value(value = "${aliyun.oss.endpoint}")
	private String endpoint;
	
	private OSSClient client;
	
	@Override
	public String uploadFile(String fileType, String fileName, File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveFile(String fileType, Long fileKey, String fileExt, InputStream ins) {
		String key;
		String uploadDir;
		FileOutputStream fos;
		if (fileType == TYPE_IMAGE) {
			saveImage(fileKey, ins);
		}else {
			key = getPhotoKey(fileKey, 0);
			try {
				fos = FileUtils.openOutputStream(new File(""));
				IOUtils.copy(ins, fos);
				ins.close();
				fos.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	private String getPhotoKey(Long id, int level) {
		return id + "-th" + level;
	}

	private void saveImage(Long id, InputStream inputStream) {
		BufferedInputStream bins = new BufferedInputStream(inputStream);
				  
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] content = null;

		InputStream ins = null;
		
		// THUMBNAIL_LEVEL_0
		try {
			IOUtils.copy(bins, baos);
			content = baos.toByteArray();
			ins = new ByteArrayInputStream(content);
			saveOSSObject(BUCKET_IMAGE, getPhotoKey(id, THUMBNAIL_LEVEL_0), ins, content.length);
			content = null;
			ins.close();
			baos.reset();			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// THUMBNAIL_LEVEL_1
		try {
			bins.reset();
			BufferedImage originalImage = ImageIO.read(bins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_1, THUMBNAIL_PIX_LEVEL_1)
					.asBufferedImage();

			ImageIO.write(thumbnail, "jpg", baos);
			content = baos.toByteArray();
			ins = new ByteArrayInputStream(content);
			saveOSSObject(BUCKET_IMAGE, getPhotoKey(id, THUMBNAIL_LEVEL_1), ins, content.length);
			content = null;
			ins.close();
			baos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// THUMBNAIL_LEVEL_2
		try {
			bins.reset();
			BufferedImage originalImage = ImageIO.read(bins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_2, THUMBNAIL_PIX_LEVEL_2)
					.asBufferedImage();
			ImageIO.write(thumbnail, "jpg", baos);
			content = baos.toByteArray();
			ins = new ByteArrayInputStream(content);
			saveOSSObject(BUCKET_IMAGE, getPhotoKey(id, THUMBNAIL_LEVEL_2), ins, content.length);
			content = null;
			ins.close();
			baos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// THUMBNAIL_LEVEL_3
		try {
			bins.reset();
			BufferedImage originalImage = ImageIO.read(bins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_3, THUMBNAIL_PIX_LEVEL_3)
					.crop(Positions.CENTER)
					.asBufferedImage();
			
			ImageIO.write(thumbnail, "jpg", baos);
			content = baos.toByteArray();
			ins = new ByteArrayInputStream(content);
			saveOSSObject(BUCKET_IMAGE, getPhotoKey(id, THUMBNAIL_LEVEL_3), ins, content.length);
			content = null;
			ins.close();
			baos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteFile(String fileType, Long id, String fileName) {

		if (null == fileName) {
			return false;
		} else {
			fileName.trim();
			if ("" == fileName)
				return false;
		}
		return false;
	}
	
	@Override
	public File readFile(String fileType, Long fileKey, String fileExt, int level) {
		String key = fileKey + "-th" + level;
		if (null == fileKey) {
			return null;
		} else {

		}
		return null;
	}
	
	private void saveOSSObject(String bucketName, String key, InputStream ins, int length ) {
		
        // 初始化一个OSSClient
		if(null == client) {
			log.info(endpoint);
			log.info(accessKeyId);
			log.info(accessKeySecret);
			client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		}

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentLength(length);
		
        // 上传Object.
        PutObjectResult result = client.putObject(bucketName, key, ins, meta);
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	

}
