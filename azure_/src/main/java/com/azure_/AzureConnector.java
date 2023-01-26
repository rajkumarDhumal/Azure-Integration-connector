package com.azure_;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.operations.ResolveUsernameApiOp;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.AuthenticateOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

import com.azure_.service.AuthTokenService;
import com.azure_.service.UserClient;
import com.azure_.service.impl.AuthTokenServiceImpl;



/**
 * This sample connector provides (empty) implementations for all ConnId operations, but this is not mandatory: any
 * connector can choose which operations are actually to be implemented.
 */
@ConnectorClass(configurationClass = AzureConfiguration.class, displayNameKey = "azure.connector.display")
public class AzureConnector implements Connector,
        CreateOp, UpdateOp, UpdateAttributeValuesOp, DeleteOp,
        AuthenticateOp, ResolveUsernameApiOp, TestOp {

    private static final Log LOG = Log.getLog(AzureConnector.class);

    private AzureConfiguration configuration;
    
	private AuthTokenService authTokenService;
	
	private UserClient userClient;

    @Override
    public AzureConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(final Configuration configuration) {
        this.configuration = (AzureConfiguration) configuration;
        LOG.ok("Connector {0} successfully inited", getClass().getName());
        
        authTokenService = new AuthTokenServiceImpl(this.configuration);
        
        userClient = new UserClient(this.configuration);

		LOG.info("Connector {0} successfully inited", getClass().getName());
    }

    @Override
    public void dispose() {
        // dispose of any resources the this connector uses.
    }

    @Override
    public Uid create(
            final ObjectClass objectClass,
            final Set<Attribute> createAttributes,
            final OperationOptions options) {

    	if (createAttributes == null || createAttributes.isEmpty()) {
			throw new ConnectorException("Attribute values are null or empty");
		}

		if (ObjectClass.ACCOUNT.equals(objectClass)) {

			try {
				
				Map<String, Object> userRequest = getUserRequest(createAttributes);
				
				System.out.println(userRequest);
				
				String createUser = userClient.createUser(userRequest);

				return new Uid(createUser);
			} catch (Exception e) {
				throw new ConnectorException("Could not create User: " + e.getMessage(), e);
			}
		} else {
			LOG.warn("Create of type {0} is not supported", objectClass.getObjectClassValue());
			throw new UnsupportedOperationException(
					"Create of type" + objectClass.getObjectClassValue() + " is not supported");
		}
    }
    
    @Override
    public Uid update(
            final ObjectClass objectClass,
            final Uid uid,
            final Set<Attribute> replaceAttributes,
            final OperationOptions options) {
    	
    	if (replaceAttributes == null || replaceAttributes.isEmpty()) {
			throw new ConnectorException("Attribute values are null or empty");
		}
    	
    	if (ObjectClass.ACCOUNT.equals(objectClass)) {

			try {
				
				Map<String, Object> userRequest = getUserRequest(replaceAttributes);
				
				System.out.println(userRequest);
				
				String updateUser = userClient.updateUser(userRequest, uid);
				
				System.out.println("Update user return Uid "+ updateUser);

				
			} catch (Exception e) {
				throw new ConnectorException("Could not update User: " + e.getMessage(), e);
			}
		} else {
			LOG.warn("update of type {0} is not supported", objectClass.getObjectClassValue());
			throw new UnsupportedOperationException(
					"update of type" + objectClass.getObjectClassValue() + " is not supported");
		}

        return uid;
    }

    @Override
    public Uid addAttributeValues(
            final ObjectClass objclass,
            final Uid uid,
            final Set<Attribute> valuesToAdd,
            final OperationOptions options) {

        return uid;
    }

    @Override
    public Uid removeAttributeValues(
            final ObjectClass objclass,
            final Uid uid,
            final Set<Attribute> valuesToRemove,
            final OperationOptions options) {

        return uid;
    }

    @Override
    public void delete(
            final ObjectClass objectClass,
            final Uid uid,
            final OperationOptions options) {
    	
    	if (uid == null || uid.toString().isEmpty()) {
			throw new ConnectorException("Set of Attributes value is null or empty");
		}
    	
    	if (ObjectClass.ACCOUNT.equals(objectClass)) {

			try {
				
				userClient.deleteUser(uid);
				

			} catch (Exception e) {
				throw new ConnectorException("Could not delete User: " + e.getMessage(), e);
			}
		} else {
			LOG.warn("delete of type {0} is not supported", objectClass.getObjectClassValue());
			throw new UnsupportedOperationException(
					"delete of type" + objectClass.getObjectClassValue() + " is not supported");
		}
    }

    @Override
    public Uid authenticate(
            final ObjectClass objectClass,
            final String username,
            final GuardedString password,
            final OperationOptions options) {

        return new Uid(username);
    }

    @Override
    public Uid resolveUsername(
            final ObjectClass objectClass,
            final String username,
            final OperationOptions options) {

        return new Uid(username);
    }

    @Override
    public void test() {
  
		LOG.info("Connector: {0} successfully tested.", getClass().getName());

		LOG.info("Token: {0}", this.authTokenService.getToken());
    }
    
    private Map<String, Object> getUserRequest(final Set<Attribute> attributes) {

		Map<String, Object> map = new HashMap<>();
		for (Attribute attribute : attributes) {
			if (Objects.nonNull(attribute) && ObjectUtils.isNotEmpty(attribute.getValue())) {
				
					map.put(attribute.getName(), attribute.getValue().get(0));
				
			}
		}

		return map;
	}
}
