
package org.eclipse.basyx.malt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.CustomId;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.AASServerBackend;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.components.registry.RegistryComponent;
import org.eclipse.basyx.components.registry.configuration.BaSyxRegistryConfiguration;
import org.eclipse.basyx.components.registry.configuration.RegistryBackend;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;

import maltdriver.Malt;
import maltdriver.Response;

/**
 * This class starts an AAS server and a Registry server
 * 
 * An AAS and a Submodel containing a Property "maxTemp"
 * is uploaded to the AAS server and registered in the Registry server.
 * 
 * 
 *
 */
public class Server {
	
	public static final String IP = "192.168.116.205";
	public static final Integer Port = 5000;
		
	
	//Conditions
	private static String EXECUTECONDITIONS = "TRUE"; //Depends on capability to execute TRUE or FALSE
	
	// Server URLs
	public static final String REGISTRYPATH = "http://localhost:4000/registry";
	public static final String AASSERVERPATH = "http://localhost:4001/aasServer";

	// AAS/Submodel/Property Ids
	public static final IIdentifier MALTAASID = new CustomId("eclipse.basyx.aas.malt");
	public static final IIdentifier DOCUSMID = new CustomId("eclipse.basyx.submodel.documentation");
	public static final String MALTCONFIG = "maltConfig";
	public static final String MALTEXECUTE = "maltExecute";

	
	public static void main(String[] args) {
		// Create Infrastructure
		startRegistry();
		startAASServer();
		
		Malt malt = new Malt(IP,Port);
		Response connres = malt.getConnectionResponse();
		System.out.println(connres);
		System.out.println(malt.getHost()+ ":"+ malt.getPort()+ ","+malt.isReady());
		
		Response res = null;
		

		// Create Manager - This manager is used to interact with an AAS server
		ConnectedAssetAdministrationShellManager manager = 
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(REGISTRYPATH));
		
		// Create AAS and push it to server
		Asset asset = new Asset("maltAsset", new CustomId("eclipse.basyx.asset.malt"), AssetKind.INSTANCE);
		AssetAdministrationShell shell = new AssetAdministrationShell("malt", MALTAASID, asset);
		
		// The manager uploads the AAS and registers it in the Registry server
		manager.createAAS(shell, AASSERVERPATH);
		
		// Create submodel
		Submodel documentationSubmodel = new Submodel("documentationSm", DOCUSMID);
		
		List<String> testconfig = new ArrayList<String>();
		for (int i=0; i<16; i++) {
			res = malt.requestTestSettings(i);
			String res2 = res.toString();
			System.out.println(res2);
			testconfig.add(res2);
		
			
		}
//		
		
		String configuration =testconfig.toString();
		// - Create malt config property
		Property maltConfig = new Property(MALTCONFIG, configuration);
		
		res = malt.disconnect();
		System.out.println(res);
		System.out.println(malt.getConnectionResponse());
		//create malt execute property
		
		
		Property maltExecute = new Property(MALTEXECUTE, EXECUTECONDITIONS);
		
		

		// Add the property to the Submodel
		documentationSubmodel.addSubmodelElement(maltConfig);
		documentationSubmodel.addSubmodelElement(maltExecute);

		// - Push the Submodel to the AAS server
		manager.createSubmodel(shell.getIdentification(), documentationSubmodel);
	}

	/**
	 * Starts an registry at "http://localhost:4000"
	 */
	private static void startRegistry() {
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(4000, "/registry");
		BaSyxRegistryConfiguration registryConfig = new BaSyxRegistryConfiguration(RegistryBackend.INMEMORY);
		RegistryComponent registry = new RegistryComponent(contextConfig, registryConfig);

		// Start the created server
		registry.startComponent();
	}

	/**
	 * Startup an  server at "http://localhost:4001/"
	 */
	private static void startAASServer() {
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(4001, "/aasServer");
		BaSyxAASServerConfiguration aasServerConfig = new BaSyxAASServerConfiguration(AASServerBackend.INMEMORY, "", REGISTRYPATH);
		AASServerComponent aasServer = new AASServerComponent(contextConfig, aasServerConfig);

		// Start the created server
		aasServer.startComponent();
	}
}
