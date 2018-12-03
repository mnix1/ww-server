package com.ww.service.auto;

import com.ww.config.security.AuthIdProvider;
import com.ww.model.constant.Language;
import com.ww.model.container.Connection;
import com.ww.model.entity.inside.social.InsideProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileIntro;
import com.ww.repository.inside.social.InsideProfileRepository;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class AutoProfileService {
    private static Logger logger = LoggerFactory.getLogger(AutoProfileService.class);

    private final InsideProfileRepository insideProfileRepository;
    private final AuthProfileService authProfileService;
    private final ProfileService profileService;
    private final ConnectionService connectionService;

    public Profile getNotLoggedAutoProfile() {
        InsideProfile insideProfile = randomElement(insideProfileRepository.findAllByAuto(true));
        String authId = AuthIdProvider.AUTO + AuthIdProvider.sepparator + insideProfile.getUsername();
        Optional<Profile> optionalProfile = profileService.retrieveProfile(authId);
        if (!optionalProfile.isPresent()) {
            return createAutoProfile(insideProfile, authId);
        }
        Profile profile = optionalProfile.get();
        Optional<Connection> optionalConnection = connectionService.findByProfileId(profile.getId());
        if (optionalConnection.isPresent()) {
            return getNotLoggedAutoProfile();
        }
        return profile;
    }

    private Profile createAutoProfile(InsideProfile insideProfile, String authId) {
        Profile profile = new Profile(authId, insideProfile.getUsername(), null, Language.POLISH, insideProfile.getWisorType());
        profileService.save(profile);
        profile.setIntro(new ProfileIntro(profile));
        authProfileService.completeIntroductionForAuto(profile);
        logger.debug("Created new auto profile=" + profile.toString());
        return profile;
    }


}
