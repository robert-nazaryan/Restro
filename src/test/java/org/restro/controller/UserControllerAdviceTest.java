package org.restro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.restro.entity.User;
import org.restro.security.SpringUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ControllerAdvice
class UserControllerAdviceTest {

    @InjectMocks
    private UserControllerAdvice userControllerAdvice;

    @Mock
    private SpringUser springUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCurrentUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("testUser");
        mockUser.setEmail("test@example.com");

        when(springUser.getUser()).thenReturn(mockUser);

        User currentUser = userControllerAdvice.currentUser(springUser);

        assertEquals(mockUser, currentUser);
    }

    @Test
    void testCurrentUserWithNullSpringUser() {
        User currentUser = userControllerAdvice.currentUser(null);

        assertNull(currentUser);
    }
}
