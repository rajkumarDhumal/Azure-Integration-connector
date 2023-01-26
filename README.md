# Azure-Integration-connector
connector create, update, delete Azure account using connid connector server.

Azure Integration Connector for ConnID Connector Server
This project is an integration connector for the ConnID Connector Server that allows for seamless integration with Azure services.

Requirements
* ConnID Connector Server
* Azure account with access to Azure Active Directory 

Installation
1. Clone the repository to your computer.
2. In Azure_ file run the command mvn clean install to build the connector bundle.
3. Copy the resulting .jar file from the target directory to the bundles directory of your ConnID Connector Server.
4. Fill in the following fields in azure_connector(AzureConnectorApplication) with your Azure account information into configuration properties:
    azure.client.id
    azure.client.secret
    azure.tenant.id 
    username
    password
    baseURL.
    also insert serever port number and password.
5. Save and close the files.
6. Restart the ConnID Connector Server for the changes to take effect.
7. run spring boot azure_connector to validate, test, create, update, delete account.
  
  

Configuration
Once the connector is installed, you can configure it through the ConnID Connector Server's management interface.

Usage
The connector can be used tovalidate, test, create, update, delete account.
