package com.cnpanoramio.service;

import java.io.File;
import java.io.InputStream;

public interface FileService {
	
	public static final String TYPE_IMAGE = "P";
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
    public String uploadFile(String fileType, String fileName, File file);
    
    /**
     * 保存文件
     * 
     * @param fileType
     * @param fileName
     * @param ins
     * @return String
     */
    public boolean saveFile(String fileType, Long fileKey, InputStream ins);
    
    /**
     * 读取文件
     * 
     * @param fileType
     * @param fileName
     * @return
     */
    public File readFile(String fileType, Long fileKey, int level);
    
    /**
     * 删除文件
     * 
     * @param fileType
     * @param fileName
     * @return boolean
     */
    public boolean deleteFile(String fileType, String fileName);
}
