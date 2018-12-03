package com.ww.service.social;

import com.ww.config.security.Roles;
import com.ww.helper.JSONHelper;
import com.ww.model.constant.social.ProfileActionType;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileAction;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.repository.outside.social.ProfileActionRepository;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.social.IntroService.*;

@Service
@AllArgsConstructor
public class AuthProfileService {
    private static final Logger logger = LoggerFactory.getLogger(AuthProfileService.class);

    private final ProfileService profileService;
    private final ProfileWisieService profileWisieService;
    private final IntroService introService;
    private final ProfileActionRepository profileActionRepository;

    @Transactional
    public ExtendedProfileResourcesDTO authProfile(Principal user) {
        String authId = profileService.getAuthId(user);
        logger.trace("authProfile authId=" + authId + ", string=" + user.toString() + ", json=" + JSONHelper.toJSON(user));
        if (authId != null) {
            Optional<Profile> optionalProfile = profileService.retrieveProfile(authId);
            Profile profile;
            ProfileAction profileAction;
            if (!optionalProfile.isPresent()) {
                profile = profileService.createProfile(user, authId);
                profileService.storeInSession(profile);
                if (user instanceof UsernamePasswordAuthenticationToken
                        && ((UsernamePasswordAuthenticationToken) user).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Roles.AUTO))) {
                    completeIntroductionForAuto(profile);
                }
                profileAction = new ProfileAction(ProfileActionType.SIGN_UP, profile);
            } else {
                profile = optionalProfile.get();
                profileService.storeInSession(profile);
                profileAction = new ProfileAction(ProfileActionType.SIGN_IN, profile);
            }
            profileActionRepository.save(profileAction);
            return new ExtendedProfileResourcesDTO(profile);
        }
        return null;
    }

    @Transactional
    public void completeIntroductionForAuto(Profile profile) {
        profile.getIntro().setIntroductionStepIndex(PICK_WISIES_INTRODUCTION_STEP_INDEX);
        introService.save(profile.getIntro());
        profileWisieService.experiment(profile);
        List<WisieType> wisieTypes = WisieType.list();
        Collections.shuffle(wisieTypes);
        List<ProfileWisie> profileWisies = profileWisieService.findAllNotInTeam(profile.getId());
        ProfileWisie fromExperiment = profileWisies.get(0);
        profile.setWisies(new HashSet<>(profileWisies));
        List<String> wisieStringTypes = wisieTypes.stream().filter(type -> fromExperiment.getType() != type)
                .limit(PICK_WISIES_COUNT).map(Enum::name).collect(Collectors.toList());
        introService.pickWisies(profile, wisieStringTypes);
        profileWisies = profileWisieService.findAllNotInTeam(profile.getId());
        profileWisies.stream().filter(profileWisie -> !profileWisie.getId().equals(fromExperiment.getId()))
                .limit(PICK_WISIES_COUNT)
                .forEach(profileWisie -> profileWisie.setInTeam(true));
        profileWisieService.save(profileWisies);
        profile.getIntro().setIntroductionStepIndex(END_INTRODUCTION_STEP_INDEX);
        profileService.save(profile);
        introService.save(profile.getIntro());
    }

}