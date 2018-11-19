package com.ww.service.auto;

import com.ww.config.security.AuthIdProvider;
import com.ww.model.constant.Language;
import com.ww.model.container.Connection;
import com.ww.model.entity.inside.social.Auto;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.inside.social.AutoRepository;
import com.ww.repository.outside.social.ProfileRepository;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutoProfileService {
    private static Logger logger = LoggerFactory.getLogger(AutoProfileService.class);

    private final AutoRepository autoRepository;
    private final AuthProfileService authProfileService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final ConnectionService connectionService;

    public Optional<Profile> getNotLoggedAutoProfile() {
        List<Profile> profiles = profileRepository.findAllByAuthIdContains(AuthIdProvider.AUTO.name());
        if (profiles.isEmpty()) {
            return createAutoProfile(profiles);
        }
        Collections.shuffle(profiles);
        for (Profile profile : profiles) {
            Optional<Connection> optionalConnection = connectionService.findByProfileId(profile.getId());
            if (!optionalConnection.isPresent()) {
                return Optional.of(profile);
            }
        }
        return createAutoProfile(profiles);
    }

    private Optional<Profile> createAutoProfile(List<Profile> profiles) {
        Set<String> usedAutoAuthIds = profiles.stream().map(Profile::getAuthId).collect(Collectors.toSet());
        List<Auto> autos = autoRepository.findAllByAuto(true);
        Collections.shuffle(autos);
        for (Auto auto : autos) {
            String authId = AuthIdProvider.AUTO + AuthIdProvider.sepparator + auto.getUsername();
            if (usedAutoAuthIds.contains(authId)) {
                continue;
            }
            Profile profile = new Profile(authId, auto.getUsername(), null, Language.POLISH);
            profileService.save(profile);
            authProfileService.completeIntroductionForAuto(profile);
            logger.error("Created new auto profile=" + profile.toString());
            return Optional.of(profile);
        }
        logger.error("Can't create new auto profile, all are used");
        return Optional.empty();
    }


}
