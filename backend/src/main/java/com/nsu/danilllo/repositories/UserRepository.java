package com.nsu.danilllo.repositories;

import com.nsu.danilllo.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findById(Long aLong);
    Optional<UserCredential> findByUsername(String username);
}
