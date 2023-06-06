package com.yuriytkach.demo.demospringsecurity.apikey;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ApiKeyRepository extends CrudRepository<ApiKeyEntity, String> {

  Optional<ApiKeyEntity> findByApiKey(String apiKey);
}
