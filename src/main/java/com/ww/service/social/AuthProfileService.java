package com.ww.service.social;

import com.ww.config.security.Roles;
import com.ww.model.constant.social.ProfileActionType;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileAction;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.repository.outside.social.ProfileActionRepository;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.service.social.IntroService.PICK_WISIES_COUNT;
import static com.ww.service.social.IntroService.PICK_WISIES_INTRODUCTION_STEP_INDEX;

@Service
public class AuthProfileService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieService profileWisieService;

    @Autowired
    private IntroService introService;

    @Autowired
    private ProfileActionRepository profileActionRepository;

    @Transactional
    public ExtendedProfileResourcesDTO authProfile(Principal user) {
        String authId = profileService.getAuthId(user);
        if (authId != null) {
            Profile profile = profileService.retrieveProfile(authId);
            ProfileAction profileAction;
            if (profile == null) {
                profile = profileService.createProfile(user, authId);
                profileService.storeInSession(profile);
                if (user instanceof UsernamePasswordAuthenticationToken
                        && ((UsernamePasswordAuthenticationToken) user).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Roles.AUTO))) {
                    completeIntroductionForAuto(profile);
                }
                profileAction = new ProfileAction(ProfileActionType.SIGN_UP, profile);
            } else {
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
        profile.setIntroductionStepIndex(PICK_WISIES_INTRODUCTION_STEP_INDEX);
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
        profile.setIntroductionCompleted(true);
        profileService.save(profile);
    }

}