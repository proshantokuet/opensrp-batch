package org.opensrp.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.opensrp.batch.entity.DataExport;
import org.springframework.jdbc.core.RowMapper;

public class ChildRowMapper implements RowMapper<DataExport> {
	
	@Override
	public DataExport mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataExport export = new DataExport();
		export.setId(rs.getString("id"));
		export.setMemberNumber("\"" + rs.getString("unique_id") + "\"");
		export.setName(rs.getString("hh_name"));
		export.setRelationwithHOH(rs.getString("relation_with_hoh"));
		export.setMotherName(rs.getString("mother_name"));
		export.setDateofBirth(rs.getString("dob"));
		export.setGender(rs.getString("gender"));
		export.setBloodGroup(rs.getString("blood_group"));
		export.setProvider(rs.getString("provider_id"));
		export.setDateCreated(rs.getString("date_created"));
		return export;
	}
}
