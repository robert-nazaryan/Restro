package org.restro.service;

import org.restro.entity.FavoriteMenu;
import org.restro.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FavoriteMenuService {

    void save(FavoriteMenu favoriteMenu);

    void deleteByMenuIdAndUserId(int menuId, int userId);

    Optional<FavoriteMenu> findById(int id);

    Page<FavoriteMenu> findAllByUserId(int id, Pageable pageable);

    List<Menu> markFavorites(int userId, List<Menu> menus);

}
