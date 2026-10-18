package org.scaler.ordermanagementservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityConfigurer {

    @Override
    public void init(SecurityBuilder builder) {

    }

    @Override
    public void configure(SecurityBuilder builder) {

    }
}
