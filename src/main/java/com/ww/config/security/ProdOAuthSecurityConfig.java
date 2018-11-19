package com.ww.config.security;

import com.ww.helper.EnvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.AUTO;
import static com.ww.helper.EnvHelper.sslForce;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile(EnvHelper.SIGN_PROD)
@Order(2)
public class ProdOAuthSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String[] ALL = new String[]{"/", "/*.js", "/*.html", "/*.json", "/*.ico", "/*.png",
            "/profile", "/classification/war", "/classification/battle", "/play",
            "/war", "/warRanking", "/warFast", "/challenge", "/battle", "/battleRanking", "/battleFast", "/training", "/campaign", "/campaignWar",
            "/shop", "/friend", "/wisies", "/settings", "/_login/**", "/login", "/static/**", "/health/**", "/health"};
    public static final String[] ONLY_ADMIN = new String[]{"/**/*.map", "/h2/**", "/actuator/**", "/cache/**", "/log/**"};
    public static final String[] ONLY_AUTO = new String[]{"/auto/**", "/staticAuto/**"};

    @Autowired
    private Environment env;

    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    public void setOauth2ClientContext(OAuth2ClientContext oauth2ClientContext) {
        this.oauth2ClientContext = oauth2ClientContext;
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers(ALL).permitAll()
                .antMatchers(ONLY_ADMIN).hasAnyRole(ADMIN)
                .antMatchers(ONLY_AUTO).hasAnyRole(AUTO)
                .anyRequest().fullyAuthenticated()
                .and()
                .logout()
                .logoutUrl("/_logout")
                .logoutSuccessUrl("/").permitAll()
                .and()
                .addFilterAt(filter(), BasicAuthenticationFilter.class);
        if (sslForce(env)) {
            http.requiresChannel().antMatchers("/health/**", "/health").requiresInsecure().anyRequest().requiresSecure();
        }
    }

    private Filter filter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/_login/facebook");
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
        facebookFilter.setRestTemplate(facebookTemplate);
        UserInfoTokenServices tokenServices = new FacebookUserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
        tokenServices.setRestTemplate(facebookTemplate);
        facebookFilter.setTokenServices(tokenServices);
        filters.add(facebookFilter);

        OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/_login/google");
        OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
        githubFilter.setRestTemplate(githubTemplate);
        tokenServices = new GoogleUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(githubTemplate);
        githubFilter.setTokenServices(tokenServices);
        filters.add(githubFilter);

        filter.setFilters(filters);
        return filter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebook() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }
}
