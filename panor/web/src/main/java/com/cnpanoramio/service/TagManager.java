package com.cnpanoramio.service;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

import com.cnpanoramio.domain.Tag;

public interface TagManager extends GenericManager<Tag, Long> {

	/**
	 * 删除用户标签
	 * 
	 * @param user
	 * @param tag
	 */
	public void deleteUserTag(User user, String tag);
}
