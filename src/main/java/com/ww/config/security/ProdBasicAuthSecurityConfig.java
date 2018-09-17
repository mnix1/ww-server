package com.ww.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.ww.config.security.ProdOAuthSecurityConfig.ONLY_ADMIN;
import static com.ww.config.security.ProdOAuthSecurityConfig.ONLY_AUTO;
import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.AUTO;
import static com.ww.config.security.Roles.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile("prod")
@Order(1)
public class ProdBasicAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                .requestMatchers()
                .antMatchers(ONLY_ADMIN)
                .antMatchers(ONLY_AUTO)
                .and()
                .authorizeRequests()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .antMatchers(ONLY_AUTO).hasAnyRole(AUTO)
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic()
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("admin1992").password("$2a$12$IZ.dAL3M92Q6XEk9K1YpY.878FBFZZb7qw.oFmXkre7aq8435Oa7u").roles(USER, ADMIN)
                .and().withUser("grzesiu").password("$2a$12$FWkYznxIyRsDOUnhMAtNaep7mxUe1ggXlcAE/dnTXDDTjpeJoOYZW").roles(USER, AUTO)//1
                .and().withUser("speedy").password("$2a$12$jHEo72aiAEMLeHGjAyrOreHNVmaGjd9nvd50heR2RGqmeXG/zd89W").roles(USER, AUTO)//2
                .and().withUser("Razerox").password("$2a$12$r/Gze/VQwZyWvaORFXFnMebmR.JCggWCmgLwrr6906E.2EEjTtpmu").roles(USER, AUTO)//3
                .and().withUser("W4X").password("$2a$12$6hWQTbA82eM8Y/0b5YVJ6u/R7dXg2iSE6vXQ/WRa5.EOfZDKvXs0u").roles(USER, AUTO)//4
                .and().withUser("pierdołła").password("$2a$12$pTN8lgRsX3HvKBh3Et8oZuL2DoCZgeWJe.AK76P1hgaq/TDlj6hmW").roles(USER, AUTO)//5
                .and().withUser("Kanar").password("$2a$12$vpAvP5.zyxn.wVXrIHDis.A10tXCKOIHjE//0HlCjNpBFR3qXvGsO").roles(USER, AUTO)//6
                .and().withUser("Best19").password("$2a$12$YbnhHkeln6QJRAXJ9niF8Ox0ZEQxld6k4rEduzOd/huIcuBTNa8oW").roles(USER, AUTO)//7
                .and().withUser("xxULAxx").password("$2a$12$rW6uQC3/MjWluxmOPHWxgeqLUN75IShoqSPVvbs2B06lGTRSZYK7C").roles(USER, AUTO)//8
                .and().withUser("qq5").password("$2a$12$Qh8lR5x4gQKajG4W9jxxneF7wYL.ybSKLQ2kCXIxA16gA1aySMfCq").roles(USER, AUTO)//9
                .and().withUser("radosny1982").password("$2a$12$EpEeVEvYMsUDaL8Tw1.uvOAeQqFRJJ/b9CFKTurCWBThIheCL2SVC").roles(USER, AUTO);//10
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
