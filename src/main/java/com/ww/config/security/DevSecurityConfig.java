package com.ww.config.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile("dev")
@Order(SecurityProperties.IGNORED_ORDER)
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/h2**").hasRole(ADMIN)
                .anyRequest().fullyAuthenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("dev").password("{noop}1").roles(USER, ADMIN)
                .and()
                .withUser("t1").password("{noop}t").roles(USER)
                .and()
                .withUser("t2").password("{noop}t").roles(USER)
                .and()
                .withUser("adam").password("{noop}t").roles(USER)
                .and()
                .withUser("iza").password("{noop}t").roles(USER)
                .and()
                .withUser("lukasz").password("{noop}t").roles(USER)
                .and()
                .withUser("krzysiek").password("{noop}t").roles(USER)
                .and()
                .withUser("diana").password("{noop}t").roles(USER)
                .and()
                .withUser("marta").password("{noop}t").roles(USER)
                .and()
                .withUser("malgosia").password("{noop}t").roles(USER)
                .and()
                .withUser("stasiu").password("{noop}t").roles(USER)
                .and()
                .withUser("tata").password("{noop}t").roles(USER)
                .and()
                .withUser("mama").password("{noop}t").roles(USER);

    }
}
