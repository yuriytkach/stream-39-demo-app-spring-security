package com.yuriytkach.demo.demospringsecurity.apikey;

import java.util.Objects;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import com.yuriytkach.demo.demospringsecurity.security.ClientStatus;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "api_keys")
public class ApiKeyEntity {

  @Id
  private String apiKey;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "status_type")
  @Type(PostgreSQLEnumType.class)
  private ClientStatus status;

  private String client;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final ApiKeyEntity that = (ApiKeyEntity) o;
    return getApiKey() != null && Objects.equals(getApiKey(), that.getApiKey());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
