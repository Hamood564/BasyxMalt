
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
import settings.CalibrationSettings.Channel;
import settings.Param;
import settings.TestSettings;

/**
 * This class starts an AAS server and a Registry server
 * 
 * An AAS and Submodels containing Properties and variables
 * is uploaded to the AAS server and registered in the Registry server.
 * 
 * 
 *
 */
public class Server {
	
	//MALT Variables
	public static final String IP = "192.168.116.205";
	public static final Integer Port = 5000;
	public static final Integer testindex = 0;
		
	
	//Conditions
	private static String EXECUTECONDITIONS = "TRUE"; //Depends on capability to execute TRUE or FALSE
	private static String FILLCONDITION = "TRUE";
	private static String ISOLATECONDITION = "TRUE";
	private static String MEASURECONDITION = "TRUE";
	private static String TESTPRESCONDITION = "TRUE";
	private static String STABCONDITION = "TRUE";
	private static String VENTDELAYCONDITION = "TRUE";
	private static String OFFSETCONDITION = "TRUE";
	private static String ALARMLEAKCONDITION = "TRUE";
	private static String ALARMDIFFCONDITION = "TRUE";
	
	
    //Creating a data twin
	static Malt malt = new Malt(IP,Port);
	static Response set = malt.requestTestSettings(testindex);
	static TestSettings localCopy = new TestSettings(set.getMessage());
	
	
	 //Expressions
	static int fill =Integer.parseInt(localCopy.get(Param.filltime));
	public static final Object fillexpression = 200 + fill ;
	
	static int isolate = Integer.parseInt(localCopy.get(Param.isolationdelay));
	public static final Object isolateexpression = 1000 + isolate; 
	
	
	static int measure = Integer.parseInt(localCopy.get(Param.measuretime));
	public static final Object measureexpression = 1000 + measure;
	
	static int testpres = Integer.parseInt(localCopy.get(Param.testpressure));
	public static final Object testpresexpression = 1000 + testpres;
	
	static int stab = Integer.parseInt(localCopy.get(Param.stabilisationtime));
	public static final Object stabexpression = 1000 + stab;
		
	static int vent_delay = Integer.parseInt(localCopy.get(Param.ventoffdelay));
	public static final Object ventexpression = 1000 + vent_delay;
	
	static int offset = Integer.parseInt(localCopy.get(Param.offsetcomp));
	public static final Object offsetexpression = 10 + offset;
	
	static int alarmleak = Integer.parseInt(localCopy.get(Param.alarmleakrate));
	public static final Object alarmleakexpression = 10 + alarmleak;
	
	static int alarmdiff = Integer.parseInt(localCopy.get(Param.alarmdiffp));
	public static final Object alarmdiffexpression = 10 + alarmdiff;
	
	
	
	// Server URLs
	public static final String REGISTRYPATH = "http://localhost:4000/registry";
	public static final String AASSERVERPATH = "http://localhost:4001/aasServer";

	// AAS/Submodel/Property Ids
	public static final IIdentifier MALTAASID = new CustomId("eclipse.basyx.aas.malt");
	public static final IIdentifier DOCUSMID = new CustomId("eclipse.basyx.submodel.documentation");
	public static final String MALTCONFIG = "maltConfig";
	public static final String MALTCALIBERATE = "maltCaliberate";
	public static final String MALTOPTIONS = "maltOptions";
	public static final String MALTEXECUTE = "maltExecute";
	public static final String MALTFILL = "maltFill";
	public static final String MALTISOLATE = "maltIsolate";
	public static final String MALTMEASURE= "maltMeasure";
	public static final String MALTTESTPRES= "maltTestpres";
	public static final String MALTSTAB= "maltStab";
	public static final String MALTVENTDELAY= "maltVentdelay";
	public static final String MALTOFFSET= "maltOffset";
	public static final String MALTALARMLEAK= "maltAlarmleak";
	public static final String MALTALARMDIFF= "maltAlarmdiff";
	
	
	
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
		
		
		//calibrations
		List<String> caliberate = new ArrayList<String>();
		for (Channel ch : Channel.values()) {
			res = malt.requestCalibrationSettings(ch);
			System.out.println(res);
			String res2 = res.toString();
			caliberate.add(res2);
		}
		String caliberation =caliberate.toString();
		
		//options
		res = malt.requestOptionSettings();
		System.out.println(res); 
		String options = res.toString();
		
		
		
		
		// - Create malt config property
		Property maltConfig = new Property(MALTCONFIG, configuration);
		// Create malt caliberate property
		Property maltCaliberate = new Property(MALTCALIBERATE, caliberation);
		//Create malt options property
		Property maltOptions= new Property(MALTOPTIONS, options);
		
		res = malt.disconnect();
		System.out.println(res);
		System.out.println(malt.getConnectionResponse());
		//create malt execute property	
		Property maltExecute = new Property(MALTEXECUTE, EXECUTECONDITIONS);
		//create malt fill property
		Property maltFill = new Property(MALTFILL,FILLCONDITION);
		//create malt isolation delay property
		Property maltIsolate = new Property(MALTISOLATE, ISOLATECONDITION);
		//create malt measure property
		Property maltMeasure = new Property(MALTMEASURE, MEASURECONDITION);
		//create malt testpres property
		Property maltTestpres = new Property(MALTTESTPRES, TESTPRESCONDITION);				
		//create malt stab property
		Property maltStab = new Property(MALTSTAB, STABCONDITION);	
		//create malt vent_delay property
		Property maltVentdelay = new Property(MALTSTAB, VENTDELAYCONDITION);
		//create malt offset property
		Property maltOffset = new Property(MALTOFFSET, OFFSETCONDITION);
		//create malt alarm rate property
		Property maltAlarmleak = new Property(MALTALARMLEAK, ALARMLEAKCONDITION); 	
		//create malt alarm diff property
		Property maltAlarmdiff = new Property(MALTALARMDIFF, ALARMDIFFCONDITION); 	

		// Add the properties to the Submodel
		documentationSubmodel.addSubmodelElement(maltConfig);
		documentationSubmodel.addSubmodelElement(maltCaliberate);
		documentationSubmodel.addSubmodelElement(maltOptions);
		documentationSubmodel.addSubmodelElement(maltExecute);
		documentationSubmodel.addSubmodelElement(maltFill);
		documentationSubmodel.addSubmodelElement(maltIsolate);
		documentationSubmodel.addSubmodelElement(maltMeasure);
		documentationSubmodel.addSubmodelElement(maltTestpres);
		documentationSubmodel.addSubmodelElement(maltStab);
		documentationSubmodel.addSubmodelElement(maltVentdelay);
		documentationSubmodel.addSubmodelElement(maltOffset);
		documentationSubmodel.addSubmodelElement(maltAlarmleak);
		documentationSubmodel.addSubmodelElement(maltAlarmdiff);
				
	

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
	 * Startup an server at "http://localhost:4001/"
	 */
	private static void startAASServer() {
		BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(4001, "/aasServer");
		BaSyxAASServerConfiguration aasServerConfig = new BaSyxAASServerConfiguration(AASServerBackend.INMEMORY, "", REGISTRYPATH);
		AASServerComponent aasServer = new AASServerComponent(contextConfig, aasServerConfig);

		// Start the created server
		aasServer.startComponent();
	}
}

