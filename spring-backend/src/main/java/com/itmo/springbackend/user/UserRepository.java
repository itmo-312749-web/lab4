package com.itmo.springbackend.user;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "select u from User u where u.username = :login or u.email = :login")
    Optional<User> findByLogin(String login);

}
