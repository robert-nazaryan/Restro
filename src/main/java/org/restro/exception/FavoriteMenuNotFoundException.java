package org.restro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Favorite menu not found")
public class FavoriteMenuNotFoundException extends RuntimeException {

    public FavoriteMenuNotFoundException(String message) {
        super(message);
    }

}
