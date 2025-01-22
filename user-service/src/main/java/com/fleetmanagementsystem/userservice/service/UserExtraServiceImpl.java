package com.fleetmanagementsystem.userservice.service;

import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserRequest;
import com.fleetmanagementsystem.userservice.exception.UserExtraNotFoundException;
import com.fleetmanagementsystem.userservice.keycloak.KeycloakUserService;
import com.fleetmanagementsystem.userservice.keycloak.UserExtraMapper;
import com.fleetmanagementsystem.userservice.organization.Organization;
import com.fleetmanagementsystem.userservice.organization.OrganizationsRepository;
import com.fleetmanagementsystem.userservice.repository.OrganizationRepository;
import com.fleetmanagementsystem.userservice.repository.ProfilRepository;
import com.fleetmanagementsystem.userservice.repository.UserExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserExtraServiceImpl implements UserExtraService {

    private final UserExtraRepository userExtraRepository;
    private final ProfilRepository profilRepository;
    private final OrganizationsRepository organizationRepository;
    private final RoleService roleService;
    private final KeycloakUserService keycloakUserService;

    private final UserExtraMapper userExtraMapper;

    @Override
    public UserExtra validateAndGetUserExtra(String username, Principal principal) {
        try {
            boolean isSuperAdmin =  roleService.isAuthorized(principal,List.of("ROLE_SUPER_ADMIN"));
            UserExtra userExtra;
            if(!isSuperAdmin){
                userExtra = getUserExtra(username)
                        .orElseThrow(() -> new UserExtraNotFoundException(username));
            }else{
                userExtra = userExtraMapper.mapJwtClaimsToUserExtra(principal);
            }
            return userExtra;
        } catch (UserExtraNotFoundException ex) {
            // Log additional details
            System.out.println("UserExtra not found for username: {}");
            throw ex; // Re-throw if needed
        }

    }

    @Override
    public Optional<UserExtra> getUserExtra(String username) {
        Optional<UserExtra> userExtra = userExtraRepository.findByUsername(username);
        return userExtra;
    }

    @Override
    public UserExtra saveUserExtra(UserExtra userExtra) {
        return userExtraRepository.save(userExtra);
    }


//    @Override
//    public UserExtra addUser(UserRequest userExtra) {
//        // Add any business logic here, e.g., checking for duplicates
//
//        Optional<Profile> profile = profilRepository.findById(userExtra.getProfile());
//        Optional<Organization> organization = organizationRepository.findById(userExtra.getOrganization());
//
//        UserExtra user = UserExtra.builder()
//                .avatar(userExtra.getProfileImageUrl())
//                .profile(profile.get())
//                .organization(organization.get())
//                .username(userExtra.getUsername())
//                .build();
//
//        return userExtraRepository.save(user);
//
//    }

    public List<UserExtra> getAllUsers() {
        List<UserExtra> userExtra = userExtraRepository.findAll();
        return userExtra;
    }



    public UserExtra createUser(UserRequest userRequest) {

        Profile profile = profilRepository.findById(userRequest.getProfile())
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        Organization organization = organizationRepository.findById(userRequest.getOrganization())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        //List<String> roles = Arrays.asList("ADMIN", "USER","MANAGER");  // List of roles to assign
        List<String> roles = Arrays.asList(profile.getRole());
        // Step 1: Create user in Keycloak
        String keycloakId = keycloakUserService.createUser(userRequest.getUsername(), userRequest.getFirstName(),userRequest.getLastName(),userRequest.getEmail(), userRequest.getPassword(),"test",organization.getId(),roles);


        // Step 2: Create user in MongoDB


        UserExtra user = UserExtra.builder()
                .keycloakId(keycloakId)
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .avatar(userRequest.getProfileImageUrl())
                .profile(profile)
                .organization(organization)
                .username(userRequest.getUsername())
                .build();

        return userExtraRepository.save(user);
    }

    public UserExtra getUserByKeycloakId(String keycloakId) {
        return userExtraRepository.findByKeycloakId(keycloakId);
    }

}