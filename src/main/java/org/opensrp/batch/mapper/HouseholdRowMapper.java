package org.opensrp.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.opensrp.batch.entity.DataExport;
import org.opensrp.batch.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

public class HouseholdRowMapper implements RowMapper<DataExport> {
	
	@Autowired
	private DateUtils dateUtil;
	
	@Override
	public DataExport mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataExport export = new DataExport();
		export.setId(rs.getString("id"));
		export.setHouseholdNumber("\'" + rs.getString("hh_number") + "\'");
		export.setSSName(rs.getString("ss_name"));
		export.setVillageName(rs.getString("village")); 
		export.setCluster(rs.getString("cluster_name"));
		export.setHouseholdType(rs.getString("hh_type"));
		export.setName(rs.getString("hh_name"));
		export.setMemberCount(rs.getString("hh_number_of_members"));
		export.setHasLatrine(rs.getString("hh_has_latrine"));
		export.setProvider(rs.getString("provider_id"));
		export.setDateCreated(DateUtils.getDateAsYYYYMMddHHMMSS(rs.getString("date_created")));
		return export;
	}
}
