package com.azure_.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Uid;

import com.azure_.AzureConfiguration;
import com.azure_.service.impl.AuthTokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


public class UserClient {
	
	private static final Log LOG = Log.getLog(UserClient.class);

	private AzureConfiguration config;
	
	public UserClient(final AzureConfiguration config) {
		this.config = config;
	}
	
	public String createUser(final Map<String, Object> createAttributes) {
		
		AuthTokenService authTokenService = new AuthTokenServiceImpl(config);
		String id = "";

		try {
			String endpointPath = "https://graph.microsoft.com/v1.0/users";
			LOG.info("Calling URL for create user {0}", endpointPath);
			
			String token = authTokenService.getToken();
			LOG.info("Token for request {0}", token);
			
			LOG.info("Request for create user {0}", createAttributes.toString());

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(endpointPath);
			request.setHeader("Authorization", "Bearer "+token);
			request.setHeader("Content-Type", "application/json");
			
			LOG.info(" Request Headers- {0}", request.getAllHeaders());
			
			ObjectMapper objectMapper = new ObjectMapper();
	        String requestBody = objectMapper.writeValueAsString(createAttributes);
	        
	        LOG.info("Request Body {0}", requestBody);
			
			request.setEntity(new StringEntity(requestBody));
			HttpResponse response = httpClient.execute(request);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			InputStream content = null;
			
			if(ObjectUtils.isNotEmpty(entity)) {
				content = response.getEntity().getContent();
			}

			LOG.info("Status: {0} ", statusCode);

			if (statusCode == 201) {
				ObjectMapper objectMapper1 = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper1.readValue(content, Map.class);

				id = (String) jsonMap.get("id");
				
			}
			else {
				handleGeneralError("Some error occured while creating user. " + String.valueOf(response));
				handleGeneralError("Some error occured while creating user. Status- " + String.valueOf(statusCode));
				
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			handleGeneralError(e.getMessage(), e);
		}
		
		return id;
	}
	
	public String updateUser(final Map<String, Object> replacedAttribute, final Uid uid) {
		
		AuthTokenService authTokenService = new AuthTokenServiceImpl(config);
		String id = "";

		try {
			String endpointPath = "https://graph.microsoft.com/v1.0/users/"+ uid.getUidValue();
			LOG.info("Calling URL for create user {0}", endpointPath);
			
			String token = authTokenService.getToken();
			LOG.info("Token for request- {0}", token);
			
			LOG.info("Request for create user {0}", replacedAttribute.toString());
		
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPatch request = new HttpPatch(endpointPath);
			request.setHeader("Authorization", "Bearer "+token);
			request.setHeader("Content-Type", "application/json");
			
			LOG.info("Request Headers {0}", request.getAllHeaders());
			
			ObjectMapper objectMapper = new ObjectMapper();
	        String requestBody = objectMapper.writeValueAsString(replacedAttribute);
	        
	        LOG.info("Request Body {0}", requestBody);
			
			request.setEntity(new StringEntity(requestBody));
			HttpResponse response = httpClient.execute(request);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			InputStream content = null;
			
			if(ObjectUtils.isNotEmpty(entity)) {
				content = response.getEntity().getContent();
			}

			LOG.info("Status {0} ", statusCode);

			if (statusCode == 201) {
				ObjectMapper objectMapper1 = new ObjectMapper();
				Map<String, Object> jsonMap = objectMapper1.readValue(content, Map.class);

				id = (String) jsonMap.get("id");
				
			}
			else {
				handleGeneralError("Some error occured while create user. " + String.valueOf(response));
				handleGeneralError("Some error occured while create user. Status is " + String.valueOf(statusCode));
				
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			handleGeneralError(ex.getMessage(), ex);
		}
		
		return id;
	}
	
	public void deleteUser(final Uid uid) {
		
		AuthTokenService authTokenService = new AuthTokenServiceImpl(config);


		try {
			String endpointPath = "https://graph.microsoft.com/v1.0/users/"+ uid.getUidValue();
			LOG.info("Calling URL for delete user {0}", endpointPath);
			
			String token = authTokenService.getToken();
			LOG.info("Token for request {0}", token);
		
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpDelete request = new HttpDelete(endpointPath);
			request.setHeader("Authorization", "Bearer "+token);
			
			HttpResponse response = httpClient.execute(request);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			InputStream content = null;
			
			if(ObjectUtils.isNotEmpty(entity)) {
				content = response.getEntity().getContent();
			}

			LOG.info("Status {0} ", statusCode);

			if (statusCode == 201) {
				LOG.info("Successfully deleted");
				
			}
			else {
				handleGeneralError("Some error occured while delete user. " + String.valueOf(response));
				handleGeneralError("Some error occured while delete user. Status is " + String.valueOf(statusCode));
				
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			handleGeneralError(ex.getMessage(), ex);
		}
		
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