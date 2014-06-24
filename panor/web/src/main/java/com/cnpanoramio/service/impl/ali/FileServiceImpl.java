package com.cnpanoramio.service.impl.ali;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.cnpanoramio.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private transient final Log log = LogFactory.getLog(FileService.class);

	@Autowired
	private Environment env;

	@Value(value = "${aliyun.oss.key}")
	private String accessKeyId;

	@Value(value = "${aliyun.oss.secret}")
	private String accessKeySecret;

	@Value(value = "${aliyun.oss.endpoint}")
	private String endpoint;

	@Value(value = "${aliyun.oss.bucket}")
	private String bucket;

	private OSSClient client;

	@Override
	public boolean saveFile(String fileType, Long fileKey, String fileExt,
			InputStream ins) {
		String key;
		String uploadDir;
		FileOutputStream fos;
		if (fileType == TYPE_IMAGE) {
			saveImage(fileKey + "." + fileExt, ins);
		} else {
			saveImage(fileType + fileKey + "." + fileExt, ins);
		}
		return false;
	}

	private String getPhotoKey(Long id, int level) {
		return id + "-th" + level;
	}

	private void saveImage(String key, InputStream inputStream) {
		BufferedInputStream bins = new BufferedInputStream(inputStream);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] content = null;

		InputStream ins = null;

		try {
			IOUtils.copy(bins, baos);
			content = baos.toByteArray();
			ins = new ByteArrayInputStream(content);
			saveOSSObject(bucket, key, ins, content.length);
			content = null;
			ins.close();
			baos.reset();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	public File readFile(String fileType, Long fileKey, String fileExt,
			String ext) {

		if (fileType.equalsIgnoreCase(TYPE_IMAGE)) {
			getOSSObject(bucket, fileKey + "." + fileExt);
			
		}
		return null;
	}

	private void saveOSSObject(String bucketName, String key, InputStream ins,
			long length) {

		// 初始化一个OSSClient
		if (null == client) {
			log.info(endpoint);
			log.info(accessKeyId);
			log.info(accessKeySecret);
			if (null == endpoint || "".equals(endpoint)) {
				client = new OSSClient(accessKeyId, accessKeySecret);
			} else {
				client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}
		}

		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();

		// 必须设置ContentLength
		meta.setContentLength(length);

		// 上传Object.
		PutObjectResult result = client.putObject(bucketName, key, ins, meta);

		// 打印ETag
		log.debug(result.getETag());
	}

	private InputStream getOSSObject(String bucketName, String key) {
		// 初始化一个OSSClient
		if (null == client) {
			log.info(endpoint);
			log.info(accessKeyId);
			log.info(accessKeySecret);
			if (null == endpoint || "".equals(endpoint)) {
				client = new OSSClient(accessKeyId, accessKeySecret);
			} else {
				client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}
		}
		// 获取Object，返回结果为OSSObject对象
		OSSObject object = client.getObject(bucketName, key);

		// 获取Object的输入流
		InputStream objectContent = object.getObjectContent();
		return objectContent;
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

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	@Override
	public void saveThumbnails(String fileType, Long fileKey, String fileExt,
			InputStream ins) {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream readAsInputStream(String fileType, Long fileKey,
			String fileExt, String ext) {
		if (fileType.equalsIgnoreCase(TYPE_IMAGE)) {
			return getOSSObject(bucket, fileKey + "." + fileExt);
		}
		return null;
	}
}
