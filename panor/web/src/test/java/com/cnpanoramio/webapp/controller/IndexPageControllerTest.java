package com.cnpanoramio.webapp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.appfuse.Constants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class IndexPageControllerTest extends BaseControllerTestCase {

	 @Autowired
	 private IndexPageController c;
	 
	 @Test
	    public void testHandleRequest() throws Exception {
	        ModelAndView mav = c.handleRequest(null, null);
	        Map m = mav.getModel();
	        assertNotNull(m.get(Constants.USER_LIST));
	        assertEquals("admin/userList", mav.getViewName());
	    }
}
