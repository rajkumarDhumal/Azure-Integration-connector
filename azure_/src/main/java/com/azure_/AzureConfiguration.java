package com.azure_;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class AzureConfiguration extends AbstractConfiguration {

    private String client_id;
    
    private String client_secret;
    
    private String resource;
    
    private String grant_type;
    
    private String username;
    
    private String password;
    
    private String baseUrl;

    public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@ConfigurationProperty(order = 1)
	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	@ConfigurationProperty(order = 2)
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@ConfigurationProperty(order = 3)
	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	@ConfigurationProperty(order = 4)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@ConfigurationProperty(order = 5)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ConfigurationProperty(order = 6)
    public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
    public void validate() {
        if (StringUtil.isBlank(client_id)) {
            throw new ConfigurationException("Client Id must not be blank!");
        }
        
        if (StringUtil.isBlank(client_secret)) {
            throw new ConfigurationException("Client Secret must not be blank!");
        }
        
        if (StringUtil.isBlank(resource)) {
            throw new ConfigurationException("Resource must not be blank!");
        }
        
        if (StringUtil.isBlank(grant_type)) {
            throw new ConfigurationException("Grant type must not be blank!");
        }
        
        if (StringUtil.isBlank(username)) {
            throw new ConfigurationException("Username must not be blank!");
        }
        
        if (StringUtil.isBlank(password)) {
            throw new ConfigurationException("Password must not be blank!");
        }
        
        if (StringUtil.isBlank(baseUrl)) {
            throw new ConfigurationException("BaseUrl must not be blank!");
        }
    }

}
