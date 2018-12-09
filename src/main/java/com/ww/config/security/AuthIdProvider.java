package com.ww.config.security;

import com.ww.model.entity.outside.social.Profile;

public enum AuthIdProvider {
    AUTO,
    ADMIN,
    WISIEMANIA,
    FACEBOOK,
    GOOGLE;

    public static final String key = "authIdProvider";
    public static final String sepparator = "^";

    public static boolean isAutoProfile(Profile profile) {
        if (profile == null) {
            return false;
        }
        return profile.getAuthId().contains(AUTO.name());
    }

    public static boolean isHumanProfile(Profile profile) {
        if (profile == null) {
            return false;
        }
        return !isAutoProfile(profile);
    }
}
