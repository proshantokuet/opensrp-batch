package org.opensrp.batch.header;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class FamilyHeader implements FlatFileHeaderCallback {
	
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("Id,Household Number,SS Name,Village Name,Cluster,Household Type,Household Name,Number of Household Member,Has Latrine");
		/*writer.write("Household Number,");
		writer.write("SS Name,");
		writer.write("Village Name,");
		writer.write("Cluster,");
		writer.write("Household Type,");
		writer.write("Household Name,");
		writer.write("Number of Household Member");*/
	}
	
}
