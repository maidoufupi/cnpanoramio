package com.cnpanoramio.dao.impl.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.FavoriteDao;
import com.cnpanoramio.domain.Favorite;

@Repository("favoriteDao")
public class FavoriteDaoImpl extends GenericDaoHibernate<Favorite, Long>
	implements FavoriteDao {

	public FavoriteDaoImpl() {
		super(Favorite.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Favorite get(Long photoId, Long userId) {
		return (Favorite) getSession().createCriteria(Favorite.class)
				.add(Restrictions.eq("pk.photo.id", photoId))
				.add(Restrictions.eq("pk.userId", userId))
				.uniqueResult();
	}

	@Override
	public Long getUserPhotoFavoriteCount(User user) {
		Query query = getSession().createQuery("select count(*) from Photo as p inner join p.favorites as f where p.owner = :user");
		query.setParameter("user", user);
		  
		Long count = (Long) query.uniqueResult();
		return count;
	}
}
