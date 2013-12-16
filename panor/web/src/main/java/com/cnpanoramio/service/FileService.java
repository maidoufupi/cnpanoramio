package com.cnpanoramio.service;

import java.io.File;
import java.io.InputStream;

public interface FileService {
	
	public static final String TYPE_IMAGE = "image";
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
    public boolean saveFile(String fileType, String fileName, InputStream ins);
    
    /**
     * 读取文件
     * 
     * @param fileType
     * @param fileName
     * @return
     */
    public File readFile(String fileType, String fileName);
    
    /**
     * 删除文件
     * 
     * @param fileType
     * @param fileName
     * @return boolean
     */
    public boolean deleteFile(String fileType, String fileName);
}
