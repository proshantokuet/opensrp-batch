package org.opensrp.batch.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchDTO {
	
	@Autowired
	private EntityManager em;
	
	public List<Integer> getBranchByUser(int userId) {
		List<Integer> branches = new ArrayList<Integer>();
		javax.persistence.Query q = em
		        .createNativeQuery("SELECT branch_id FROM core.\"user_branch\" where user_id = :userId");
		q.setParameter("userId", userId);
		int batchId = 0;
		@SuppressWarnings("unchecked")
		List<Integer> branchList = q.getResultList();
		for (Integer branch : branchList) {
			batchId = branch;
			
			branches.add(batchId);
		}
		return branches;
		
	}
}
