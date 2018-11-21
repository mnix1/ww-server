package com.ww.config.security;

import com.ww.helper.EnvHelper;
import com.ww.model.entity.inside.social.Auto;
import com.ww.repository.inside.social.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.ww.config.security.ProdOAuthSecurityConfig.ONLY_ADMIN;
import static com.ww.config.security.Roles.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile(EnvHelper.SIGN_PROD)
@Order(1)
public class ProdBasicAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutoRepository autoRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                .requestMatchers()
                .antMatchers(ONLY_ADMIN)
                .and()
                .authorizeRequests()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic()
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            Optional<Auto> optionalAuto = autoRepository.findFirstByUsername(username);
            if (!optionalAuto.isPresent()) {
                throw new UsernameNotFoundException("not found");
            }
            Auto auto = optionalAuto.get();
            String roles = "ROLE_" + USER;
            if (auto.getAdmin()) {
                roles += ",ROLE_" + ADMIN;
            }
            if (auto.getAuto()) {
                roles += ",ROLE_" + AUTO;
            }
            return new User(username, auto.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
