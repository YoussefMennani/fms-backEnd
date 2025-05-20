package com.fleetmanagementsystem.userservice.repository;

import com.fleetManagementSystem.commons.user.UserMinimal;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserExtraRepository extends MongoRepository<UserExtra, String> {
    Optional<UserExtra> findByUsername(String username);
    UserExtra findByKeycloakId(String keycloakId);

    @Query("{ 'organization.$id': { $in: ?0 } }")
    List<UserMinimal> findAllByOrganizationIds(List<ObjectId> organizationIds);

    List<UserMinimal> findByIdIn(List<String> ids);

}
