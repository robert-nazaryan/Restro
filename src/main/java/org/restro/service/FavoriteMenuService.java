package org.restro.service;

import org.restro.entity.FavoriteMenu;
import org.restro.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface FavoriteMenuService {

    void save(FavoriteMenu favoriteMenu);

    void deleteByMenuIdAndUserId(int menuId, int userId);

    Optional<FavoriteMenu> findById(int id);

    List<FavoriteMenu> findAllByUserId(int id);

    List<Menu> markFavorites(int userId, List<Menu> menus);

}
