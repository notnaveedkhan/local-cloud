package com.localcloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .mvcMatchers("**/css/**").permitAll()
                .mvcMatchers("**/js/**").permitAll()
                .mvcMatchers("/api/**").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/").authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable();
        return http.build();
    }

}
