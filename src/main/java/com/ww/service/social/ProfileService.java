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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    public static final int WISOR_MIN_ID = 1;
    public static final int WISOR_MAX_ID = 48;
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 20;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SessionService sessionService;

    public Profile getActiveProfile() {
        //TODO add active check
        return profileRepository.findFirstByIdNot(sessionService.getProfileId());
    }

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

    public Map<String, Object> changeWisor(String wisor) {
        Map<String, Object> model = new HashMap<>();
        if (wisor.length() > ("wisor" + WISOR_MAX_ID).length() || !wisor.contains("wisor")) {
            model.put("code", -1);
            return model;
        }
        String idString = wisor.replace("wisor", "");
        int id = Integer.parseInt(idString);
        if (id < WISOR_MIN_ID || id > WISOR_MAX_ID) {
            model.put("code", -1);
            return model;
        }
        Profile profile = getProfile();
        String newWisorType = "wisor" + id;
        profile.setWisorType(newWisorType);
        save(profile);
        model.put("code", 1);
        model.put("wisorType", newWisorType);
        return model;
    }

    public Map<String, Object> changeName(String name) {
        Map<String, Object> model = new HashMap<>();
        name = name.trim();
        if (name.length() > NAME_MAX_LENGTH || name.length() < NAME_MIN_LENGTH) {
            model.put("code", -2);
            return model;
        }
        Pattern pattern = Pattern.compile("^(\\w+\\s)*\\w+$");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            model.put("code", -3);
            return model;
        }
        Profile profile = getProfile();
        profile.setName(name);
        save(profile);
        model.put("code", 1);
        model.put("name", name);
        return model;
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

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile updateProfileName(String name) {
        Profile profile = getProfile();
        profile.setName(name);
        profileRepository.save(profile);
        return profile;
    }
}
