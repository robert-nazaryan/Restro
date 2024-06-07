package org.restro.controller;

import org.junit.jupiter.api.Test;
import org.restro.entity.WeeklyEmail;
import org.restro.service.WeeklyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(WeeklyEmailController.class)
class WeeklyEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeeklyEmailService weeklyEmailService;

    @Test
    @WithMockUser
    void testAddEmail() throws Exception {
        WeeklyEmail weeklyEmail = new WeeklyEmail();

        mockMvc.perform(post("/weeklyEmail/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("weeklyEmail", weeklyEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(weeklyEmailService, times(1)).save(any(WeeklyEmail.class));
    }
}
