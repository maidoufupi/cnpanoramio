package com.cnpanoramio.service.impl;

import java.awt.image.BufferedImage;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cnpanoramio.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private transient final Log log = LogFactory.getLog(FileService.class);

	@Value(value = "${file.parent.path}")
	private String parentPath;
	
	public boolean saveFile(String fileType, Long fileKey, String fileExt, InputStream ins) {
		String uploadDir;
		FileOutputStream fos;
		String parent = getParentPath();
		
		uploadDir = getFilePath(fileType, fileKey, fileExt, THUMBNAIL_LEVEL_0);
		try {
			fos = FileUtils.openOutputStream(new File(uploadDir));
			IOUtils.copy(ins, fos);
			ins.close();
			fos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据文件ID 扩展名 图片缩略图级别 组装文件名
	 * 
	 * @param id
	 * @param fileExt
	 * @param level
	 * @return
	 */
	private String getPhotoKey(Long id, String fileExt, int level) {
		String l = "";
		if(level > 0) {
			l = "-th" + level;
		}
		if (StringUtils.hasText(fileExt)) {
			return id + l + "." + fileExt.trim();
		} else {
			return id + l;
		}
	}

	private void saveImage(Long id, String fileExt, InputStream inputStream) {

		String uploadDir;
		File uploadFile;
		String parent = getParentPath();

		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(inputStream);
			
			inputStream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		log.info("THUMBNAIL_LEVEL_0");
//		// THUMBNAIL_LEVEL_0
//		uploadDir = parent + "/" + TYPE_IMAGE + "/"
//				+ getPhotoKey(id, fileExt, THUMBNAIL_LEVEL_0);
//		
//		try {
//			uploadFile = new File(uploadDir);
//			FileUtils.touch(uploadFile);
//			ImageIO.write(originalImage, fileExt, uploadFile);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		BufferedImage thumbnail = null;

		log.debug("THUMBNAIL_LEVEL_1 " + id);
		// THUMBNAIL_LEVEL_1
//		uploadDir = parent + "/" + TYPE_IMAGE + "/"
//				+ getPhotoKey(id, fileExt, THUMBNAIL_LEVEL_1);
		uploadDir = getFilePath(TYPE_IMAGE, id, fileExt, THUMBNAIL_LEVEL_1);
		try {
			thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_1, THUMBNAIL_PIX_LEVEL_1)
					.asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, fileExt, uploadFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.debug("THUMBNAIL_LEVEL_2 " + id);
		// THUMBNAIL_LEVEL_2
//		uploadDir = parent + "/" + TYPE_IMAGE + "/"
//				+ getPhotoKey(id, fileExt, THUMBNAIL_LEVEL_2);
		uploadDir = getFilePath(TYPE_IMAGE, id, fileExt, THUMBNAIL_LEVEL_2);
		try {
			thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_2, THUMBNAIL_PIX_LEVEL_2)
					.asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, fileExt, uploadFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.debug("THUMBNAIL_LEVEL_3 " + id);
		// THUMBNAIL_LEVEL_3
//		uploadDir = parent + "/" + TYPE_IMAGE + "/"
//				+ getPhotoKey(id, fileExt, THUMBNAIL_LEVEL_3);
		uploadDir = getFilePath(TYPE_IMAGE, id, fileExt, THUMBNAIL_LEVEL_3);
		try {
			thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_3, THUMBNAIL_PIX_LEVEL_3)
					.crop(Positions.CENTER).asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, fileExt, uploadFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("THUMBNAIL_LEVEL_end");
	}

	@Override
	public boolean deleteFile(String fileType, Long fileKey, String fileExt) {

		String parent = getParentPath();
		if (null != parent) {
			String uploadDir = parent + "/" + fileType + "/"
					+ getPhotoKey(fileKey, fileExt, THUMBNAIL_LEVEL_0);
			new File(uploadDir).delete();
			uploadDir = parent + "/" + fileType + "/"
					+ getPhotoKey(fileKey, fileExt, THUMBNAIL_LEVEL_1);
			new File(uploadDir).delete();
			uploadDir = parent + "/" + fileType + "/"
					+ getPhotoKey(fileKey, fileExt, THUMBNAIL_LEVEL_2);
			new File(uploadDir).delete();
			uploadDir = parent + "/" + fileType + "/"
					+ getPhotoKey(fileKey, fileExt, THUMBNAIL_LEVEL_3);
			return new File(uploadDir).delete();
			// return FileUtils.deleteQuietly(new File(uploadDir));
		}
		return false;
	}

	/**
	 * 取保存资源的根目录
	 * 
	 * @return
	 */
	public String getParentPath() {
		return parentPath;
		// return System.getProperty("cnpanoramio.root") + "/resources";
		// return getClass().getResource("/resources").getPath();
	}

	@Override
	public File readFile(String fileType, Long fileKey, String fileExt,
			int level) {

		File file = new File(getFilePath(fileType, fileKey, fileExt, level));
		if(file.exists() && !file.isDirectory()) {
			return file;
		}else {
			return null;
		}
		
	}

	/**
	 * 获取文件路径名
	 * 
	 * @param fileType
	 * @param fileKey
	 * @param fileExt
	 * @param level
	 * @return
	 */
	private String getFilePath(String fileType, Long fileKey, String fileExt,
			int level) {
		return getParentPath() + "/" + fileType + "/"
				+ getPhotoKey(fileKey, fileExt, level);
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}


	@Override
	public void saveThumbnails(String fileType, Long fileKey, String fileExt,
			InputStream ins) {
		saveImage(fileKey, fileExt, ins);
	}

}
