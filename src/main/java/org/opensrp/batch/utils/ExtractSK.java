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
	
	public String getSKsForAdmin(String userName, String userType, String branch, String skName) {
		String skUserNames = "";
		if (userType.equalsIgnoreCase(ADMIN)) {
			if (branch.isEmpty() && skName.isEmpty()) {
				skUserNames = "";
			} else if (!branch.isEmpty() && !skName.isEmpty()) {
				skUserNames += "'" + skName + "',";
			} else if (!branch.isEmpty() && skName.isEmpty()) {
				List<Integer> branchId = new ArrayList<Integer>();
				skUserNames = userDTO.getUserNames(branchId);
			} else {
				
			}
			
		} else if (userType.equalsIgnoreCase(AM)) {
			if (!branch.isEmpty() && skName.isEmpty()) {
				int userId = userDTO.getUserIdByUserName(userName);
				List<Integer> branchList = branchDTO.getBranchByUser(userId);
				skUserNames = userDTO.getUserNames(branchList);
			}
		} else {
			
		}
		
		int userId = userDTO.getUserIdByUserName(userName);
		System.err.println("UserId:" + userId);
		List<Integer> branchList = branchDTO.getBranchByUser(userId);
		System.err.println("branchList:" + branchList);
		
		String sks = userDTO.getUserNames(branchList);
		System.err.println(sks);
		
		return skUserNames;
	}
}
