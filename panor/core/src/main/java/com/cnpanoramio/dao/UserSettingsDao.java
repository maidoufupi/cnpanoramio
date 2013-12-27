package com.cnpanoramio.dao;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.UserSettings;

public interface UserSettingsDao extends GenericDao<UserSettings, Long> {

	public UserSettings getByUserName(String userName);
}
