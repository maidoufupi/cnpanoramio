package com.cnpanoramio.service.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CommentQueryInput")
public class CommentQueryInput {

	private Long photoId;
    private Integer pageNo;
    private Integer pageSize;
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
    
}
