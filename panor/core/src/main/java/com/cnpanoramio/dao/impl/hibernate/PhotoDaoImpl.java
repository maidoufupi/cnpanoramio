package com.cnpanoramio.dao.impl.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.PhotoDao;
import com.cnpanoramio.domain.Photo;

@Repository("photoDao")
@Transactional
public class PhotoDaoImpl extends GenericDaoHibernate<Photo, Long> implements PhotoDao {

	public PhotoDaoImpl() {
		super(Photo.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Photo> getUserPhotos(User user) {
		Query photoListQuery = getSession().createQuery("from Photo p where owner = :owner");
				
		photoListQuery.setParameter("owner", user);

		return photoListQuery.list();
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
}
