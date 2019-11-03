package org.opensrp.batch.entity;

import org.springframework.stereotype.Service;

@Service
public class SK {
	
	private int id;
	
	private String username;
	
	public SK() {
		
	}
	
	public SK(int id, String username) {
		this.id = id;
		this.username = username;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "SK [id=" + id + ", username=" + username + "]";
	}
	
}
