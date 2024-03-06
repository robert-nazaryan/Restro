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
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/users/loginPage").permitAll()
//                .requestMatchers("/users/register").permitAll()
//                .requestMatchers("/admin/**").hasAuthority(UserType.ADMIN.name())
                .anyRequest().permitAll()
                .and()
                .formLogin()
//                .loginPage("/users/loginPage")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/users/loginSuccess")
                .and()
                .logout()
                .logoutSuccessUrl("/");

        return httpSecurity.build();
    }
}
