package org.opensrp.batch.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.opensrp.batch.entity.Users;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<Users> {
	
	@Override
	public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
		Users user = new Users();
		user.setName(rs.getString("name"));
		user.setUserId(rs.getLong("id"));
		
		return user;
	}
}
