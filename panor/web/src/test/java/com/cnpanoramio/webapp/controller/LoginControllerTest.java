package com.cnpanoramio.webapp.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class LoginControllerTest extends BaseControllerTestCase {

    @Autowired
    private LoginController loginController;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testHandleRequest() throws Exception {
        String view = loginController.handleRequest(null, null);
        assertEquals("", "/index.jsp", view);
    }
}