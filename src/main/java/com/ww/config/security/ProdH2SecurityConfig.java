package com.ww.config.security;

import com.ww.helper.EnvHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.ww.config.security.Roles.ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile(EnvHelper.SIGN_PROD)
@Order(1)
public class ProdH2SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                .requestMatchers()
                .antMatchers("/_h2/**")
                .and()
                .authorizeRequests()
                .antMatchers("/_h2/**").hasAnyRole(ADMIN)
                .and()
                .httpBasic()
                .and().csrf().disable();
    }
}
