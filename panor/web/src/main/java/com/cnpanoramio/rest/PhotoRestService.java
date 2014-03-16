package com.cnpanoramio.rest;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cnpanoramio.MapVendor;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.json.PhotoCameraInfo;
import com.cnpanoramio.json.PhotoProperties;
import com.cnpanoramio.json.Tags;
import com.cnpanoramio.service.FileService;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.utils.UserUtil;
import com.cnpanoramio.utils.PhotoUtil;

@Controller
@RequestMapping("/api/rest/photo")
public class PhotoRestService {
	
	private transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserManager userManager = null; 
	
	@Autowired
	private PhotoManager photoService;
	
	@Autowired
	private FileService fileService;
    
	@RequestMapping(value = "/{photoId}/best", method = RequestMethod.PUT)
	@ResponseBody
	public boolean markBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoService.markBest(id, true);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/best", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean removeBest(@PathVariable String photoId) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			photoService.markBest(id, false);
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/properties", method = RequestMethod.POST)
	@ResponseBody
	public boolean properties(@PathVariable String photoId,
			@RequestBody final PhotoProperties properties) {
		
		Long id = null;
		
		User me = UserUtil.getCurrentUser(userManager);
		id = Long.parseLong(photoId);
		Photo photo = photoService.getPhoto(id);
		if(photo.getOwner().equals(me)) {
			return photoService.properties(id, properties);
		}else {
			return false;
		}
	}
	
	@RequestMapping(value = "/{photoId}/camerainfo", method = RequestMethod.GET)
	@ResponseBody
	public PhotoCameraInfo cameraInfo(@PathVariable String photoId) {
		
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.getCameraInfo(id);
	}
	
	@RequestMapping(value = "/{photoId}/{level}", 
			method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource getPhoto(@PathVariable String photoId, @PathVariable int level) {
		
		Long id = null;
		id = Long.parseLong(photoId);
//		Photo photo = photoService.getPhoto(id);
		
		File file = fileService.readFile(FileService.TYPE_IMAGE, id, level);

		return new FileSystemResource(file);
	}

	@RequestMapping(value = "/{photoId}", method = RequestMethod.DELETE)
	@ResponseBody
	public PhotoProperties delete(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.delete(id);
	}
	
	@RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
	@ResponseBody
	public PhotoProperties get(@PathVariable String photoId) {
		Long id = null;
		id = Long.parseLong(photoId);
		return photoService.getPhotoProperties(id);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", 
    		method=RequestMethod.POST,
    		consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody PhotoProperties handleFileUpload(
    		@RequestParam("lat") String lat,
    		@RequestParam("lng") String lng,
    		@RequestParam(value="address", required=false) String address,
    		@RequestParam("vendor") String vendor,
    		@RequestParam("files[]") MultipartFile file){
    	
    	MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
		
        if (!file.isEmpty()) {
            try {
            	return photoService.upload(lat, lng, address, mVendor, file);
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
        } else {
        	log.debug("file is empty");
            return null;
        }
    }
        
    @RequestMapping(value = "/{photoId}/tag", 
    		method = RequestMethod.POST,
    		consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean addTags(@PathVariable String photoId,
			@RequestBody final Tags tags) {
    	log.info(tags.size());
    	Long id = Long.parseLong(photoId);
    	photoService.addTags(id, tags);
    	return true;
    }
    
    @RequestMapping(value = "/{photoId}/gps", 
    		method = RequestMethod.GET)
	@ResponseBody
	public PhotoGps getGPSInfo(@PathVariable String photoId,
			@RequestParam String vendor) {
    	
    	MapVendor mVendor = PhotoUtil.getMapVendor(vendor);
    	Long id = Long.parseLong(photoId);
    	return photoService.getGPSInfo(id, mVendor);
    }
    
    
}
