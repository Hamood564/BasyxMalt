package XMPP;


import java.util.Set;

import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPI;
import org.eclipse.basyx.submodel.restapi.api.ISubmodelAPIFactory;
import org.eclipse.basyx.submodel.restapi.vab.VABSubmodelAPI;
import org.eclipse.basyx.vab.modelprovider.api.IModelProvider;
import org.eclipse.basyx.vab.modelprovider.lambda.VABLambdaProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMPPSubmodelAPIFactory implements ISubmodelAPIFactory{
	private static Logger logger = LoggerFactory.getLogger(XMPPSubmodelAPIFactory.class);
	
	private BaSyxXMPPConfiguration config;
	
	/**
	 * Constructor with XMPP configuration for providing submodel APIs
	 * 
	 * @param config
	 */
	public XMPPSubmodelAPIFactory(BaSyxXMPPConfiguration config) {
		this.config = config;
	}
	
	@Override
	public ISubmodelAPI getSubmodelAPI(Submodel submodel) {
		// Get the submodel's id from the given provider
		String submodelId = submodel.getIdentification().getId();
		
		// Create the API
		IModelProvider provider = new VABLambdaProvider(submodel);
		VABSubmodelAPI observedApi = new VABSubmodelAPI(provider);
		// Configure the API according to the given configs
		String brokerEndpoint = config.getServer();
		String clientId = submodelId;
		XMPPSubmodelAPI api;
		try {
			if (config.getUser() != null) {
				String user = config.getUser();
				String pass = config.getPass();
				api = new XMPPSubmodelAPI(observedApi, brokerEndpoint, clientId, user, pass);
			} else {
				api =new XMPPSubmodelAPI(observedApi, brokerEndpoint, clientId);
			}
			setWhitelist(api,submodelId );
		} catch (Exception e) {
			logger.error("Could not create XMPPSubmodelApi", e);
			return observedApi;
		}
		return api;
	}

	private void setWhitelist(XMPPSubmodelAPI api, String submodelId) {
		if (!config.isWhitelistEnabled(submodelId)) {
			// Do not use the whitelist if it has been disabled
			api.disableWhitelist();
			return;
		}

		// Read whitelist from configuration
		Set<String> whitelist = config.getWhitelist(submodelId);

		logger.info("Set MQTT whitelist for " + submodelId + " with " + whitelist.size() + " entries");
		api.setWhitelist(whitelist);
		api.enableWhitelist();
	}
		
}




