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
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AzureConnectorApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(AzureConnectorApplication.class, args);

		ConnectorInfoManagerFactory fact = ConnectorInfoManagerFactory.getInstance();

		RemoteFrameworkConnectionInfo remoteInfo = new RemoteFrameworkConnectionInfo("localhost", 8759,
				new GuardedString("rajkumar".toCharArray()));

		ConnectorInfoManager manager = fact.getRemoteManager(remoteInfo);

		ConnectorKey key = new ConnectorKey("slack", "1.0-SNAPSHOT", "slack.SlackConnector");
		ConnectorInfo info = manager.findConnectorInfo(key);

		APIConfiguration apiConfig = info.createDefaultAPIConfiguration();

		ConfigurationProperties properties = apiConfig.getConfigurationProperties();

		properties.setPropertyValue("client_id", "4227951990963.4251736143392");
		properties.setPropertyValue("token", "xoxe.xoxp-1-Mi0yLTQyMjc5NTE5OTA5NjMtNDI1MTczMzc5MDI0MC00MjEzODc0NDYyNDU1LTQyOTI3MTcxOTQzNTctZjZmZGQ1N2NjNzhhMjMxODRkZGM1NDM1MzExYmUyYzI2ZjgwZDAyNmQ4MDIyZmZkNDI1Zjc1ODU0OWNjNTc3ZA");
		
		ConnectorFacade conn = ConnectorFacadeFactory.getInstance().newInstance(apiConfig);

//		conn.validate();
		
//		conn.test();
		
		create(conn);
		
//		update(conn, "4a51ff4f-6f1f-4da6-849a-42208cf4f99c");
		
//		delete(conn, "4a51ff4f-6f1f-4da6-849a-42208cf4f99c");
//		conn.delete(ObjectClass.ACCOUNT, new Uid("55020d87-34f5-4791-bbed-20c9e152567e"), null);
	}
	
		public static void create(ConnectorFacade conn) {
			
			Set<Attribute> createAttributes = new HashSet<>();
			
			createAttributes.add(AttributeBuilder.build("App_name", "rr"));
			
			
			
			Uid create = conn.create(ObjectClass.ACCOUNT, createAttributes, null);
			
			System.out.println("Create Uid :"+ create);
		}

}
