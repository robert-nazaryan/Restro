package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FavoriteMenuServiceImpl implements FavoriteMenuService {

    private final FavoriteMenuRepository favoriteMenuRepository;

    @Override
    public void save(FavoriteMenu favoriteMenu) {
        favoriteMenuRepository.save(favoriteMenu);
        log.info("Saving favorite menu with id: {}", favoriteMenu.getId());
    }

    @Override
    public void deleteByMenuIdAndUserId(int menuId, int userId) {
        favoriteMenuRepository.deleteByMenuIdAndUserId(menuId, userId);
        log.info("User with id: {} delete his favorite menu with id: {}", userId, menuId);
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
        log.info("Marking all favorite menus for user with id: {}", userId);
        return menus;
    }

}
