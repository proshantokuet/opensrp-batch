package org.opensrp.batch.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SelectField {
	
	String selectField = "";
	
	@Value("${household.form.name}")
	String householdFormName;
	
	@Value("${child.form.name}")
	String childFormName;
	
	@Value("${member.form.name}")
	String memberFormName;
	
	public String getSelectField(String formName) {
		if (formName.equalsIgnoreCase(householdFormName)) {
			System.err.println("formName" + formName);
			selectField = "id,hh_name, hh_number,hh_number_of_members,ss_name,cluster_name,hh_type,hh_phone_number,hh_number_of_members,hh_has_latrine";
			
		} else if (formName.equalsIgnoreCase(memberFormName)) {
			selectField = "id,hh_name, unique_id,relation_with_hoh,mother_name,mobile_number,id_type,nid,birthregistrationid,dob_known,dob,age,gender,marital_status,blood_group";
			
		} else if (formName.equalsIgnoreCase(childFormName)) {
			selectField = "id,hh_name, unique_id,relation_with_hoh,mother_name,dob,gender,blood_group";
			
		} else {
			
		}
		
		return selectField;
	}
}
