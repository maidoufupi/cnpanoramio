package com.cnpanoramio.service.json;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PhotoThumbnails")
public class PhotoThumbnails {
	
	 private Collection<PhotoThumbnail> thumbnails;
	 
	    public Collection<PhotoThumbnail> getThumbnails() {
	        return thumbnails;
	    }
	 
	    public void setThumbnails(Collection<PhotoThumbnail> c) {
	        this.thumbnails = c;
	    }
	
}
