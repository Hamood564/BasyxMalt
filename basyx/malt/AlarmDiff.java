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

public class AlarmDiff {
	public static void main(String[] args) {
		// Create Manager
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(new AASRegistryProxy(Server.REGISTRYPATH));

		// Retrieve submodel
		ISubmodel submodel = manager.retrieveSubmodel(Server.MALTAASID, Server.DOCUSMID);

		// Retrieve AlarmLeak Property
		ISubmodelElement maltAlarmdiff= submodel.getSubmodelElement(Server.MALTALARMDIFF);
		
		//Print and check condition
		
		
		System.out.println(maltAlarmdiff.getIdShort() + " is " + maltAlarmdiff.getValue());
		Object CheckCondition_Object = maltAlarmdiff.getValue();
		String CheckCondition = CheckCondition_Object.toString();
		//Execute the Property
		
		if (CheckCondition.equals("TRUE")) {

			Malt malt = new Malt(Server.IP,Server.Port);
			malt.selectTest(Server.testindex);
			TestSettings ts = new TestSettings(Server.testindex);
			Response set = malt.requestTestSettings(Server.testindex);
			//creating local copy as an object
			TestSettings localCopy = new TestSettings(set.getMessage());
			Object val = Server.alarmdiffexpression;
			System.out.println(val);
			//get() method to retrieve field and covert to type
		    int alarmdiff = Integer.parseInt(localCopy.get(Param.alarmdiffp)); 
		    System.out.println(alarmdiff);
			while (!malt.isReady()) {
				try {
					Thread.sleep(200);
				}catch(InterruptedException e) {
					
				}
			}
			
			String value = val.toString();
			ts.set(Param.alarmdiffp, value);
			Response res = malt.settings(ts);
			System.out.println(res);
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
			Response ressave = malt.exportSettings(fileName);
			System.out.println(ressave);
			System.out.println("Alarm Diff is updated...");
			res = malt.disconnect();
			System.out.println(res);
			System.out.println(malt.getConnectionResponse());
				
		}else {
			System.out.println("No capability to execute");
		}
		
		

		// Print finished state
		System.out.println(maltAlarmdiff.getIdShort() + " is " + "Executed");	
	

}

}
