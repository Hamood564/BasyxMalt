package org.eclipse.basyx.malt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;

import maltdriver.Malt;
import maltdriver.Response;
import settings.Param;
import settings.TestSettings;

public class Isolat_delay {
	public static void main(String[] args) {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve Isolate Property
		ISubmodelElement maltIsolate = submodel.getSubmodelElement(Server.MALTISOLATE);
		
		//Print and check condition
		
		
		System.out.println(maltIsolate.getIdShort() + " is " + maltIsolate.getValue());
		Object CheckCondition_Object = maltIsolate.getValue();
		String CheckCondition = CheckCondition_Object.toString();
		//Execute the Property
		
		if (CheckCondition.equals("TRUE")) {

			Malt malt = new Malt(Server.IP,Server.Port);
			malt.selectTest(Server.testindex);
			TestSettings ts = new TestSettings(Server.testindex);
			Response set = malt.requestTestSettings(Server.testindex);
			//creating local copy as an object
			TestSettings localCopy = new TestSettings(set.getMessage());
			Object val = Server.isolateexpression;
			System.out.println(val);
			//get() method to retrieve field and covert to type
		    int isolate = Integer.parseInt(localCopy.get(Param.isolationdelay)); //Modify to better the code currently too crude
		    // fill should be used here to import variable locally and then modify the object val expresion
		    //maybe lesz could help
		    System.out.println(isolate);
			while (!malt.isReady()) {
				try {
					Thread.sleep(200);
				}catch(InterruptedException e) {
					
				}
			}
			
			
			String value = val.toString();
			ts.set(Param.isolationdelay, value);
			Response res = malt.settings(ts);
			System.out.println(res);
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
			Response ressave = malt.exportSettings(fileName);
			System.out.println(ressave);
			System.out.println("Isolation Delay updated...");
			res = malt.disconnect();
			System.out.println(res);
			System.out.println(malt.getConnectionResponse());
				
		}else {
			System.out.println("No capability to execute");
		}
		
		

		// Print finished state
		System.out.println(maltIsolate.getIdShort() + " is " + "Executed");	
	

	
}

}
