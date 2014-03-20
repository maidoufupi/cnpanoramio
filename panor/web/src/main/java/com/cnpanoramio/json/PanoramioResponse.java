package com.cnpanoramio.json;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.domain.PhotoPanoramio;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PanoramioResponse {

	public enum Status {
		/**
		 * 一切正常
		 */
		OK,
		/**
		 * 参数格式错误
		 */
		FORMAT_ERROR,
		/**
		 * 找不到USER对象
		 */
		NO_USER,
		/**
		 * 未授权
		 */
		NO_AUTHORIZE,
		/**
		 * 出现异常
		 */
		EXCEPTION
	}
	
	private String status;
	
	private String info;
	
	private List<PhotoPanoramio> photos;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<PhotoPanoramio> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoPanoramio> photos) {
		this.photos = photos;
	}
	
	
}
