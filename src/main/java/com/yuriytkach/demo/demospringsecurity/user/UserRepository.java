package com.yuriytkach.demo.demospringsecurity.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {

  Optional<UserEntity> findByUsername(String username);

}
