package com.fleetmanagementsystem.userservice.controller;


import com.fleetmanagementsystem.userservice.Model.Menu;
import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.ProfileRequest;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserExtraRequest;
import com.fleetmanagementsystem.userservice.controller.dto.UserRequest;
import com.fleetmanagementsystem.userservice.organization.Organization;
import com.fleetmanagementsystem.userservice.service.MenuService;
import com.fleetmanagementsystem.userservice.service.ProfilService;
import com.fleetmanagementsystem.userservice.service.RoleService;
import com.fleetmanagementsystem.userservice.service.UserExtraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    @GetMapping("/me")
    public UserExtra getUserExtra(Principal principal) {
//        Authentication authentication = (Authentication) principal;
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // Define the role you're looking for
//        String targetRole = "ROLE_ADMIN"; // Replace with the role you want to check
//
//        // Check if the role exists in the authorities
//        boolean roleExists = authorities.stream()
//                .anyMatch(authority -> authority.getAuthority().equals(targetRole));
//
//        if (roleExists) {
//            System.out.println("Role " + targetRole + " exists.");
//        } else {
//            System.out.println("Role " + targetRole + " does not exist.");
//        }


        return userExtraService.validateAndGetUserExtra(principal.getName(),principal);
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
            profiles = profilService.findProfileByOrganizationId(roleService.extractIdOrganization(principal));
        }
        return ResponseEntity.ok(profiles);
    }

    @DeleteMapping("/profile/{idProfile}")
    public ResponseEntity<String> deleteProfileById(@PathVariable String idProfile){
        String id = profilService.deleteProfile(idProfile);
        return  ResponseEntity.ok(id);
    }



}
