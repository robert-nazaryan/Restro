package org.restro.repository;

import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuPictureRepository extends JpaRepository<MenuPicture,Integer> {


    List<MenuPicture> findAllByMenu(Menu menu);
}
