package org.eclipse.basyx.malt;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;

public class Caliberate {
	
	public static void main(String[] args) {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve Caliberation Property
		ISubmodelElement maltCaliberate = submodel.getSubmodelElement(Server.MALTCALIBERATE);

		// Print value
		System.out.println(maltCaliberate.getIdShort() + " is " + maltCaliberate.getValue());
	}

}

