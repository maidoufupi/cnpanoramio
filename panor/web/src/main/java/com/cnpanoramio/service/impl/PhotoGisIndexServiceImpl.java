package com.cnpanoramio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoGisIndexDao;
import com.cnpanoramio.service.PhotoGisIndexService;

@Service("gisindexService")
@Transactional
public class PhotoGisIndexServiceImpl implements PhotoGisIndexService {
	
	@Autowired
	private PhotoGisIndexDao photoDao;
	
	@Override
	public boolean updateGisIndex() {
		return photoDao.updateGisIndex();
	}

}
