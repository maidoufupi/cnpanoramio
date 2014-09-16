package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;

public interface TagDao extends GenericDao<Tag, Long> {
	
	/**
	 * 获取或创建用户的标签
	 * 
	 * @return
	 */
	public Tag getOrCreateUserTag(UserSettings user, String content);
	
}
