package com.cnpanoramio.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.cnpanoramio.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadFile(String fileType, String fileName, File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveFile(String fileType, String file, InputStream ins) {

		String parent = getParentPath();
		if (null != parent) {
			String uploadDir = parent + "/" + fileType + "/" + file;
			FileOutputStream fos;
			try {
				fos = FileUtils.openOutputStream(new File(uploadDir));
				IOUtils.copy(ins, fos);
				fos.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
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
		return System.getProperty("cnpanoramio.root") + "/resources";
		// return getClass().getResource("/resources").getPath();
	}

	@Override
	public File readFile(String fileType, String fileName) {
		if (null == fileName) {
			return null;
		} else {
			fileName.trim();
			if ("" == fileName)
				return null;
		}
		String parent = getParentPath();
		if (null != parent) {
			String uploadDir = parent + "/" + fileType + "/" + fileName;
			return new File(uploadDir);
		}
		return null;
	}

}
