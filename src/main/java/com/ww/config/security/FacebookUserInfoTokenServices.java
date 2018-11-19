package com.ww.config.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

public class FacebookUserInfoTokenServices extends UserInfoTokenServices {
    public FacebookUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication authentication = super.loadAuthentication(accessToken);
        ((Map) authentication.getUserAuthentication().getDetails()).put(AuthorizationServer.key, AuthorizationServer.FACEBOOK);
        return authentication;
    }
}
