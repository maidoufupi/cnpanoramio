package com.cnpanoramio.domain;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cnpanoramio.MapVendor;

@RunWith(JUnit4.class)
public class UserSettingsTest {
	protected transient final Log log = LogFactory.getLog(getClass());
	
	JAXBContext jc;
	 
	@Before
	public void preMethodSetup() throws JAXBException {
		jc = JAXBContext.newInstance(UserSettings.class);
	}
	
	@After
	public void postMethodTearDown() {
		
	}
	
	@Test
	public void testUserSettingsMarshaller() throws JAXBException {
		UserSettings root = new UserSettings();
		root.setAlertComments(true);
		root.setAlertGroupInvitations(false);
		root.setAlertPhotos(false);
		root.setAllRightsReserved(false);
		root.setHomepageUrl("http://www.baidu.com");
		root.setMapVendor(MapVendor.gaode);
		root.setUser(new User());
		root.setUrlName("http://www.panoramio.cn");
	    root.setCommercialUse(false);
	    root.setDescription("desc");
	    root.setModify(false);

	    Marshaller marshaller = jc.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    marshaller.marshal(root, System.out);
	}
}
