package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.Menu;
import org.restro.repository.FavoriteMenuRepository;
import org.restro.service.FavoriteMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteMenuServiceImpl implements FavoriteMenuService {

    private final FavoriteMenuRepository favoriteMenuRepository;

    @Override
    public void save(FavoriteMenu favoriteMenu) {
        favoriteMenuRepository.save(favoriteMenu);
    }

    @Override
    public void deleteByMenuIdAndUserId(int menuId, int userId) {
        favoriteMenuRepository.deleteByMenuIdAndUserId(menuId, userId);
    }

    @Override
    public Optional<FavoriteMenu> findById(int id) {
        return favoriteMenuRepository.findById(id);
    }

    @Override
    public List<FavoriteMenu> findAllByUserId(int id) {
        return favoriteMenuRepository.findAllByUserId(id);
    }

    @Override
    public List<Menu> markFavorites(int userId, List<Menu> menus) {
        List<FavoriteMenu> favoriteMenus = favoriteMenuRepository.findAll();
        for (Menu menu : menus) {
            for (FavoriteMenu favoriteMenu : favoriteMenus) {
                if (menu.getId() == favoriteMenu.getMenu().getId() && userId == favoriteMenu.getUser().getId()) {
                    menu.setFavorite(true);
                }
            }
        }
        return menus;
    }

    @Override
    public void deleteById(int id) {
        favoriteMenuRepository.deleteById(id);
    }

}
