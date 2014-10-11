package com.cnpanoramio.json;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;


@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PanoramioResponse extends ExceptionResponse {
	
	private List<PhotoProperties> photos;

	public List<PhotoProperties> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoProperties> photos) {
		this.photos = photos;
	}
	
}
