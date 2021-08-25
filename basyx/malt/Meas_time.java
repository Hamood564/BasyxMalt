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

public class Meas_time implements Runnable{
	@Override
	public void run() {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve Measure Property
		ISubmodelElement maltMeasure = submodel.getSubmodelElement(Server.MALTMEASURE);
		
		//Print and check condition
		
		
		System.out.println(maltMeasure.getIdShort() + " is " + maltMeasure.getValue());
		Object CheckCondition_Object = maltMeasure.getValue();
		String CheckCondition = CheckCondition_Object.toString();
		//Execute the Property
		
		if (CheckCondition.equals("TRUE")) {

			Malt malt = new Malt(Server.IP,Server.Port);
			malt.selectTest(Server.testindex);
			TestSettings ts = new TestSettings(Server.testindex);
			Response set = malt.requestTestSettings(Server.testindex);
			//creating local copy as an object
			TestSettings localCopy = new TestSettings(set.getMessage());
			Object val = Server.measureexpression;
			System.out.println(val);
			//get() method to retrieve field and covert to type
		    int measure = Integer.parseInt(localCopy.get(Param.measuretime)); 
		    System.out.println(measure);
			while (!malt.isReady()) {
				try {
					Thread.sleep(200);
				}catch(InterruptedException e) {
					
				}
			}
			
			
			String value = val.toString();
			ts.set(Param.measuretime, value);
			Response res = malt.settings(ts);
			System.out.println(res);
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
			Response ressave = malt.exportSettings(fileName);
			System.out.println(ressave);
			System.out.println("Measure Time is updated...");
			res = malt.disconnect();
			System.out.println(res);
			System.out.println(malt.getConnectionResponse());
				
		}else {
			System.out.println("No capability to execute");
		}
		
		

		// Print finished state
		System.out.println(maltMeasure.getIdShort() + " is " + "Executed");	
	


	}
}
