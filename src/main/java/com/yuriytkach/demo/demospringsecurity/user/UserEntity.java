package com.yuriytkach.demo.demospringsecurity.user;

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
@Table(name = "users")
public class UserEntity {

  @Id
  private String username;

  private String password;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "status_type")
  @Type(PostgreSQLEnumType.class)
  private ClientStatus status;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final UserEntity that = (UserEntity) o;
    return getUsername() != null && Objects.equals(getUsername(), that.getUsername());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
