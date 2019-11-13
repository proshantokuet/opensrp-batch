package org.opensrp.batch.header;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class FamilyHeader implements FlatFileHeaderCallback {
	
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("Id,Household Number,SS Name,Village Name,Cluster,Household Type,Household Head Name,Number of Household Member,Has Latrine,Provider,Date Created, Union, Upazila, District");
		
	}
	
}
