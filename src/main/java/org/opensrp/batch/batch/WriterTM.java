package org.opensrp.batch.batch;

import java.util.List;

import org.opensrp.batch.entity.DataExport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WriterTM implements ItemWriter<DataExport> {
	
	/*@Autowired
	private UsersRepository repo;*/
	
	@Override
	@Transactional
	public void write(List<? extends DataExport> users) throws Exception {
		/*System.err.println(users);
		repo.saveAll(users);*/
	}
	
}
