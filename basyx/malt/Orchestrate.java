package org.eclipse.basyx.malt;


import java.util.Arrays;
import java.util.List;

public class Orchestrate  {
	
	        
	
public static void main(String[] args) {
		
	String[] Skill = new String[] {"True","True","True","True","True",
			"True","True","True","True","True",
			"True","True","True"};
	List<String> SkillList = Arrays.asList(Skill);
	


	
	//A bad implementation: suggestions needed to make general 
	//maybe a for loop that iterates over a list and executes threads 
	//but I do not know how to instantiate correct new class for every value in list
	if (SkillList.get(0).equals("True")){
		Runnable options = new Options();
		Thread options_thread = new Thread(options);
		options_thread.start();
	}
	
	if (SkillList.get(1).equals("True")){
		Runnable caliberate = new Caliberate();
		Thread caliberate_thread = new Thread(caliberate);
		caliberate_thread.start();
	}
	
	if (SkillList.get(2).equals("True")){
		Runnable configuration = new Configuration();
		Thread configuration_thread = new Thread(configuration);
		configuration_thread.start();
	}
		
	if (SkillList.get(3).equals("True")) {
		Runnable alarmDiff = new AlarmDiff();
		Thread alarmdiff_thread = new Thread(alarmDiff);
		alarmdiff_thread.start();
	}
	
	if (SkillList.get(4).equals("True")) {
		Runnable alarmLeak = new AlarmLeak();
		Thread alarmleak_thread = new Thread(alarmLeak);
		alarmleak_thread.start();
	}

	if (SkillList.get(5).equals("True")) {
		Runnable fill = new Fill_time();
		Thread fill_thread = new Thread(fill);
		fill_thread.start();
	}
	
	if (SkillList.get(6).equals("True")) {	
		Runnable isolate = new Isolat_delay();
		Thread isolate_thread = new Thread(isolate);
		isolate_thread.start();
	}
		
	if (SkillList.get(7).equals("True")) {	
		Runnable measure = new Meas_time();
		Thread measure_thread = new Thread(measure);
		measure_thread.start();
	}
	
	if (SkillList.get(8).equals("True")) {
		Runnable offset = new Offset();
		Thread offset_thread = new Thread(offset);
		offset_thread.start();
	}
	
	if (SkillList.get(9).equals("True")) {
		Runnable stabilisation = new Stabilisation_time();
		Thread stabilisation_thread = new Thread(stabilisation);
		stabilisation_thread.start();
	}
	
	if (SkillList.get(10).equals("True")) {
		Runnable testpres = new Testpres();
		Thread testpres_thread = new Thread(testpres);
		testpres_thread.start();
	}
	
	if (SkillList.get(10).equals("True")) {
		Runnable vent = new Vent_delay();
		Thread vent_thread = new Thread(vent);
		vent_thread.start();
	}
	
	if (SkillList.get(11).equals("True")) {
		Runnable testexecute = new TestExecute();
		Thread execute_thread = new Thread(testexecute);
		execute_thread.start();
	}
	
	if (SkillList.get(12).equals("True")) {
		Runnable testresult = new Test_result();
		Thread result_thread = new Thread(testresult);
		result_thread.start();
	}
		
	
	}

}
