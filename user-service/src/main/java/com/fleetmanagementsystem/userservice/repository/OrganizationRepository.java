package com.fleetmanagementsystem.userservice.repository;

import com.fleetmanagementsystem.userservice.Model.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationRepository extends MongoRepository<Organization,String> {
}
