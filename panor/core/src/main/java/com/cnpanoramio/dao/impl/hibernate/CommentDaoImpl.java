package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.domain.Comment;

@Repository("commentDao")
public class CommentDaoImpl extends GenericDaoHibernate<Comment, Long> 
	implements CommentDao {
	
	public CommentDaoImpl() {
		super(Comment.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Comment> getComments(Long photoId) {
		Query photoListQuery = getSession().createQuery("select c from Comment as c left join c.photo as p "
				+ "where p.id = :photoid order by create_time desc");
		
		photoListQuery.setParameter("photoid", photoId);
		List<Comment> res = photoListQuery.list();
        return res;
	}

	@Override
	public List<Comment> getCommentPager(Long photoId, int pageSize, int pageNo) {
		Query query = getSession().createQuery("select c from Comment as c left join c.photo as p "
				+ "where p.id = :photoid order by create_time desc");
		
		query.setParameter("photoid", photoId);
		query.setFirstResult((pageNo - 1) * pageSize);  
        query.setMaxResults(pageSize);  
  
		List<Comment> res = query.list();
		return res;
	}

	@Override
	public Long getCommentSize(Long photoId) {
		Query query = getSession().createQuery("select count(*) from Comment as c left join c.photo as p where p.id = :photoid");
		
		query.setParameter("photoid", photoId);
  
		Long count = (Long) query.uniqueResult();
		return count;
	}

}
