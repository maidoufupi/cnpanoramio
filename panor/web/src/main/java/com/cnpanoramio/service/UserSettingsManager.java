package com.cnpanoramio.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.imaging.ImageReadException;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.UserSettings;

public interface UserSettingsManager {
	
	public UserSettings getCurrentUserSettings();
	public UserSettings getSettingsByUserName(String userName);
	
}
