package com.ww.config.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

import static com.ww.config.security.Roles.ADMIN;
import static com.ww.config.security.Roles.USER;

public class CustomAuthoritiesExtractor implements AuthoritiesExtractor {
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        String authorities = "ROLE_" + USER;
        if (map.get("email").equals("mvppl123@gmail.com")) {
            authorities += ",ROLE_" + ADMIN;
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }
}
