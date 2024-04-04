package org.restro.repository;

import org.restro.entity.FavoriteMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteMenuRepository extends JpaRepository<FavoriteMenu, Integer> {

    List<FavoriteMenu> findAllByUserId(int id);

    void deleteByMenuIdAndUserId(int menuId, int userId);

}
