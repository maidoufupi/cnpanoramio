package com.cnpanoramio.rest;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.cnpanoramio.json.PhotoComments;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class CommentRestServiceTest {

	protected transient final Log log = LogFactory.getLog(getClass());

	private MockRestServiceServer mockServer;

	private RestTemplate restTemplate;

	@Before
	public void preMethodSetup() {
		this.restTemplate = new RestTemplate();
		this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
	}

	@After
	public void postMethodTearDown() {
	}

	@Test
	public void testSaveComment() {
		String responseBody = "{\"id\" : \"1\", \"count\" : \"1\"}";

		this.mockServer.expect(requestTo("/api/rest/comment/photo/2/10/1")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

		@SuppressWarnings("unused")
		PhotoComments ludwig = restTemplate.getForObject("/api/rest/comment/photo/{photoId}/{pageSize}/{pageNo}", PhotoComments.class, 2, 10, 1);
		
		log.info(ludwig.getId());
		this.mockServer.verify();
	}
}
