package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.PhotoGps;

public interface PhotoGpsDao extends GenericDao<PhotoGps, Long>{

	/**
	 * 获取图片的所有gps信息
	 * 
	 * @param id
	 * @return
	 */
	public List<PhotoGps> getAll(Long id);
}
