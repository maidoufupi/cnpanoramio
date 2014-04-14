package com.cnpanoramio.service;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface FileService {
	
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_AVATAR = "avatar";
	
	public static final int THUMBNAIL_LEVEL_0 = 0;
	public static final int THUMBNAIL_LEVEL_1 = 1;
	public static final int THUMBNAIL_LEVEL_2 = 2;
	public static final int THUMBNAIL_LEVEL_3 = 3;
	
	public static final int THUMBNAIL_PIX_LEVEL_1 = 900;
	public static final int THUMBNAIL_PIX_LEVEL_2 = 240;
	public static final int THUMBNAIL_PIX_LEVEL_3 = 33;
	
	/**
     * 上传文件
     * 
     * @param fileType
     * @param fileName
     * @param file
     * @return String
     */
//    public String uploadFile(String fileType, String fileName, File file);
    
    /**
     * 保存文件
     * 
     * @param fileType
     * @param fileName
     * @param ins
     * @return String
     */
    public boolean saveFile(String fileType, Long fileKey, String fileExt, InputStream ins);
    
    /**
     * 保存图片缩略图
     * 
     * @param fileType
     * @param fileKey
     * @param fileExt
     * @param ins
     */
    public void saveThumbnails(String fileType, Long fileKey, String fileExt, InputStream ins);

    /**
     * 读取文件
     * 
     * @param fileType
     * @param fileName
     * @return
     */
    public File readFile(String fileType, Long fileKey, String fileExt, int level);
    
    /**
     * 删除文件
     * 
     * @param fileType
     * @param fileName
     * @return boolean
     */
    public boolean deleteFile(String fileType, Long fileKey, String fileExt);
}
