package com.cnpanoramio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Query;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cnpanoramio.dao.TravelDao;
import com.cnpanoramio.json.TravelResponse.Travel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = true,
        locations = { 
				"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-service.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "classpath*:/applicationContext-test.xml",
                "/WEB-INF/spring-security.xml",
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"})
//@Transactional
public class TravelServiceTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private TravelService travelService;
	@Autowired
	private TravelManager travelManager;
	@Autowired
	private TravelDao travelDao;
	
	@Resource
    private SessionFactory sessionFactory;
	
	private User user;
	
	private List<com.cnpanoramio.domain.Travel> deleteTravels = new ArrayList<com.cnpanoramio.domain.Travel>();
	
	@Before
	public void preMethodSetup() {
//		user = new User();
//		user.addRole(roleManager.getRole(Constants.USER_ROLE));
//		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                "user", "user", user.getAuthorities());
//        auth.setDetails(user);
//		SecurityContextHolder.getContext().setAuthentication(auth);
		
//		com.cnpanoramio.domain.Travel tra = new com.cnpanoramio.domain.Travel();
//		tra.setAddress("江苏省常州镇111");
//		tra.setTitle("江苏省常州");
//		travelDao.save(tra);
//		deleteTravels.add(tra);
//		
//		com.cnpanoramio.domain.Travel tra2 = new com.cnpanoramio.domain.Travel();
//		tra2.setAddress("江苏无锡2222");
//		tra2.setTitle("江苏");
//		travelDao.save(tra2);
//		deleteTravels.add(tra2);
	}
	
	@After
	public void postMethodTearDown() {
		for(com.cnpanoramio.domain.Travel travel : deleteTravels) {
			travelDao.remove(travel);
		}
	}
	
	@Test
	public void testAddSpotPhotos() {
		List<Long> photoIds = new ArrayList<Long>();
		photoIds.add(5L);
		Travel travel = travelService.addSpotPhotos(1L, photoIds);
		travel = travelService.addSpotPhotos(2L, photoIds);
		
		// debug
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			log.debug("input param:" + ow.writeValueAsString(travel));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch() {
		com.cnpanoramio.domain.Travel tra = new com.cnpanoramio.domain.Travel();
		tra.setAddress("江苏省常州镇111");
		tra.setTitle("江苏省常州");
		travelManager.save(tra);
		deleteTravels.add(tra);
		
		com.cnpanoramio.domain.Travel tra2 = new com.cnpanoramio.domain.Travel();
		tra2.setAddress("江苏无锡2222");
		tra2.setTitle("江苏");
		travelManager.save(tra2);
		deleteTravels.add(tra2);
		
		List<com.cnpanoramio.domain.Travel> travels = travelManager.search("江苏省", Travel.class);
		
		for(com.cnpanoramio.domain.Travel travel : travels) {
			log.info(travel.getTitle());
			log.info(travel.getAddress());
		}
		Assert.assertEquals(2, travels.size());
		
//		travelDao.remove(tra2);
//		travelDao.remove(tra);
	}
	
	@Test
	public void testRangeSearch() {
		Session sess = getSession();
		Transaction tx = sess.beginTransaction();

		com.cnpanoramio.domain.Travel tra = new com.cnpanoramio.domain.Travel();
		tra.setAddress("江苏省常州镇111");
		tra.setTitle("常州");
		travelManager.save(tra);
		deleteTravels.add(tra);
		com.cnpanoramio.domain.Travel tra2 = new com.cnpanoramio.domain.Travel();
		tra2.setAddress("无锡2222");
		tra2.setTitle("s");
		travelManager.save(tra2);
		deleteTravels.add(tra2);
		tx.commit();
		
        FullTextSession fullTextSession = Search.getFullTextSession(sess);
        final QueryBuilder mythQB = fullTextSession.getSearchFactory()
        	    .buildQueryBuilder().forEntity( com.cnpanoramio.domain.Travel.class ).get();
        
//        Query luceneQuery = mythQB.phrase().onField("address").sentence("江苏省").createQuery();
        Query luceneQuery = mythQB.keyword().onFields("address", "title")
        	      .matching("江苏省").createQuery();
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery );
        List<com.cnpanoramio.domain.Travel> travels = fullTextQuery.list();
        for(com.cnpanoramio.domain.Travel travel : travels) {
			log.info(travel.getTitle());
			log.info(travel.getAddress());
		}
//		Assert.assertEquals(1, travels.size());
		
		tx = sess.beginTransaction();
		deleteTravel();
		tx.commit();
		sess.close();
	}
	
	public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
	
	@Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	public Session getSession() throws HibernateException {
        Session sess = null;
        try {
        	sess = getSessionFactory().getCurrentSession();
        }catch(Exception ex) {
        }
        if (sess == null) {
            sess = getSessionFactory().openSession();
        }
        return sess;
    }
	
	private void deleteTravel() {
		for(com.cnpanoramio.domain.Travel travel : deleteTravels) {
			travelDao.remove(travel);
		}
	}
}
