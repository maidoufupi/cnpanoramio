package com.cnpanoramio.service.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cnpanoramio.service.FileService;

@Service
public class FileServiceImpl  implements FileService {

	@Value(value = "${file.parent.path}")
	private String parentPath;

	public String uploadFile(String fileType, String fileName, File file) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean saveFile(String fileType, Long fileKey, InputStream ins) {
		String key;
		String uploadDir;
		FileOutputStream fos;
		String parent = getParentPath();
		if (fileType == TYPE_IMAGE) {
			saveImage(fileKey, ins);
		}else {
			key = getPhotoKey(fileKey, 0);
			uploadDir = parent + "/" + fileType + "/" + key;
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
		}
		return false;
	}

	private String getPhotoKey(Long id, int level) {
		return id + "-th" + level;
	}

	private void saveImage(Long id, InputStream inputStream) {
		
		String uploadDir;
		File uploadFile;
		String parent = getParentPath();
		FileOutputStream fos;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			IOUtils.copy(inputStream, baos);
			inputStream.close();
		} catch (IOException e) {
			if(null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e1) {
					// i
					e1.printStackTrace();
				}
			}
		}
		byte[] content = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		InputStream ins = new ByteArrayInputStream(content);
		
		// THUMBNAIL_LEVEL_0
		uploadDir = parent + "/" + TYPE_IMAGE + "/"
				+ getPhotoKey(id, THUMBNAIL_LEVEL_0);
		try {
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			fos = FileUtils.openOutputStream(uploadFile);
			IOUtils.copy(ins, fos);
			fos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// THUMBNAIL_LEVEL_1
		uploadDir = parent + "/" + TYPE_IMAGE + "/"
				+ getPhotoKey(id, THUMBNAIL_LEVEL_1);
		ins = new ByteArrayInputStream(content);
		try {
			BufferedImage originalImage = ImageIO.read(ins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_1, THUMBNAIL_PIX_LEVEL_1)
					.asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, "jpg", uploadFile);
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// THUMBNAIL_LEVEL_2
		uploadDir = parent + "/" + TYPE_IMAGE + "/"
				+ getPhotoKey(id, THUMBNAIL_LEVEL_2);
		ins = new ByteArrayInputStream(content);
		try {

			BufferedImage originalImage = ImageIO.read(ins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_2, THUMBNAIL_PIX_LEVEL_2)
					.asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, "jpg", uploadFile);
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// THUMBNAIL_LEVEL_3
		uploadDir = parent + "/" + TYPE_IMAGE + "/"
				+ getPhotoKey(id, THUMBNAIL_LEVEL_3);
		ins = new ByteArrayInputStream(content);
		try {

			BufferedImage originalImage = ImageIO.read(ins);
			BufferedImage thumbnail = Thumbnails.of(originalImage)
					.size(THUMBNAIL_PIX_LEVEL_3, THUMBNAIL_PIX_LEVEL_3)
					.crop(Positions.CENTER)
					.asBufferedImage();
			uploadFile = new File(uploadDir);
			FileUtils.touch(uploadFile);
			ImageIO.write(thumbnail, "jpg", uploadFile);
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteFile(String fileType, String fileName) {

		if (null == fileName) {
			return false;
		} else {
			fileName.trim();
			if ("" == fileName)
				return false;
		}
		String parent = getParentPath();
		if (null != parent) {
			String uploadDir = parent + "/" + fileType + "/" + fileName;
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
//		return System.getProperty("cnpanoramio.root") + "/resources";
		// return getClass().getResource("/resources").getPath();
	}

	@Override
	public File readFile(String fileType, Long fileKey, int level) {
		String key = fileKey + "-th" + level;
		if (null == fileKey) {
			return null;
		} else {

		}
		String parent = getParentPath();
		if (null != parent) {
			String uploadDir = parent + "/" + fileType + "/" + key;
			return new File(uploadDir);
		}
		return null;
	}

}
