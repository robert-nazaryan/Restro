package org.restro.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.restro.controller.UserController;
import org.restro.controller.admin.AdminUserController;
import org.restro.entity.WeeklyEmail;
import org.restro.repository.WeeklyEmailRepository;
import org.restro.security.UserDetailService;
import org.restro.service.WeeklyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeeklyEmailServiceImplTest {

    @Autowired
    private WeeklyEmailService weeklyEmailService;

    @MockBean
    private AdminUserController adminUserController;

    @MockBean
    private UserController userController;

    @MockBean
    private UserDetailService userDetailService;

    @MockBean
    private SendMailServiceImpl sendMailService;

    @MockBean
    private WeeklyEmailRepository weeklyEmailRepository;

    @Test
    void testSaveWeeklyEmail() {
        WeeklyEmail weeklyEmail = new WeeklyEmail();
        weeklyEmail.setEmail("test@example.com");

        weeklyEmailService.save(weeklyEmail);

        ArgumentCaptor<WeeklyEmail> captor = ArgumentCaptor.forClass(WeeklyEmail.class);
        verify(weeklyEmailRepository, times(1)).save(captor.capture());
        WeeklyEmail capturedWeeklyEmail = captor.getValue();

        assertEquals("test@example.com", capturedWeeklyEmail.getEmail());
        verify(weeklyEmailRepository, times(1)).save(weeklyEmail);
    }
}
