package com.azure_connector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.ConnectorKey;
import org.identityconnectors.framework.api.RemoteFrameworkConnectionInfo;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AzureConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzureConnectorApplication.class, args);

		ConnectorInfoManagerFactory fact = ConnectorInfoManagerFactory.getInstance();

		RemoteFrameworkConnectionInfo remoteInfo = new RemoteFrameworkConnectionInfo("localhost", 8759,
				new GuardedString("password".toCharArray()));

		ConnectorInfoManager manager = fact.getRemoteManager(remoteInfo);

		ConnectorKey key = new ConnectorKey("azure_", "1.0-SNAPSHOT", "com.azure_.AzureConnector");
		ConnectorInfo info = manager.findConnectorInfo(key);

		APIConfiguration apiConfig = info.createDefaultAPIConfiguration();

		ConfigurationProperties properties = apiConfig.getConfigurationProperties();

		properties.setPropertyValue("client_id", "client_id");
		properties.setPropertyValue("client_secret", "client_secret");
		properties.setPropertyValue("resource", "https://graph.microsoft.com");
		properties.setPropertyValue("grant_type", "client_credentials");
		properties.setPropertyValue("username", "username");
		properties.setPropertyValue("password", "password");
		properties.setPropertyValue("baseUrl", "baseUrl");

		ConnectorFacade conn = ConnectorFacadeFactory.getInstance().newInstance(apiConfig);

		conn.validate();
		
//		conn.test();
		
//		create(conn);
		
//		update(conn, "4a51ff4f-6f1f-4da6-849a-42208cf4f99c");
		
		delete(conn, "4a51ff4f-6f1f-4da6-849a-42208cf4f99c");
//		conn.delete(ObjectClass.ACCOUNT, new Uid("55020d87-34f5-4791-bbed-20c9e152567e"), null);
	}
	
		public static void create(ConnectorFacade conn) {
			
			Set<Attribute> createAttributes = new HashSet<>();
			
			createAttributes.add(AttributeBuilder.build("displayName", "displayName"));
			createAttributes.add(AttributeBuilder.build("userPrincipalName", "userPrincipalName"));
			createAttributes.add(AttributeBuilder.build("accountEnabled", true));
			createAttributes.add(AttributeBuilder.build("mailNickname", "mailNickname"));
			Map<String, String> map = new HashMap<>();
			map.put("password", "password");
			createAttributes.add(AttributeBuilder.build("passwordProfile", map));
			
			Uid create = conn.create(ObjectClass.ACCOUNT, createAttributes, null);
			
			System.out.println("Create Uid :"+ create);
		}
	
		public static void delete(ConnectorFacade conn, String uid) {
			
			conn.delete(ObjectClass.ACCOUNT, new Uid(uid), null);
			
		}

		
		public static void update(ConnectorFacade conn, String uid) {
		
		Set<Attribute> createAttributes = new HashSet<>();
		
		createAttributes.add(AttributeBuilder.build("displayName", "displayName"));
		
		Uid update = conn.update(ObjectClass.ACCOUNT, new Uid(uid), createAttributes, null);
		
		System.out.println("Update Uid :"+ update);
	}
	
	

}
