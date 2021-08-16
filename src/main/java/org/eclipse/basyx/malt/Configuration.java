
package org.eclipse.basyx.malt;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;

/**
 * This class connects to the server created in Server
 * 
 * It retrieves the Submodel and prints the
 * idShort and Value of the contained Property to the console
 * 
 *
 *
 */
public class Configuration {
	public static void main(String[] args) {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve Config Property
		ISubmodelElement maltConfig = submodel.getSubmodelElement(Server.MALTCONFIG);

		// Print value
		System.out.println(maltConfig.getIdShort() + " is " + maltConfig.getValue());
	}
}
