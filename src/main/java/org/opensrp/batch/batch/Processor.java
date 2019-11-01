package org.opensrp.batch.batch;

import java.util.Optional;

import org.opensrp.batch.entity.Users;
import org.opensrp.batch.repository.UsersRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<Users, Users> {
	
	@Autowired
	private UsersRepository userRepo;
	
	@Override
	public Users process(Users user) throws Exception {
		
		Optional<Users> userFromDb = userRepo.findById(user.getUserId());
		System.err.println(userFromDb);
		if (userFromDb.isPresent()) {
			System.err.println("oo");
			user.setAccount(user.getAccount().add(userFromDb.get().getAccount()));
		}
		return user;
	}
	
}
