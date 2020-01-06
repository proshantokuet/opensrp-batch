package org.opensrp.batch.utils;

import java.util.ArrayList;
import java.util.List;

import org.opensrp.batch.dto.BranchDTO;
import org.opensrp.batch.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExtractSK {
	
	@Autowired
	private UserDTO userDTO;
	
	@Autowired
	private BranchDTO branchDTO;
	
	@Value("${user.type.admin}")
	String ADMIN;
	
	@Value("${user.type.am}")
	String AM;
	
	public String getSKs(String userName, String userType, int branch, String skName) {
		System.err.println("USerType" + userType);
		List<Integer> branchId = new ArrayList<Integer>();
		String skUserNames = "";
		if (userType.equalsIgnoreCase(ADMIN)) {
			if (branch == 0 && skName.isEmpty()) {
				skUserNames = "";
			} else if (branch != 0 && !skName.isEmpty()) {
				skUserNames += "'" + skName + "'";
			} else if (branch != 0 && skName.isEmpty()) {
				branchId.add(branch);
				skUserNames = userDTO.getUserNames(branchId);
			} else if (branch == 0 && !skName.isEmpty()){
				skUserNames += "'" + skName + "'";
			}
			
		} else if (userType.equalsIgnoreCase(AM)) {
			if (branch == 0 && skName.isEmpty()) {
				int userId = userDTO.getUserIdByUserName(userName);
				List<Integer> branchList = branchDTO.getBranchByUser(userId);
				skUserNames = userDTO.getUserNames(branchList);
			} else if (branch != 0 && !skName.isEmpty()) {
				skUserNames += "'" + skName + "'";
			} else if (branch != 0 && skName.isEmpty()) {
				branchId.add(branch);
				skUserNames = userDTO.getUserNames(branchId);
			}
		} else {
			System.err.println("NO found");
		}
		
		/*int userId = userDTO.getUserIdByUserName(userName);
		System.err.println("UserId:" + userId);
		List<Integer> branchList = branchDTO.getBranchByUser(userId);
		System.err.println("branchList:" + branchList);
		
		String sks = userDTO.getUserNames(branchList);
		System.err.println(sks);*/
		
		return skUserNames;
	}
}
