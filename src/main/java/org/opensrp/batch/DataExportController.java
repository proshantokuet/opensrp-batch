package org.opensrp.batch;

import java.util.HashMap;
import java.util.Map;

import org.opensrp.batch.utils.FormNameUtil;
import org.opensrp.batch.utils.SelectField;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	static String householdFormName;
	
	@Value("${child.form.name}")
	static String childFormName;
	
	@Value("${member.form.name}")
	static String memberFormName;
	
	@Autowired
	private SelectField selectField;
	
	@Autowired
	private FormNameUtil formNameUtil;
	
	@RequestMapping("/data-export")
	@ResponseBody
	public ResponseEntity<String> export(@RequestParam(name = "user", required = true) String user,
	                                     @RequestParam(name = "user_type", required = true) String userType,
	                                     @RequestParam(name = "start", required = true) String start,
	                                     @RequestParam(name = "end", required = true) String end,
	                                     @RequestParam(name = "branch") String branch, @RequestParam(name = "sk") String sk,
	                                     @RequestParam(name = "form_name", required = true) String formName) {
		System.err.println("formName:" + formName.trim());
		formName = formName.trim();
		try {
			String fileName = formNameUtil.getFormName(formName, start, end);
			
			//String selectedField = selectField.getSelectField(formName);
			JobParameters jobParameters = new JobParametersBuilder()
			        .addString("source", System.currentTimeMillis() + "")
			        .addString("query",
			            "SELECT * FROM core.\"viewJsonDataConversionOfClient\" where " + whereClause(start, end, formName))
			        .addString("user", user).addString("userType", userType).addString("fileName", fileName)
			        .addString("formName", formName).addString("sk", sk).addString("branch", branch).toJobParameters();
			jobLauncher.run(dataExportJob, jobParameters);
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		}
		catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>("error", HttpStatus.EXPECTATION_FAILED);
		//return "Batch job has been invoked";
	}
	
	public String whereClause(String start, String end, String formName) {
		String where = " Date(date_created) BETWEEN \'" + start + "\' AND \'" + end + "\' and entity_type = " + "\'"
		        + myMap.get(formName) + "\'";
		
		return where;
		
	}
	
	private static Map<String, String> myMap = new HashMap<String, String>();
	static {
		
		myMap.put("Family-Registration", "ec_family");
		myMap.put("Child-Registration", "ec_child");
		myMap.put("Family-Member-Registration", "ec_family_member");
	}
}
