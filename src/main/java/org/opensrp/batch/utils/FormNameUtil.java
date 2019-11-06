package org.opensrp.batch.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FormNameUtil {
	
	String fileName = "";
	
	@Value("${household.form.name}")
	String householdFormName;
	
	@Value("${child.form.name}")
	String childFormName;
	
	@Value("${member.form.name}")
	String memberFormName;
	
	public String getFormName(String formName, String start, String end) {
		if (formName.equalsIgnoreCase(householdFormName)) {
			fileName = "HHReg_" + start + "_" + end;
			
		} else if (formName.equalsIgnoreCase(memberFormName)) {
			
			fileName = "MemberReg_" + start + "_" + end;
		} else if (formName.equalsIgnoreCase(childFormName)) {
			
			fileName = "ChildReg_" + start + "_" + end;
		} else {
			
		}
		return fileName;
	}
}
