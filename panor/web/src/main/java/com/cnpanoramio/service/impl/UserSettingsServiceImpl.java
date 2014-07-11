package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpanoramio.json.UserResponse.Recycle;
import com.cnpanoramio.service.UserSettingsManager;
import com.cnpanoramio.service.UserSettingsService;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

	@Autowired
	private UserSettingsManager userSettingsManager;
	
	@Override
	public List<Recycle> getRecycleBin(Long id) {
		return convertRecycleList(userSettingsManager.getRecycleBin(id));
	}

	private List<Recycle> convertRecycleList(List<com.cnpanoramio.domain.Recycle> recycles) {
		List<Recycle> rs = new ArrayList<Recycle>();
		for(com.cnpanoramio.domain.Recycle recycle : recycles) {
			Recycle r = new Recycle();
			r.setId(recycle.getId());
			r.setCreateTime(recycle.getCreateTime());
			r.setUserId(recycle.getUser().getId());
			r.setRecyType(recycle.getRecyType());
			r.setRecyId(recycle.getRecyId());
			rs.add(r);
		}
		return rs;
	}
}
