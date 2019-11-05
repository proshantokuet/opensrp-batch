package org.opensrp.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBatchDemoApplicationTests {
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void dd() throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
		        .parse("2019-10-27T13:30:38.939+06:00");
		
		//System.err.println(date.getTime());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.err.println(simpleDateFormat.format(date));
		String s = "Member-Registration";
		String replacedString = s.replace("-", " ");
		System.err.println(replacedString);
	}
	
}
