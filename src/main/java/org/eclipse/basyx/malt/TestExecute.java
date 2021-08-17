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
		
		//Print and check condition
		
		
		System.out.println(maltExecute.getIdShort() + " is " + maltExecute.getValue());
		Object CheckCondition_Object = maltExecute.getValue();
		String CheckCondition = CheckCondition_Object.toString();
		//Execute the Property
		
		if (CheckCondition.equals("TRUE")) {

			Malt malt = new Malt(Server.IP,Server.Port);
			Response res = malt.start();	
			System.out.println(res);
		}else {
			System.out.println("No capability to execute");
		}
		
		
		

		// Print value
		System.out.println(maltExecute.getIdShort() + " is " + "Executed");
	}                                                                                                                                         
}
