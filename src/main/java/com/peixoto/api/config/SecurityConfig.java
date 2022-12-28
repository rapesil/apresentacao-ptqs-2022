package com.peixoto.api.config;

import com.peixoto.api.services.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/actuator**").permitAll()
            .antMatchers("/books**").authenticated()
            .and()
            .httpBasic()
            .and()
            .formLogin()
            .and()
            .csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password enconded {}", passwordEncoder.encode("passNormalUser"));

        auth.inMemoryAuthentication()
            .withUser("admin1")
            .password(passwordEncoder.encode("test"))
            .roles("ADMIN", "USER")
            .and()
            .withUser("user1")
            .password(passwordEncoder.encode("test"))
            .roles("USER");
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
}