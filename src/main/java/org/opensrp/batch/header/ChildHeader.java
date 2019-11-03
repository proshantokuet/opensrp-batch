package org.opensrp.batch.header;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class ChildHeader implements FlatFileHeaderCallback {
	
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("Id, Member Number, Member Name, Relation with HOH, Mother Name, Date of Birth, Gender, Blood Group");
		
	}
}
