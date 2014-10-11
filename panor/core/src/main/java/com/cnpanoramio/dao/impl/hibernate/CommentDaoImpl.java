package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.domain.Circle;
import com.cnpanoramio.domain.Comment;
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Comment.CommentType;

@Repository("commentDao")
public class CommentDaoImpl extends GenericDaoHibernate<Comment, Long> 
	implements CommentDao {
	
	public CommentDaoImpl() {
		super(Comment.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Comment persist(Comment comment) {
		getSession().persist(comment);
		return comment;
	}
	
	@Override
	public List<Comment> getComments(Photo photo) {
		
		Criteria criteria = getSession().createCriteria(Comment.class)
				.add(Restrictions.eq("type", CommentType.photo))
				.add(Restrictions.eq("photo", photo))
				.addOrder(Order.desc("modifyDate"));
		
		return criteria.list();
	}

	@Override
	public List<Comment> getComments(Photo photo, int pageSize, int pageNo) {
		
		Criteria criteria = getSession().createCriteria(Comment.class)
				.add(Restrictions.eq("type", CommentType.photo))
				.add(Restrictions.eq("photo", photo))
				.addOrder(Order.desc("modifyDate"));
		
		criteria.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
		
		return criteria.list();
	}

	@Override
	public Long getCommentSize(Long photoId) {
		Query query = getSession().createQuery("select count(*) from Comment as c left join c.photo as p where p.id = :photoid");
		
		query.setParameter("photoid", photoId);
  
		Long count = (Long) query.uniqueResult();
		return count;
	}

}
