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
import static com.ww.config.security.ProdOAuthSecurityConfig.ONLY_BOT;
import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.BOT;
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
                .antMatchers(ONLY_BOT)
                .and()
                .authorizeRequests()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .antMatchers(ONLY_BOT).hasAnyRole(BOT)
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        System.out.println(passwordEncoder().encode("!1botgrzesiu"));
        auth
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("admin1992").password("$2a$12$IZ.dAL3M92Q6XEk9K1YpY.878FBFZZb7qw.oFmXkre7aq8435Oa7u").roles(USER, ADMIN)
                .and()
                .withUser("grzesiu").password("$2a$12$FWkYznxIyRsDOUnhMAtNaep7mxUe1ggXlcAE/dnTXDDTjpeJoOYZW").roles(USER, BOT);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
