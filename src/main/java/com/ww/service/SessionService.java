package com.ww.service;

import com.ww.model.Session;
import com.ww.model.entity.outside.social.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionService implements Serializable {

    private Session session = new Session();

    public void storeProfile(Profile profile){
        setProfileId(profile.getId());
        setProfileTag(profile.getTag());
    }

    public void setProfileId(Long profileId) {
        session.setProfileId(profileId);
    }

    public void setProfileTag(String profileTag) {
        session.setProfileTag(profileTag);
    }

    public Long getProfileId() {
        return session.getProfileId();
    }

    public String getProfileTag() {
        return session.getProfileTag();
    }

    public void checkForProfileId(Long profileId) throws IllegalAccessException {
        if (!getProfileId().equals(profileId)) {
            throw new IllegalAccessException("Wrong profileId");
        }
    }

}
