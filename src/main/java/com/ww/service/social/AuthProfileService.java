package com.ww.service.social;

import com.ww.config.security.AuthIdProvider;
import com.ww.config.security.Roles;
import com.ww.helper.JSONHelper;
import com.ww.helper.TagHelper;
import com.ww.model.constant.Language;
import com.ww.model.constant.social.ProfileActionType;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.model.entity.outside.social.OutsideProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileAction;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.repository.inside.social.InsideProfileRepository;
import com.ww.repository.inside.social.OutsideProfileRepository;
import com.ww.repository.outside.social.ProfileActionRepository;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.service.social.IntroService.*;
import static com.ww.service.social.ProfileService.NAME_MAX_LENGTH;
import static com.ww.service.social.ProfileService.NAME_MIN_LENGTH;

@Service
@AllArgsConstructor
public class AuthProfileService {
    private static final Logger logger = LoggerFactory.getLogger(AuthProfileService.class);

    private final ProfileService profileService;
    private final ProfileWisieService profileWisieService;
    private final IntroService introService;
    private final ProfileActionRepository profileActionRepository;
    private final OutsideProfileRepository outsideProfileRepository;
    private final InsideProfileRepository insideProfileRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

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
                if (user instanceof UsernamePasswordAuthenticationToken) {
                    Collection<GrantedAuthority> authorities = ((UsernamePasswordAuthenticationToken) user).getAuthorities();
                    if (authorities.contains(new SimpleGrantedAuthority("ROLE_" + Roles.AUTO))
                            || authorities.contains(new SimpleGrantedAuthority("ROLE_" + Roles.ADMIN))) {
                        completeIntroductionForAuto(profile);
                    }
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

    private boolean validEmail(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
        }
        return false;
    }

    private boolean validUsername(String username) {
        if (username.length() > NAME_MAX_LENGTH || username.length() < NAME_MIN_LENGTH) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\\p{IsAlphabetic}\\d _]+$");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    @Transactional
    public Map<String, Object> createOutsideProfile(String username, String email, Language language) {
        Map<String, Object> model = new HashMap<>();
        if (!validUsername(username)) {
            return putCode(model, -2);
        }
        if (!validEmail(email)) {
            return putCode(model, -3);
        }
        Optional<OutsideProfile> optionalOutsideProfile = outsideProfileRepository.findFirstByUsernameOrEmail(username, email);
        if (optionalOutsideProfile.isPresent()) {
            OutsideProfile outsideProfile = optionalOutsideProfile.get();
            if (outsideProfile.getEmail().equals(email)) {
                return putCode(model, -4);
            }
            return putCode(model, -5);
        }
        if (insideProfileRepository.findFirstByUsername(username).isPresent()) {
            return putCode(model, -5);
        }
        OutsideProfile outsideProfile = new OutsideProfile(username, email);
        String password = TagHelper.randomUUID().substring(0, 10);
        outsideProfile.setPassword(passwordEncoder.encode(password));
        try {
            mailService.sendWelcomeEmail(email, username, password);
        } catch (Exception e) {
            logger.error(e.toString());
            return putCode(model, -6);
        }
        outsideProfileRepository.save(outsideProfile);
        profileService.saveProfileAndIntro(new Profile(AuthIdProvider.WISIEMANIA + AuthIdProvider.sepparator + username, username, email, language));
        return putSuccessCode(model);
    }
}