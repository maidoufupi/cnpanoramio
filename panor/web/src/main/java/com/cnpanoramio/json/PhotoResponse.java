package com.cnpanoramio.json;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.domain.PhotoGps;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PhotoResponse {
	
	public enum Status {
		/**
		 * 一切正常
		 */
		OK,
		/**
		 * 图片ID格式错误
		 */
		ID_FORMAT_ERROR,
		/**
		 * 找不到对象
		 */
		NO_ENTITY,
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
	
//	@XmlAttribute
	private String info;
	
//	@XmlAttribute
	private PhotoProperties prop;
	
//	@XmlAttribute
	private PhotoComments comments;
	
//	@XmlAttribute
	@JsonProperty("camera_info")
	private PhotoCameraInfo camerainfo;
	
//	@XmlAttribute
	private List<PhotoGps> gps;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PhotoProperties getProp() {
		return prop;
	}

	public void setProp(PhotoProperties prop) {
		this.prop = prop;
	}

	public PhotoComments getComments() {
		return comments;
	}

	public void setComments(PhotoComments comments) {
		this.comments = comments;
	}

	public PhotoCameraInfo getCamerainfo() {
		return camerainfo;
	}

	public void setCamerainfo(PhotoCameraInfo camerainfo) {
		this.camerainfo = camerainfo;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<PhotoGps> getGps() {
		return gps;
	}

	public void setGps(List<PhotoGps> gps) {
		this.gps = gps;
	}
	
	
}
