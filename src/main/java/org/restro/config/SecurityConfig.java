package org.restro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/users/loginSuccess")
                .and()
                .logout()
                .logoutSuccessUrl("/");

        return httpSecurity.build();
    }
}
