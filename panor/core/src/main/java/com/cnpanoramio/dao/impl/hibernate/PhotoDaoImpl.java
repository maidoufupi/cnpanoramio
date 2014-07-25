package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;

@Repository("photoDao")
public class PhotoDaoImpl extends GenericDaoHibernate<Photo, Long> implements PhotoDao {

	public PhotoDaoImpl() {
		super(Photo.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Photo> getUserPhotos(User user) {
		return getSession().createCriteria(Photo.class)
			.add(Restrictions.eq("owner", user))
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.desc("createDate")).list();
	}
	
	public Photo savePhoto(Photo photo) {
        if (log.isDebugEnabled()) {
            log.debug("photo's id: " + photo.getId());
        }
        getSession().saveOrUpdate(photo);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return photo;
    }

	@Override
	public Photo save(Photo photo) {
		return this.savePhoto(photo);
	}

	@Override
	public List<Photo> getUserPhotos(User user, int pageSize, int pageNo) {
		// 按最新到最旧排列图片
        Criteria criteria = getSession().createCriteria(Photo.class)
			.add(Restrictions.eq("owner", user))
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.desc("createDate"));
        criteria.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
  
		return criteria.list();
	}

	@Override
	public Long getPhotoCount(User user) {
		Criteria criteria = getSession().createCriteria(Photo.class)
				.add(Restrictions.eq("owner", user))
				.add(Restrictions.eq("deleted", false));
		return (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
//		Query query = getSession().createQuery("select count(*) from Photo where owner = :owner");
//		
//		query.setParameter("owner", user);
		
//		return ((Long)query.list().get(0)).intValue();
	}

	@Override
	public Photo delete(Long id) {
		Photo photo = get(id);
		photo.setDeleted(true);
		save(photo);
		return photo;
	}

	@Override
	public Long getUserPhotoCountBytag(User user, String tag) {
		
		Query query = getSession().createQuery("select count(distinct p.id) from Photo as p inner join p.tags as t where p.owner = :owner and p.deleted = false and t.tag = :tag");
		
		query.setParameter("owner", user);
		query.setParameter("tag", tag);
		
		return (Long)query.list().get(0);
	}

	@Override
	public List<Photo> getUserPhotosByTag(User user, String tag) {
		Query query = getSession().createQuery("select distinct p from Photo as p inner join p.tags as t where p.owner = :owner and p.deleted = false and t.tag = :tag");
		
		query.setParameter("owner", user);
		query.setParameter("tag", tag);
		
		return query.list();
	}

	@Override
	public List<Photo> getUserPhotoPageByTag(User user, String tag,
			int pageSize, int pageNo) {
		Query query = getSession().createQuery("select distinct p from Photo as p inner join p.tags as t where p.owner = :owner and p.deleted = false and t.tag = :tag");
		
		query.setParameter("owner", user);
		query.setParameter("tag", tag);
		query.setFirstResult((pageNo - 1) * pageSize);  
        query.setMaxResults(pageSize);
		
		return query.list();
	}

	@Override
	public List<Photo> getUserPhotosBounds(User user, int pageSize, int pageNo,
			Double swLat, Double swLng, Double neLat, Double neLng) {
		// 按最新到最旧排列图片
        Criteria criteria = getSession().createCriteria(Photo.class)
			.add(Restrictions.eq("owner", user))
			.add(Restrictions.between("gpsPoint.lat", swLat, neLat))
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.desc("createDate"));
        
        if (neLng > swLng) {
        	criteria.add(Restrictions.between("gpsPoint.lng", swLng, neLng));
		} else {
			criteria.add(Restrictions.or(
					Restrictions.between("gpsPoint.lng", -180D, neLng), 
					Restrictions.between("gpsPoint.lng", swLng, 180D)));
		}
        
        criteria
        	.setFirstResult((pageNo - 1) * pageSize)
        	.setMaxResults(pageSize);
  
		return criteria.list();
	}

	
}
