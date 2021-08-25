package org.eclipse.basyx.malt;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;

public class Test_result implements Runnable{
	
	@Override
	public void run() {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve Result Property
		ISubmodelElement maltResult = submodel.getSubmodelElement(Server.MALTRESULT);
		ISubmodelElement maltLastdata = submodel.getSubmodelElement(Server.MALTLASTDATA);

		// Print value
		System.out.println(maltResult.getIdShort() + " is " + maltResult.getValue());
		System.out.println(maltLastdata.getIdShort() + " is " + maltLastdata.getValue());

	}
	

}
