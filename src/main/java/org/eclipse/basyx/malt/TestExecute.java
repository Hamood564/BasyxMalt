package org.eclipse.basyx.malt;


import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;

import maltdriver.Malt;
import maltdriver.Response;

public class TestExecute {
	public static void main(String[] args) {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve TestExecute Property
		ISubmodelElement maltExecute = submodel.getSubmodelElement(Server.MALTEXECUTE);
		
		//Print 
		System.out.println(maltExecute.getIdShort() + " is " + maltExecute.getValue());
		
		//Execute the Property
		Malt malt = new Malt("192.168.116.205",5000);
		Response res = malt.start();
		
		System.out.println(res);
		

		// Print value
		System.out.println(maltExecute.getIdShort() + " is " + "Executed");
	}                                                                                                                                         
}
