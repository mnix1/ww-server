package com.ww.service.social;

import com.ww.config.security.Roles;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

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

    public ProfileResourcesDTO authProfile(Principal user) {
        String authId = profileService.getAuthId(user);
        if (authId != null) {
            Profile profile = profileService.retrieveProfile(authId);
            if (profile == null) {
                profile = profileService.createProfile(user, authId);
                profileService.storeInSession(profile);
                if (user instanceof UsernamePasswordAuthenticationToken
                        && ((UsernamePasswordAuthenticationToken) user).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Roles.AUTO))) {
                    completeIntroductionForAuto(profile);
                }
            } else {
                profileService.storeInSession(profile);
            }
            return new ProfileResourcesDTO(profile);
        }
        return null;
    }


    public void completeIntroductionForAuto(Profile profile) {
        profile.setIntroductionStepIndex(PICK_WISIES_INTRODUCTION_STEP_INDEX);
        profileWisieService.experiment(profile);
        List<WisieType> wisieTypes = WisieType.list();
        Collections.shuffle(wisieTypes);
        List<ProfileWisie> profileWisies = profileWisieService.findAllNotInTeam(profile.getId());
        ProfileWisie fromExperiment = profileWisies.get(0);
        profile.setWisies(new HashSet<>(profileWisies));
        List<String> wisieStringTypes = wisieTypes.stream().filter(type -> fromExperiment.getWisie().getType() != type)
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