package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.restro.repository.MenuPictureRepository;
import org.restro.service.MenuPictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuPictureServiceImpl implements MenuPictureService {
    private final MenuPictureRepository menuPictureRepository;

    @Value("${restro.picture.upload.directory}")
    private String uploadDirectory;

    @Override
    public boolean savePicture(Menu menu, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
                for (MultipartFile multipartFile : multipartFiles) {
                    String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                    File file = new File(uploadDirectory, picName);
                    try {
                        multipartFile.transferTo(file);
                    } catch (IOException e) {
                        log.error("Something went wrong");
                        return false;
                    }
                    save(MenuPicture.builder()
                            .menu(menu)
                            .picName(picName)
                            .build());
                }
                return true;
        }
        return false;
    }

    @Override
    public MenuPicture save(MenuPicture menuPicture) {
        return menuPictureRepository.save(menuPicture);
    }

    @Override
    public List<MenuPicture> findAll() {
        return menuPictureRepository.findAll();
    }

    @Override
    public byte[] getMenuImage(String picName) throws IOException {
            InputStream inputStream = new FileInputStream(uploadDirectory + File.separator + picName);
            return IOUtils.toByteArray(inputStream);

    }
}
