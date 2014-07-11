package com.cnpanoramio.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Recycle;

public interface RecycleDao extends GenericDao<Recycle, Long> {

	/**
	 * 获取用户全部的删除记录
	 * 
	 * @param id
	 * @return
	 */
	public List<Recycle> getUserRecycle(Long id);
}
