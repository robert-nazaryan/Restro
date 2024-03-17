package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import org.restro.entity.Menu;
import org.restro.repository.MenuRepository;
import org.restro.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Override
    public Menu save(Menu manu) {
        return menuRepository.save(manu);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> findById(int id) {
        return menuRepository.findById(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public void deleteMenuById(int id) {
        menuRepository.deleteById(id);
    }
}
