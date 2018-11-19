package com.ww.config.security;

public enum AuthIdProvider {
    AUTO,

    FACEBOOK,
    GOOGLE;

    public static final String key = "authIdProvider";
    public static final String sepparator = "^";
}
