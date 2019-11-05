package org.opensrp.batch.dto;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTO {
	
	@Autowired
	private EntityManager em;
	
	public String getUserNames(List<Integer> branch) {
		String skUserNames = "";
		javax.persistence.Query q = em
		        .createNativeQuery("SELECT u.id, u.username FROM core.\"users\"  u join core.\"user_role\" ur on u.id = ur.user_id join core.\"user_branch\" ub on u.id = ub.user_id where ur.role_id = :skRoleId and ub.branch_id in(:branchId)");
		q.setParameter("skRoleId", 28);
		q.setParameter("branchId", branch);
		
		int cnt = 0;
		@SuppressWarnings("unchecked")
		List<Object[]> SKs = q.getResultList();
		cnt = SKs.size();
		
		int i = 0;
		for (Object[] sk : SKs) {
			i = i + 1;
			if (cnt != i) {
				skUserNames += "'" + sk[1] + "',";
			} else {
				skUserNames += "'" + sk[1] + "'";
			}
			
		}
		
		return skUserNames;
		
	}
	
	public int getUserIdByUserName(String userName) {
		int userId = 0;
		javax.persistence.Query q = em.createNativeQuery("SELECT id FROM core.\"users\" where username = :userName");
		q.setParameter("userName", userName);
		
		@SuppressWarnings("unchecked")
		List<Integer> users = q.getResultList();
		for (Integer user : users) {
			userId = user;
		}
		
		return userId;
		
	}
}
