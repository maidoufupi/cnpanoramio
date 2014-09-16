package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

import com.cnpanoramio.domain.Recycle;


public interface RecycleManager extends GenericManager<Recycle, Long> {
	
	/**
	 * 放入回收站
	 * 
	 * @param type
	 * @param entityId
	 * @return
	 */
	public Recycle saveRecycle(User user, Recycle.RecycleType type, Long entityId);

	/**
	 * 获取用户回收站所有
	 * 
	 * @param user
	 * @return
	 */
	public List<Recycle> getUserRecycleBin(User user);
	
	/**
	 * 撤销删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void cancelRecycle(Long id);
	
	/**
	 * 永久删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void removeRecycle(Long id);
	
	/**
	 * 清空用户的回收站
	 * 
	 * @param id 用户id
	 */
	public void emptyRecycleBin(Long id);
}
