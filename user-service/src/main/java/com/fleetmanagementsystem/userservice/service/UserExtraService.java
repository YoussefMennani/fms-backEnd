package com.fleetmanagementsystem.userservice.service;


import com.fleetManagementSystem.commons.user.UserMinimal;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserRequest;
import com.fleetmanagementsystem.userservice.organization.Organization;
import org.bson.types.ObjectId;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserExtraService {

    UserExtra validateAndGetUserExtra(String username, Principal principal);

    Optional<UserExtra> getUserExtra(String username);

    UserExtra saveUserExtra(UserExtra userExtra);

//    UserExtra addUser(UserRequest driver);

    UserExtra createUser(UserRequest userExtra);

    List<UserExtra> getAllUsers();

    List<UserExtra> findProfileByOrganizationId(List<Organization> organizationList);

    List<UserMinimal> getUsersByOrgs(List<ObjectId> stringListOrg);

    List<UserMinimal> getUsersFromList(List<String> usersList);
}
