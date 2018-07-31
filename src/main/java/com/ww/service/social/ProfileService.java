package com.ww.service.social;

import com.ww.model.entity.social.Profile;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SessionService sessionService;

    public Profile getProfileOnlyWithId() {
        return getProfileOnlyWithId(sessionService.getProfileId());
    }

    public Profile getProfileOnlyWithId(Long profileId) {
        return new Profile(profileId);
    }

    public Profile getProfile() {
        return getProfile(sessionService.getProfileId());
    }

    public Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId).get();
    }

    public Profile getProfile(String tag) {
        return profileRepository.findByTag(tag);
    }

    public List<Profile> getProfiles(List<Long> profileIds) {
        if (profileIds.isEmpty()) {
            return new ArrayList<>();
        }
        return profileRepository.findAllByIdIn(profileIds);
    }

    public String getAuthId(Principal user) {
        if (user == null) {
            return null;
        }
        if (user instanceof OAuth2Authentication) {
            Map<String, String> details = (Map<String, String>) ((OAuth2Authentication) user).getUserAuthentication().getDetails();
            return details.get("sub");
        }
        if (user instanceof UsernamePasswordAuthenticationToken) {
            return ((User) ((UsernamePasswordAuthenticationToken) user).getPrincipal()).getUsername();
        }
        return null;
    }

    public Profile createOrRetrieveProfile(String authId) {
        Profile profile = profileRepository.findByAuthId(authId);
        if (profile == null) {
            profile = new Profile(authId);
            profileRepository.save(profile);
        }
        return profile;
    }

    public Profile updateProfileName(String name) {
        Profile profile = getProfile();
        profile.setName(name);
        profileRepository.save(profile);
        return profile;
    }
}
