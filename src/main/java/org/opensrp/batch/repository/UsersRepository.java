package org.opensrp.batch.repository;

import org.opensrp.batch.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
