package com.fleetmanagementsystem.userservice.repository;

import com.fleetmanagementsystem.userservice.Model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MenuRepository extends MongoRepository<Menu, String> {
    Optional<Menu> findByMenuName(String menuName);
}
