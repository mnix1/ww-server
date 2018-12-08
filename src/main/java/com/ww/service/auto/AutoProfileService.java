package com.ww.service.auto;

import com.ww.config.security.AuthIdProvider;
import com.ww.model.constant.Language;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.entity.inside.social.InsideProfile;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileIntro;
import com.ww.repository.inside.social.InsideProfileRepository;
import com.ww.repository.outside.social.ProfileRepository;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class AutoProfileService {
    private static Logger logger = LoggerFactory.getLogger(AutoProfileService.class);

    private final InsideProfileRepository insideProfileRepository;
    private final AuthProfileService authProfileService;
    private final ProfileService profileService;
    private final RivalProfileSeasonService rivalProfileSeasonService;
    private final RivalSeasonService rivalSeasonService;

    public Profile getNotLoggedAutoProfile() {
        InsideProfile insideProfile = randomElement(insideProfileRepository.findAllByAuto(true));
        String authId = AuthIdProvider.AUTO + AuthIdProvider.sepparator + insideProfile.getUsername();
        Optional<Profile> optionalProfile = profileService.retrieveProfile(authId);
        if (!optionalProfile.isPresent()) {
            return createAutoProfile(insideProfile, authId);
        }
        Profile profile = optionalProfile.get();
        if (AutoService.activeAutoManagersMap.containsKey(profile.getId())) {
            return getNotLoggedAutoProfile();
        }
        return profile;
    }

    public Profile getNotLoggedAutoProfile(RivalOneInit init) {
        Profile profile = init.getCreatorProfile();
        Season season = rivalSeasonService.actual(init.getType());
        List<ProfileSeason> profileSeasons = rivalProfileSeasonService.findProfileSeasons(season.getId());
        ProfileSeason profileSeason = profileSeasons.stream().filter(pS -> pS.getProfile().getId().equals(profile.getId())).findFirst().orElse(new ProfileSeason());
        List<Profile> possibleOpponents = profileSeasons.stream().filter(pS -> pS.getProfile().getAuthId().startsWith(AuthIdProvider.AUTO.name()) && Math.abs(profileSeason.getElo() - pS.getElo()) <= 50).map(ProfileSeason::getProfile).collect(Collectors.toList());
        for (Profile possibleOpponent : possibleOpponents) {
            if (!AutoService.activeAutoManagersMap.containsKey(possibleOpponent.getId())) {
                return possibleOpponent;
            }
        }
        return getNotLoggedAutoProfile();
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
