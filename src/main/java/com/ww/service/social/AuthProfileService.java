package com.ww.service.social;

import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.wisie.WisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AuthProfileService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    public ProfileResourcesDTO authProfile(Principal user){
        String authId = profileService.getAuthId(user);
        if (authId != null) {
            Profile profile = profileService.retrieveProfile(authId);
            if (profile == null) {
                profile = profileService.createProfile(user, authId);
            }
            sessionService.setProfileId(profile.getId());
            sessionService.setProfileTag(profile.getTag());
            return new ProfileResourcesDTO(profile);
        }
        return null;
    }

}
