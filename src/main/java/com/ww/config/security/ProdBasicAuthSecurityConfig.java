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

import static com.ww.config.security.ProdOAuthSecurityConfig.ALL;
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
        auth
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("admin1992").password("$2a$12$IZ.dAL3M92Q6XEk9K1YpY.878FBFZZb7qw.oFmXkre7aq8435Oa7u").roles(USER, ADMIN)
                .and().withUser("grzesiu").password("$2a$12$FWkYznxIyRsDOUnhMAtNaep7mxUe1ggXlcAE/dnTXDDTjpeJoOYZW").roles(USER, BOT)
                .and().withUser("speedy").password("$2a$12$jHEo72aiAEMLeHGjAyrOreHNVmaGjd9nvd50heR2RGqmeXG/zd89W").roles(USER, BOT)
                .and().withUser("Razerox").password("$2a$12$r/Gze/VQwZyWvaORFXFnMebmR.JCggWCmgLwrr6906E.2EEjTtpmu").roles(USER, BOT)
                .and().withUser("W4X").password("$2a$12$IFW3lKneTvdSpdIfBCTHJuhirJnkPPk3XSEQCDDhVZarDfFpkLOT6").roles(USER, BOT)
                .and().withUser("pierdołła").password("$2a$12$WP3DTcZlbvuT3MDyQ.5A1uxhaz9HRUkvJ7DqsBalaoHGRZtbQc4Jm").roles(USER, BOT)
                .and().withUser("Kanar").password("$2a$12$RT8mOMC49Ix5Y6OUv9EE1.VYg3jxKIHrUGq0NTiw2gXnkUJ769U0O").roles(USER, BOT)
                .and().withUser("Best19").password("$2a$12$QQjb7ioHCwdEP7ny2FYjIOAO2B4wyrgqtX5UTbq7A.g/vhALLXL5G").roles(USER, BOT)
                .and().withUser("xxULAxx").password("$2a$12$9lESyDHvv.pAFx4mDVrVuObnayLTdfhk1R9ymzHgeRkDX7DoI/97a").roles(USER, BOT)
                .and().withUser("qq5").password("$2a$12$W5yVWWIpDgvRoBndMYpThus4NoYPQwwpd2PmB2SEd2/clug5VnNfW").roles(USER, BOT)
                .and().withUser("radosny1982").password("$2a$12$lW68bmdBlWzJ87KeuLu1HetuB8kBc7A1w98CJ8valtBtx5vhOSDoW").roles(USER, BOT);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
