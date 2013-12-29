package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.CommentDao;
import com.cnpanoramio.domain.Comment;

@Repository("commentDao")
@Transactional
public class CommentDaoImpl extends GenericDaoHibernate<Comment, Long> implements CommentDao {

	public CommentDaoImpl() {
		super(Comment.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Comment> getComments(Long photoId) {
		Query photoListQuery = getSession().createQuery("select c from Comment as c left join c.photo as p where p.id = :photoid");
		
		photoListQuery.setParameter("photoid", photoId);
		List<Comment> res = photoListQuery.list();
        return res;
	}

}
