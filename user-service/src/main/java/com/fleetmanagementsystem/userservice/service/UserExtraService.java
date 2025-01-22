package com.fleetmanagementsystem.userservice.service;


import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserRequest;

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
}
