package org.opensrp.batch.batch;

import org.opensrp.batch.entity.DataExport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<DataExport, DataExport> {
	
	/*@Autowired
	private UsersRepository userRepo;*/
	
	@Override
	public DataExport process(DataExport user) throws Exception {
		
		/*Optional<DataExport> userFromDb = userRepo.findById(user.getUserId());
		
		if (userFromDb.isPresent()) {
			System.err.println("oo");
			user.setAccount(user.getAccount().add(userFromDb.get().getAccount()));
		}*/
		return user;
	}
	
}
