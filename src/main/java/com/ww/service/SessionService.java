package com.ww.service;

import com.ww.model.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionService {

    private Session session = new Session();

    public void setProfileId(Long profileId) {
        session.setProfileId(profileId);
    }

    public Long getProfileId() {
        return session.getProfileId();
    }

    public void checkForProfileId(Long profileId) throws IllegalAccessException {
        if (!getProfileId().equals(profileId)) {
            throw new IllegalAccessException("Wrong profileId");
        }
    }

}
