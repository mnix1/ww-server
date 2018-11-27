package com.ww.service.social;

import com.ww.config.security.AuthIdProvider;
import com.ww.config.security.Roles;
import com.ww.model.constant.Language;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.social.ProfileRepository;
import com.ww.service.SessionService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ProfileService {
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 20;

    private final ProfileRepository profileRepository;
    private final SessionService sessionService;

    public Profile getProfile() {
        return getProfile(sessionService.getProfileId());
    }

    public Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId).get();
    }

    public Profile getProfile(String tag) {
        return profileRepository.findByTag(tag);
    }

    public String getProfileTag() {
        return sessionService.getProfileTag();
    }

    public Language getProfileLanguage() {
        return sessionService.getProfileLanguage();
    }

    public Long getProfileId() {
        return sessionService.getProfileId();
    }

    public Map<String, Object> delete() {
        Profile profile = getProfile();
        profile.setAuthId("DELETED_" + profile.getAuthId());
        save(profile);
        return putSuccessCode(new HashMap<>());
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
        Pattern pattern = Pattern.compile("^[\\p{IsAlphabetic}\\d _]+$");
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
            Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) user).getUserAuthentication().getDetails();
            AuthIdProvider authIdProvider = (AuthIdProvider) details.get(AuthIdProvider.key);
            if (authIdProvider.equals(AuthIdProvider.GOOGLE)) {
                return getGoogleAuthId(details);
            } else if (authIdProvider.equals(AuthIdProvider.FACEBOOK)) {
                return getFacebookAuthId(details);
            }
        } else if (user instanceof UsernamePasswordAuthenticationToken) {
            String username = ((User) ((UsernamePasswordAuthenticationToken) user).getPrincipal()).getUsername();
            String authId = username;
            if (((UsernamePasswordAuthenticationToken) user).getAuthorities().contains(Roles.AUTO)) {
                authId = AuthIdProvider.AUTO + AuthIdProvider.sepparator + username;
            }
            return authId;
        }
        return null;
    }

    public String getGoogleAuthId(Map<String, Object> details) {
        return AuthIdProvider.GOOGLE + AuthIdProvider.sepparator + details.get("sub");
    }

    public String getFacebookAuthId(Map<String, Object> details) {
        return AuthIdProvider.FACEBOOK + AuthIdProvider.sepparator + details.get("id");
    }

    public void storeInSession(Profile profile) {
        sessionService.storeProfile(profile);
    }

    public Profile retrieveProfile(String authId) {
        return profileRepository.findByAuthId(authId);
    }

    public Profile createProfile(Principal user, String authId) {
        Profile profile = null;
        if (user instanceof OAuth2Authentication) {
            Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) user).getUserAuthentication().getDetails();
            AuthIdProvider authIdProvider = (AuthIdProvider) details.get(AuthIdProvider.key);
            if (authIdProvider == AuthIdProvider.GOOGLE) {
                profile = createProfileGoogle(details, authId);
            } else if (authIdProvider == AuthIdProvider.FACEBOOK) {
                profile = createProfileFacebook(details, authId);
            }
        } else if (user instanceof UsernamePasswordAuthenticationToken) {
            profile = new Profile(authId, authId, null, Language.POLISH);
        }
        profileRepository.save(profile);
        return profile;
    }

    public Profile createProfileGoogle(Map<String, Object> details, String authId) {
        String name = (String) details.get("name");
        name = name.substring(0, Math.min(name.length(), NAME_MAX_LENGTH));
        return new Profile(authId, name, (String) details.get("email"), Language.fromLocale((String) details.get("locale")));
    }

    public Profile createProfileFacebook(Map<String, Object> details, String authId) {
        String name = (String) details.get("name");
        name = name.substring(0, Math.min(name.length(), NAME_MAX_LENGTH));
        return new Profile(authId, name, (String) details.get("email"), Language.ENGLISH);
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public void save(List<Profile> profiles) {
        profileRepository.saveAll(profiles);
    }
}
