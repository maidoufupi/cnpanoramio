package com.cnpanoramio.service;

import java.util.List;

import org.appfuse.model.User;

import com.cnpanoramio.json.RecycleResponse.Recycle;

public interface RecycleService {

	public List<Recycle> getUserRecycleBin(User user);
	
	/**
	 * 撤销删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void cancelRecycle(User user, Long id);
	
	/**
	 * 永久删除操作
	 * 
	 * @param id 删除记录的id
	 */
	public void removeRecycle(User user, Long id);
	
	/**
	 * 清空用户的回收站
	 * 
	 * @param user
	 */
	public void emptyRecycleBin(User user);
	
}
