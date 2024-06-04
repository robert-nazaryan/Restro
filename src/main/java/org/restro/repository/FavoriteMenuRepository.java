package org.restro.repository;

import org.restro.entity.FavoriteMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteMenuRepository extends JpaRepository<FavoriteMenu, Integer> {

    Page<FavoriteMenu> findAllByUserId(int id, Pageable pageable);

    void deleteByMenuIdAndUserId(int menuId, int userId);

}
