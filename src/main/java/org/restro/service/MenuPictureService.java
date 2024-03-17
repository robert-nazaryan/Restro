package org.restro.service;

import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuPictureService {

    boolean savePicture(Menu menu, List<MultipartFile> multipartFiles);

    MenuPicture save(MenuPicture manuPicture);
    List<MenuPicture> findAll();

    byte[] getMenuImage(String picName) throws IOException;
}
