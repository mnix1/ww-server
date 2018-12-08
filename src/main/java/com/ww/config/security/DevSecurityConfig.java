package com.ww.config.security;

import com.ww.helper.EnvHelper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.ww.config.security.ProdSecurityConfig.ALL;
import static com.ww.config.security.ProdSecurityConfig.ONLY_ADMIN;
import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.AUTO;
import static com.ww.config.security.Roles.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile(EnvHelper.SIGN_DEV)
@Order(SecurityProperties.IGNORED_ORDER)
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers(ALL).permitAll()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("dev").password("{noop}1").roles(USER, ADMIN, AUTO)
                .and()
                .withUser("adam").password("{noop}t").roles(USER, ADMIN, AUTO)
                .and()
                .withUser("adasio").password("{noop}t").roles(USER)
                .and()
                .withUser("iza").password("{noop}t").roles(USER, AUTO)
                .and()
                .withUser("lukasz").password("{noop}t").roles(USER, AUTO)
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
