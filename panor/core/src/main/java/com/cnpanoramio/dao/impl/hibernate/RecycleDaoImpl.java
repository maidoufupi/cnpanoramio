package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.RecycleDao;
import com.cnpanoramio.domain.Recycle;

@Repository("recycleDao")
public class RecycleDaoImpl extends GenericDaoHibernate<Recycle, Long> implements RecycleDao {

	public RecycleDaoImpl() {
		super(Recycle.class);
	}

	@Override
	public Recycle persist(Recycle recycle) {
		getSession().persist(recycle);
		return recycle;
	}
	
	@Override
	public List<Recycle> getUserRecycle(Long id) {
		return getSession().createCriteria(Recycle.class)
				.add(Restrictions.eq("user.id", id)).list();
	}
}
