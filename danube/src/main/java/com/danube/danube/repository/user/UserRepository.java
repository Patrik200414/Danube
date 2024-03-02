package com.danube.danube.repository.user;

import com.danube.danube.model.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Long, UserEntity> {
}
