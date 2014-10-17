package com.cnpanoramio.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Travel;
import com.cnpanoramio.domain.TravelSpot;

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
@Transactional
public class TravelManagerTest {

	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private PhotoManager photoManager;

	@Autowired
	private TravelManager travelManager;
	@Autowired
	private TravelDao travelDao;
	
	@Resource
    private SessionFactory sessionFactory;
	
	private User user;
	
	private List<Travel> travels = new ArrayList<Travel>();
	
	private Travel travel;
	
	@Before
	public void preMethodSetup() {
		user = new User();
		user.addRole(roleManager.getRole(Constants.USER_ROLE));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user", "user", user.getAuthorities());
        auth.setDetails(user);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		travel = new Travel();
		travel.setAddress("江苏省常州镇111");
		travel.setTitle("江苏省常州");
		travelDao.persist(travel);
		travels.add(travel);

	}
	
	@After
	public void postMethodTearDown() {
		for(Travel travel : travels) {
			travelDao.remove(travel);
		}
	}
	
	@Test
	public void testAddTravelPhoto() {
		
		Photo photo = photoManager.get(3L);
		
		TravelSpot travelSpot = new TravelSpot();
		travelManager.createTravelSpot(travel.getId(), travelSpot);
		photo = travelManager.addTravelPhoto(travel, photo);
				
		for(TravelSpot spot : travel.getSpots()) {
			log.info(spot.getTimeStart());
		}
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

}
