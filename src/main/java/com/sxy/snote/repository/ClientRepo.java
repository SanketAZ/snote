package com.sxy.snote.repository;

import com.sxy.snote.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepo extends JpaRepository<Client,UUID> {
    Optional<Client>findByUsername(String username);
    Optional<Client>findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
