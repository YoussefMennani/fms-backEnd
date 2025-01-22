package com.fleetmanagementsystem.userservice.organization;


import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrganizationsRepository extends MongoRepository<Organization, String> {
    List<Organization> findByParentId(String parentId); // Find children of a given parent
    List<Organization> findByParentIdIsNull(); // Find root organizations (parentId is null)

}