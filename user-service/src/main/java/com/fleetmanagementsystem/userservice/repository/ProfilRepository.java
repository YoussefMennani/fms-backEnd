package com.fleetmanagementsystem.userservice.repository;

import com.fleetmanagementsystem.userservice.Model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProfilRepository extends MongoRepository<Profile, String> {
//    List<Profile> findByOrganization(String idOrganization);



}
