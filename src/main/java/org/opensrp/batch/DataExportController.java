package org.opensrp.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataExportController {
	
	@Autowired
	@Qualifier("myJobLauncher")
	JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("accountJob")
	Job dataExportJob;
	
	@Value("${household.form.name}")
	String householdFormName;
	
	@Value("${child.form.name}")
	String childFormName;
	
	@Value("${member.form.name}")
	String memberFormName;
	
	@RequestMapping("/data-export")
	public String export(@RequestParam(name = "user", required = true) String user,
	                     @RequestParam(name = "start", required = true) String start,
	                     @RequestParam(name = "end", required = true) String end,
	                     @RequestParam(name = "batch") String batch, @RequestParam(name = "sk") String sk,
	                     @RequestParam(name = "form_name", required = true) String formName) throws Exception {
		String row = "";
		String where = "";
		String fileName = "";
		
		if (formName.equalsIgnoreCase(householdFormName)) {
			System.err.println("formName" + formName);
			row = "id,hh_name, hh_number,hh_number_of_members,ss_name,cluster_name,hh_type,hh_phone_number,hh_number_of_members,hh_has_latrine";
			fileName = "HHReg_" + start + "_" + end + ".xlsx";
			
		} else if (formName.equalsIgnoreCase(memberFormName)) {
			row = "id,hh_name, unique_id,relation_with_hoh,mother_name,mobile_number,id_type,nid,birthregistrationid,dob_known,dob,age,gender,marital_status,blood_group";
			fileName = "MemberReg_" + start + "_" + end + ".xlsx";
		} else if (formName.equalsIgnoreCase(childFormName)) {
			row = "id,hh_name, unique_id,relation_with_hoh,mother_name,dob,gender,blood_group";
			fileName = "ChildReg_" + start + "_" + end + ".xlsx";
		} else {
			
		}
		
		JobParameters jobParameters = new JobParametersBuilder()
		        .addString("source", System.currentTimeMillis() + "")
		        .addString(
		            "query",
		            "SELECT " + row + " FROM core.\"viewJsonDataConversionOfClient\" where "
		                    + whereClause(start, end, formName)).addString("user", user).addString("fileName", fileName)
		        .addString("formName", formName).addString("batch", batch).toJobParameters();
		jobLauncher.run(dataExportJob, jobParameters);
		
		return "Batch job has been invoked";
	}
	
	public String whereClause(String start, String end, String formName) {
		String where = "date_created BETWEEN \'" + start + "\' AND \'" + end + "\' and event_type = " + "\'"
		        + myMap.get(formName) + "\'";
		
		return where;
		
	}
	
	private static Map<String, String> myMap = new HashMap<String, String>();
	static {
		
		myMap.put("Household Registration", "Family Registration");
		myMap.put("Child Registration", "Child Registration");
		myMap.put("Member Registration", "Family Member Registration");
	}
}
