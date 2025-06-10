package com.fleetmanagementsystem.userservice.controller;


import com.fleetManagementSystem.commons.user.UserMinimal;
import com.fleetManagementSystem.commons.user.UserResponse;
import com.fleetmanagementsystem.userservice.FeignClient.SubscriptionClient;
import com.fleetmanagementsystem.userservice.Model.Menu;
import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.ProfileRequest;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserExtraRequest;
import com.fleetmanagementsystem.userservice.controller.dto.UserRequest;
import com.fleetmanagementsystem.userservice.exception.UserExtraNotFoundException;
import com.fleetmanagementsystem.userservice.organization.Organization;
import com.fleetmanagementsystem.userservice.organization.OrganizationsService;
import com.fleetmanagementsystem.userservice.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userextras")
public class UserExtraController {

    private final UserExtraService userExtraService;
    private final MenuService menuService;
    private final ProfilService profilService;

    private final RoleService roleService;
    private final SubscriptionClient subscriptionClient;
    private final OrganizationsService organizationsService;
    @GetMapping("/me")
    public ResponseEntity<UserExtra> getUserExtra(Principal principal) {
        try {
            // Get the authentication token
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String token = "Bearer " + authentication.getToken().getTokenValue();

            // Validate and get user details
            UserExtra userExtra = userExtraService.validateAndGetUserExtra(principal.getName(), principal);

            // Check if the user is a super admin
            boolean isSuperAdmin = roleService.isAuthorized(principal, List.of("ROLE_SUPER_ADMIN"));

            if (!isSuperAdmin) {
                // Get the organization and check if the user is authorized
                Organization organization = this.organizationsService.getRootOrganizationById(userExtra.getOrganization().getId());
                boolean isOrganizationAuthorized = subscriptionClient.isOrganizationAuthorized(token, organization.getId());

                if (!isOrganizationAuthorized) {
                    // If not authorized, throw an AccessDeniedException or return 403 Forbidden
                    throw new AccessDeniedException("User is not authorized to access this resource");
                }

                userExtra.setAuthorizedUser(true);
            } else {
                userExtra.setAuthorizedUser(true);
            }

            return ResponseEntity.ok(userExtra);
        } catch (AccessDeniedException e) {
            // Handle unauthorized access
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getAccountInfo")
    public ResponseEntity<UserResponse> getAccountInfo(Principal principal) {
        try {
            // Validate and get user details
            UserExtra userExtra = userExtraService.validateAndGetUserExtra(principal.getName(), principal);
            UserResponse userResponse = UserResponse.builder()
            .id(userExtra.getId())
            .username(userExtra.getUsername())
            .firstName(userExtra.getFirstName())
            .lastName(userExtra.getLastName())
            .email(userExtra.getEmail())
            .avatar(userExtra.getAvatar())
            .profileName(userExtra.getProfile().getName())
            .profileRole(userExtra.getProfile().getRole())
            .organizationName(userExtra.getOrganization().getName())
            .phoneNumber(userExtra.getPhoneNumber())
            .build();
            // Check if the user is a super admin
            return ResponseEntity.ok(userResponse);
        } catch (AccessDeniedException e) {
            // Handle unauthorized access
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/updateProfileInfo")
    public ResponseEntity<UserResponse> updateProfileInfo(Principal principal,@RequestBody UserResponse userResponse) {
        try {
            // Validate and get user details
            UserResponse userResp =  userExtraService.updateAccountDetailsUser(principal.getName(), userResponse);

            // Check if the user is a super admin
            return ResponseEntity.ok(userResp);
        } catch (AccessDeniedException e) {
            // Handle unauthorized access
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping("/me")
    public UserExtra saveUserExtra(@Valid @RequestBody UserExtraRequest updateUserExtraRequest,
                                   Principal principal) {

        Optional<UserExtra> userExtraOptional = userExtraService.getUserExtra(principal.getName());
        UserExtra userExtra = userExtraOptional.orElseGet(() -> new UserExtra(principal.getName()));
        userExtra.setAvatar(updateUserExtraRequest.getAvatar());
        return userExtraService.saveUserExtra(userExtra);
    }


    @GetMapping("/getUsersOfOrganization")
    public ResponseEntity<List<UserMinimal>> getUsersOfOrganization(Principal principal){
        UserExtra userExtraOptional = userExtraService.getUserExtra(principal.getName()).orElseThrow(() -> new UserExtraNotFoundException("User Not Found"));
        List<Organization> organizationList = this.organizationsService.getChildOrganization(userExtraOptional.getOrganization().getId());
        List<String> stringListOrg = organizationList.stream().map(org -> org.getId()).toList();

        // inside your controller method
        List<ObjectId> orgIds = organizationList.stream()
                .map(org -> new ObjectId(org.getId()))
                .toList();


        List<UserMinimal> userExtraList = this.userExtraService.getUsersByOrgs(orgIds);

        return ResponseEntity.ok(userExtraList);
    }


    @PostMapping
    public ResponseEntity<UserExtra> addUser(@Valid @RequestBody UserRequest userRequest) {
        UserExtra savedUserExtra = userExtraService.createUser(userRequest);
        return ResponseEntity.ok(savedUserExtra);
    }

//    @PostMapping("")
//    public void createUser(@Valid @RequestBody UserRequest userRequest) {
//        keycloakAdminService.createUser(userRequest.getUsername(), userRequest.getEmail(), userRequest.getFirstName() ,userRequest.getLastName() );
//    }

    @GetMapping("")
    public ResponseEntity<List<UserExtra>> getAllUsers() {
        List<UserExtra> users =  userExtraService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/getAllUsersByProfile")
    public ResponseEntity<List<UserExtra>> getAllUsersByProfile(Principal principal) {
        boolean isAuthorized =  roleService.isAuthorized(principal,List.of("ROLE_SUPER_ADMIN"));
        List<UserExtra> userExtraList;
        if(isAuthorized){
            userExtraList = userExtraService.getAllUsers();
        }else{
            Organization org = this.organizationsService.getOrganizationById(roleService.extractIdOrganization(principal));
            List<Organization> organizationList = this.organizationsService.destructuredOrganizations(org);
            userExtraList = userExtraService.findProfileByOrganizationId(organizationList);
        }
        return ResponseEntity.ok(userExtraList);
    }

    /**
     * Save or update a menu.
     */
    @PostMapping("/menu/saveOrUpdate")
    public ResponseEntity<Menu> saveOrUpdateMenu(@RequestBody Menu menu) {
        Menu responseMenu =  menuService.saveOrUpdateMenu(menu);
        return ResponseEntity.ok(responseMenu);
    }

    /**
     * Get a menu by its ID.
     */
    @GetMapping("/menu/{id}")
    public Menu getMenuById(@PathVariable String id) {
        return menuService.getMenuById(id);
    }

    /**
     * Get a menu by its menuName.
     */
    @GetMapping("/menu/getByName/{menuName}")
    public Menu getMenuByMenuName(@PathVariable String menuName) {
        return menuService.getMenuByMenuName(menuName);
    }


    @PutMapping("/profile")
    public ResponseEntity<Profile> updateProfile(@RequestBody ProfileRequest profileRequest) {
        Profile savedProfile =  profilService.updateProfile(profileRequest);
        return ResponseEntity.ok(savedProfile);
    }

    @PostMapping("/profile")
    public ResponseEntity<Profile> saveProfile(@RequestBody ProfileRequest profileRequest) {
        Profile savedProfile =  profilService.saveProfile(profileRequest);
        return ResponseEntity.ok(savedProfile);
    }


    @GetMapping("/profiles")
    public ResponseEntity<List<Profile>> getAllProfiles(Principal principal) {
        boolean isAuthorized =  roleService.isAuthorized(principal,List.of("ROLE_SUPER_ADMIN"));
        List<Profile> profiles;
        if(isAuthorized){
            profiles = profilService.getAllProfiles();
        }else{
            Organization org = this.organizationsService.getOrganizationById(roleService.extractIdOrganization(principal));
            List<Organization> organizationList = this.organizationsService.destructuredOrganizations(org);


            profiles = profilService.findProfileByOrganizationId(organizationList);
        }
        return ResponseEntity.ok(profiles);
    }

    @DeleteMapping("/profile/{idProfile}")
    public ResponseEntity<String> deleteProfileById(@PathVariable String idProfile){
        String id = profilService.deleteProfile(idProfile);
        return  ResponseEntity.ok(id);
    }


    @PostMapping("/getUsersFromList")
    public Optional<List<UserMinimal>> getUsersFromList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usersList) {
        List<UserMinimal> userMinimalsList = userExtraService.getUsersFromList(usersList);
        return Optional.of(userMinimalsList);
    }


}
