package com.azure_.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import com.azure_.AzureConfiguration;
import com.azure_.service.AuthTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTokenServiceImpl implements AuthTokenService{
	
	private static final Log LOG = Log.getLog(AuthTokenServiceImpl.class);

	private final AzureConfiguration configuration;

	public AuthTokenServiceImpl(AzureConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public String getToken() {
		String authToken= null;
		
		String oldUrl = getUrl();
		
		StringBuilder newUrl =new StringBuilder();
		newUrl.append(oldUrl);
		
		LOG.info("Calling URL to generate TOKEN {0}", newUrl);
		authToken=this.getAuthToken(newUrl.toString());
		
		return authToken;
	}

	private String getAuthToken(String url) {
		String token = null;
		try {
			String client_id = this.configuration.getClient_id();
			String client_secret = this.configuration.getClient_secret();
			String grant_type = this.configuration.getGrant_type();
			String password = this.configuration.getPassword();
			String resource = this.configuration.getResource();
			String username = this.configuration.getUsername();
			
			LOG.info(" remote username {0} ", username);
			
			List < NameValuePair > form = new ArrayList < > ();
		    form.add(new BasicNameValuePair("client_id", client_id));
		    form.add(new BasicNameValuePair("client_secret", client_secret));
		    form.add(new BasicNameValuePair("resource", resource));
		    form.add(new BasicNameValuePair("grant_type", grant_type));
            form.add(new BasicNameValuePair("username", username));
            form.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

            HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);

			int statusCode = response.getStatusLine().getStatusCode();
			InputStream content = response.getEntity().getContent();

			LOG.info("Status {0} ", statusCode);

			if (statusCode == HttpStatus.SC_OK) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> jsonMap = mapper.readValue(content, Map.class);

				 token = (String) jsonMap.get("access_token");
			}
			else {
				handleGeneralError("Some error occured while generating token. Status is " + String.valueOf(statusCode));
			}
            
		} catch (Exception e) {
			LOG.error(e.getMessage());
			handleGeneralError(e.getMessage(), e);
		}
		return token;
	}

	@Override
	public String getUrl() {
		String baseUrl =this.configuration.getBaseUrl();
		LOG.info("calling base URL {0}", baseUrl);
		
		return baseUrl;
	}
	private static void handleGeneralError(final String message, final Exception ex) {
		LOG.error(ex, message);
		throw new ConnectorException(message, ex);
	}
	
	private static void handleGeneralError(final String message) {
        LOG.error("General error : {0}", message);
        throw new ConnectorException(message);
	
	
	
}
}
