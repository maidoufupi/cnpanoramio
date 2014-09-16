package com.cnpanoramio.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.TagDao;
import com.cnpanoramio.domain.Tag;
import com.cnpanoramio.domain.UserSettings;
import com.cnpanoramio.service.TagManager;
import com.cnpanoramio.service.UserSettingsManager;

@Service
@Transactional
public class TagManagerImpl extends GenericManagerImpl<Tag, Long> implements TagManager {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserSettingsManager userSettingsManager;
	
	private TagDao tagDao;
	
	public TagDao getTagDao() {
		return tagDao;
	}

	@Autowired
	public void setTagDao(TagDao tagDao) {
		this.dao = tagDao;
		this.tagDao = tagDao;
	}

	@Override
	public void deleteUserTag(User user, String tagContent) {
		// TODO 删除用户tag要先删除photo travel等上已经添加的tag
		UserSettings userSettings = userSettingsManager.get(user);
		for(Tag tag : userSettings.getTags()) {
			if(tagContent.equals(tag.getContent())) {
				this.remove(tag);
			}
		}
		
	}

}
