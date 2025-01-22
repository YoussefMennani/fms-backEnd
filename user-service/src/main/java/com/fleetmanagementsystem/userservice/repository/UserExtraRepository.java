package com.fleetmanagementsystem.userservice.repository;

import com.fleetmanagementsystem.userservice.Model.UserExtra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserExtraRepository extends MongoRepository<UserExtra, String> {
    Optional<UserExtra> findByUsername(String username);
    UserExtra findByKeycloakId(String keycloakId);


}
