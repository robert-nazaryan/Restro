package org.restro.service;

import org.restro.entity.User;

public interface SendMailService {

    void send(String to, String subject, String message);

    void sendWelcomeMail(User user);

}
