package org.restro.service;


import org.restro.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    Menu save(Menu manu);

    List<Menu> findAll();

    Optional<Menu> findById(int id);

    void updateMenu(Menu menu);

    void deleteMenuById(int id);
}
