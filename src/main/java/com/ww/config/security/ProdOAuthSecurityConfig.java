package com.ww.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.BOT;
import static com.ww.config.security.Roles.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile("prod")
@Order(2)
public class ProdOAuthSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String[] ALL = new String[]{"/", "/profile", "/play", "/war", "/challenge", "/battle", "/practise",
            "/shop", "/friend", "/wisies", "/login/**", "/static/**", "/actuator/health"};
    public static final String[] ONLY_ADMIN = new String[]{"/**/*.map", "/h2/**", "/actuator/**", "/cache/**", "/log/**"};
    public static final String[] ONLY_BOT = new String[]{"/bot/**", "/static/js/main.bot.js"};

    private OAuth2ClientContext oauth2ClientContext;
    private AuthorizationCodeResourceDetails authorizationCodeResourceDetails;
    private ResourceServerProperties resourceServerProperties;

    @Autowired
    public void setOauth2ClientContext(OAuth2ClientContext oauth2ClientContext) {
        this.oauth2ClientContext = oauth2ClientContext;
    }

    @Autowired
    public void setAuthorizationCodeResourceDetails(AuthorizationCodeResourceDetails authorizationCodeResourceDetails) {
        this.authorizationCodeResourceDetails = authorizationCodeResourceDetails;
    }

    @Autowired
    public void setResourceServerProperties(ResourceServerProperties resourceServerProperties) {
        this.resourceServerProperties = resourceServerProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers(ALL).permitAll()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .antMatchers(ONLY_BOT).hasAnyRole(BOT)
                .anyRequest().fullyAuthenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/").permitAll()
                .and()
                .addFilterAt(filter(), BasicAuthenticationFilter.class);
    }

    private OAuth2ClientAuthenticationProcessingFilter filter() {
        OAuth2ClientAuthenticationProcessingFilter oAuth2Filter = new OAuth2ClientAuthenticationProcessingFilter(
                "/login/google");
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails,
                oauth2ClientContext);
        oAuth2Filter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
//        tokenServices.setAuthoritiesExtractor(new CustomAuthoritiesExtractor());
        oAuth2Filter.setTokenServices(tokenServices);
        return oAuth2Filter;
    }
}
