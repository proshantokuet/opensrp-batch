package org.opensrp.batch.batch;

import java.util.List;

import org.opensrp.batch.entity.Users;
import org.opensrp.batch.repository.UsersRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WriterTM implements ItemWriter<Users> {
	
	@Autowired
	private UsersRepository repo;
	
	@Override
	@Transactional
	public void write(List<? extends Users> users) throws Exception {
		System.err.println(users);
		repo.saveAll(users);
	}
	
}
