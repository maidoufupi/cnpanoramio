package com.cnpanoramio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.domain.Recycle.RecycleType;
import com.cnpanoramio.json.RecycleResponse.Recycle;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.RecycleManager;
import com.cnpanoramio.service.RecycleService;
import com.cnpanoramio.service.TravelService;

@Service
@Transactional
public class RecycleServiceImpl implements RecycleService {

	@Autowired
	private RecycleManager recycleManager;
	
	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private TravelService travelService;
	
	@Override
	public List<Recycle> getUserRecycleBin(User user) {
		return convertRecycleList(recycleManager.getUserRecycleBin(user));
	}

	@Override
	public void removeRecycle(User user, Long id) {
		com.cnpanoramio.domain.Recycle recycle = recycleManager.get(id);
		checkMyRecycle(user, recycle);
		
		recycleManager.removeRecycle(id);
	}

	@Override
	public void emptyRecycleBin(User user) {
//		com.cnpanoramio.domain.Recycle recycle = recycleManager.get(id);
//		checkMyRecycle(user, recycle);
		
		recycleManager.emptyRecycleBin(user.getId());
	}
	
	@Override
	public void cancelRecycle(User user, Long id) {
		com.cnpanoramio.domain.Recycle recycle = recycleManager.get(id);
		checkMyRecycle(user, recycle);
		
		recycleManager.cancelRecycle(id);
		
	}
	
	private List<Recycle> convertRecycleList(
			List<com.cnpanoramio.domain.Recycle> recycles) {
		List<Recycle> rs = new ArrayList<Recycle>();
		for (com.cnpanoramio.domain.Recycle recycle : recycles) {
			Recycle r = new Recycle();
			r.setId(recycle.getId());
			r.setCreateTime(recycle.getCreateDate());
			r.setUserId(recycle.getUser().getId());
			r.setRecyType(recycle.getRecyType());
			r.setRecyId(recycle.getRecyId());
			
			if (recycle.getRecyType() == RecycleType.photo) {
				r.setPhoto(photoManager.getPhotoProperties(recycle
						.getRecyId(), null));
			}else if(recycle.getRecyType() == RecycleType.travel) {
				r.setTravel(travelService.getTravel(recycle.getRecyId()));
			}

			rs.add(r);
		}
		return rs;
	}
	
	private void checkMyRecycle(User me, com.cnpanoramio.domain.Recycle recycle) {
		if(!recycle.getUser().getId().equals(me.getId())) {
			throw new AccessDeniedException("Recycle (id=" + recycle.getId()
					+ ") is not belong to you");
		}
	}
}
