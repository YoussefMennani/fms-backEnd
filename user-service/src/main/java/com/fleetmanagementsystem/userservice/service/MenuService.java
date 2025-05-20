package com.fleetmanagementsystem.userservice.service;

import com.fleetmanagementsystem.userservice.Model.Menu;
import com.fleetmanagementsystem.userservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * Save or update a menu by its menuName.
     */
    public Menu saveOrUpdateMenu(Menu menu) {
        Optional<Menu> existingMenuOptional = menuRepository.findByMenuName(menu.getMenuName());

        if (existingMenuOptional.isPresent()) {
            Menu existingMenu = existingMenuOptional.get();
            existingMenu.setData(menu.getData());
            return menuRepository.save(existingMenu);
        } else {
            return menuRepository.save(menu);
        }
    }

    /**
     * Get a menu by its ID.
     */
    public Menu getMenuById(String id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + id));
    }

    /**
     * Get a menu by its menuName.
     */
    public Menu getMenuByMenuName(String menuName) {
        return menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new RuntimeException("Menu not found with name: " + menuName));
    }
}
