package org.opensrp.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.opensrp.batch.entity.DataExport;
import org.opensrp.batch.utils.DateUtils;
import org.springframework.jdbc.core.RowMapper;

public class MemberRowMapper implements RowMapper<DataExport> {
	
	@Override
	public DataExport mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataExport export = new DataExport();
		export.setId(rs.getString("id"));
		export.setMemberNumber("\'" + rs.getString("unique_id") + "\'");
		export.setName(rs.getString("hh_name"));
		export.setRelationwithHOH(rs.getString("relation_with_hoh"));
		export.setMotherName(rs.getString("mother_name"));
		export.setMobileNumber(rs.getString("mobile_number"));
		export.setIdType(rs.getString("id_type"));
		export.setNIDNumber("\'" + rs.getString("nid") + "\'");
		export.setBirthIdNumber("\'" + rs.getString("birthregistrationid") + "\'");
		export.setDOBKnown(rs.getString("dob_known"));
		export.setDateofBirth(rs.getString("dob"));
		export.setAge(rs.getString("age"));
		export.setGender(rs.getString("gender"));
		export.setMaritalStatus(rs.getString("marital_status"));
		export.setBloodGroup(rs.getString("blood_group"));
		export.setProvider(rs.getString("provider_id"));
		
		export.setDateCreated(DateUtils.getDateAsYYYYMMddHHMMSS(rs.getString("date_created")));
		export.setGuid(rs.getString("gu_id"));
		return export;
	}
}
