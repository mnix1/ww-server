package com.ww.service.social;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.social.Profile;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

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

    public Profile getProfile() {
        return getProfile(sessionService.getProfileId());
    }

    public Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId).get();
    }

    public Profile getProfile(String tag) {
        return profileRepository.findByTag(tag);
    }

    public String getProfileTag(){
        return sessionService.getProfileTag();
    }

    public Map<String, Object> changeWisor(WisorType wisor) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = getProfile();
        profile.setWisorType(wisor);
        save(profile);
        model.put("wisorType", wisor);
        return putSuccessCode(model);
    }

    public Map<String, Object> changeName(String name) {
        Map<String, Object> model = new HashMap<>();
        name = name.trim();
        if (name.length() > NAME_MAX_LENGTH || name.length() < NAME_MIN_LENGTH) {
            return putCode(model, -2);
        }
        Pattern pattern = Pattern.compile("^(\\w+\\s)*\\w+$");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            return putCode(model, -3);
        }
        Profile profile = getProfile();
        profile.setName(name);
        save(profile);
        model.put("name", name);
        return putSuccessCode(model);
    }

    public String getAuthId(Principal user) {
        if (user == null) {
            return null;
        }
        if (user instanceof OAuth2Authentication) {
            Map<String, String> details = (Map<String, String>) ((OAuth2Authentication) user).getUserAuthentication().getDetails();
            return details.get("sub");
        } else if (user instanceof UsernamePasswordAuthenticationToken) {
            return ((User) ((UsernamePasswordAuthenticationToken) user).getPrincipal()).getUsername();
        }
        return null;
    }

    public Profile retrieveProfile(String authId) {
        return profileRepository.findByAuthId(authId);
    }

    public Profile createProfile(Principal user, String authId) {
        Profile profile = null;
        if (user instanceof OAuth2Authentication) {
            Map<String, String> details = (Map<String, String>) ((OAuth2Authentication) user).getUserAuthentication().getDetails();
            String name = details.get("name");
            profile = new Profile(authId, name.substring(0, Math.min(name.length(), NAME_MAX_LENGTH)), Language.fromLocale(details.get("locale")));
        } else if (user instanceof UsernamePasswordAuthenticationToken) {
            profile = new Profile(authId, authId, Language.POLISH);
        }
        profileRepository.save(profile);
        return profile;
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public List<Profile> classification(RivalType type) {
        if (type == RivalType.BATTLE) {
            return profileRepository.findAllByOrderByBattleEloDescBattlePreviousEloDescBattleLastPlayDesc();
        } else if (type == RivalType.WAR) {
            return profileRepository.findAllByOrderByWarEloDescWarPreviousEloDescWarLastPlayDesc();
        }
        throw new IllegalArgumentException();
    }
}
