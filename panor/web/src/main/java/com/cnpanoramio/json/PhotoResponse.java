package com.cnpanoramio.json;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cnpanoramio.domain.PhotoGps;
import com.cnpanoramio.json.CommentResponse.Comment;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class PhotoResponse extends ExceptionResponse {
	
	public PhotoResponse() {
		super();
	}
	
	public PhotoResponse(Status ok) {
		super(ok);
	}

	public static PhotoResponse getInstance() {
		return new PhotoResponse(Status.OK);
	}
	
	private PhotoProperties prop;

	private Collection<Comment> comments;
	
//	@XmlAttribute
	@JsonProperty("camera_info")
	private PhotoCameraInfo camerainfo;
	
//	@XmlAttribute
	private List<PhotoGps> gps;

	public PhotoProperties getProp() {
		return prop;
	}

	public void setProp(PhotoProperties prop) {
		this.prop = prop;
	}

	public PhotoCameraInfo getCamerainfo() {
		return camerainfo;
	}

	public void setCamerainfo(PhotoCameraInfo camerainfo) {
		this.camerainfo = camerainfo;
	}

	public List<PhotoGps> getGps() {
		return gps;
	}

	public void setGps(List<PhotoGps> gps) {
		this.gps = gps;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}
	
	
}
