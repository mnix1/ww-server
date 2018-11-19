package com.ww.service.auto;

import com.ww.config.security.AuthIdProvider;
import com.ww.model.constant.Language;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.inside.social.Auto;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.inside.social.AutoRepository;
import com.ww.repository.outside.social.ProfileRepository;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AutoProfileService {
    private final AutoRepository autoRepository;
    private final AuthProfileService authProfileService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final ProfileConnectionService profileConnectionService;

    public Profile getNotLoggedAutoProfile() {
        List<Profile> profiles = profileRepository.findAllByAuthIdContains(AuthIdProvider.AUTO.name());
        if (profiles.isEmpty()) {
            return createAutoProfile();
        }
        Collections.shuffle(profiles);
        for (Profile profile : profiles) {
            Optional<ProfileConnection> optionalProfileConnection = profileConnectionService.findByProfileId(profile.getId());
            if (!optionalProfileConnection.isPresent()) {
                return profile;
            }
        }
        return createAutoProfile();
    }

    private Profile createAutoProfile() {
        Auto auto = autoRepository.findAllByAuto(true);
        String authId = AuthIdProvider.AUTO + AuthIdProvider.sepparator + auto.getUsername();
        Profile profile = new Profile(authId, auto.getUsername(), null, Language.POLISH);
        profileService.save(profile);
        authProfileService.completeIntroductionForAuto(profile);
        return profile;
    }


}
