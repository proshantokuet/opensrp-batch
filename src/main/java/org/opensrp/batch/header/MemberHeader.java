package org.opensrp.batch.header;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class MemberHeader implements FlatFileHeaderCallback {
	
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("Id, Member Number, Member Name, Relation with HOH, Mother Name, Mobile Number, Id Type,NID Number, Birth Id Number, DOB_Known, Date of Birth, Age, Gender, Marital Status, Blood Group,Provider,Date Created,Fingerprint Status, Village Name, Union, Upazila, District");
	}
}
