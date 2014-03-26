package com.cnpanoramio.dao.impl.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.ViewsDao;
import com.cnpanoramio.domain.Views;

@Repository("viewsDao")
public class ViewsDaoImpl extends GenericDaoHibernate<Views, Views.ViewsPK>
		implements ViewsDao {

	public ViewsDaoImpl() {
		super(Views.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void view(Long photoId, String appId) {
//		Date date = getDateWOTime();
		Views.ViewsPK pk = new Views.ViewsPK(photoId, appId);
		List<Views> views = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk", pk)).list();
		if (null == views || views.size() == 0) {
			Views view = new Views();
			view.setPk(pk);
			view.setCount(1);
			this.save(view);
		} else {
			views.get(0).setCount(views.get(0).getCount() + 1);
		}
	}

	@Override
	public int getViewsCount(Long photoId, String appId) {
//		Date date = getDateWOTime();
		Views.ViewsPK pk = new Views.ViewsPK(photoId, appId);

		Criteria criteria = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk.photoId", photoId))
				.add(Restrictions.eq("pk.appId", appId));

		criteria.setProjection(Projections.sum("count"));
		List<Long> views = criteria.list();
		return views.get(0).intValue();
	}

	@Override
	public int getViewsCount(Long photoId) {

		Criteria criteria = getSession().createCriteria(Views.class).add(
				Restrictions.eq("pk.photoId", photoId));

		criteria.setProjection(Projections.sum("count"));
		List<Long> views = criteria.list();
		if(null == views.get(0)) {
			return 0;
		}else {
			return views.get(0).intValue();			
		}
	}

	@Override
	public List<Views> getViewsList(Long photoId, String appId) {

		Criteria criteria = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk.photoId", photoId))
				.add(Restrictions.eq("pk.appId", appId));
		return criteria.list();
	}

	@Override
	public List<Views> getViewsList(Long photoId) {
		Criteria criteria = getSession().createCriteria(Views.class).add(
				Restrictions.eq("pk.photoId", photoId));
		return criteria.list();
	}
	
	@Override
	public Views getViews(Long photoId, String appId, Date date) {
		date = getDateWOTime(date);
		Criteria criteria = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk.photoId", photoId))
				.add(Restrictions.eq("pk.appId", appId))
				.add(Restrictions.eq("pk.date", date));
		return (Views)criteria.uniqueResult();
	}
	
	@Override
	public int getViewsCount(Long photoId, Date date) {
		date = getDateWOTime(date);
		Criteria criteria = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk.photoId", photoId))
				.add(Restrictions.eq("pk.date", date));
		criteria.setProjection(Projections.sum("count"));
		List<Long> views = criteria.list();
		return views.get(0).intValue();
	}
	
	@Override
	public List<Views> getViewsList(Long photoId, Date date) {
		date = getDateWOTime(date);
		Criteria criteria = getSession().createCriteria(Views.class)
				.add(Restrictions.eq("pk.photoId", photoId))
				.add(Restrictions.eq("pk.date", date));
		return criteria.list();
	}

	private Date getDateWOTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
