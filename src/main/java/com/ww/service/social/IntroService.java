package com.ww.service.social;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.wisie.Wisie;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.wisie.WisieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class IntroService {
    private static final Logger logger = LoggerFactory.getLogger(IntroService.class);

    public static final Integer PICK_WISIES_COUNT = 4;
    public static final Integer PROFILE_WISIES_COUNT = 1;
    public static final Integer PICK_WISIES_INTRODUCTION_STEP_INDEX = 11;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieService profileWisieService;

    @Autowired
    private WisieService wisieService;

    public Map<String, Object> changeIntroStepIndex(Integer stepIndex) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        if (stepIndex <= profile.getIntroductionStepIndex()) {
            return putErrorCode(model);
        }
        profile.setIntroductionStepIndex(stepIndex);
        profileService.save(profile);
        model.put("profile", new ProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    public synchronized Map<String, Object> pickWisies(List<String> wisieStringTypes) {
        Map<String, Object> model = new HashMap<>();
        if (wisieStringTypes.size() != PICK_WISIES_COUNT) {
            return putErrorCode(model);
        }
        List<WisieType> wisieTypes = wisieStringTypes.stream().map(WisieType::fromString).filter(Objects::nonNull).collect(Collectors.toList());
        if (wisieTypes.size() != PICK_WISIES_COUNT) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (!profile.getIntroductionStepIndex().equals(PICK_WISIES_INTRODUCTION_STEP_INDEX)
                || profile.getWisies().size() != PROFILE_WISIES_COUNT) {
            logger.error("Hacker trying to get extra wisies, profileId: {}", profile.getId());
            return putErrorCode(model);
        }
        for (ProfileWisie profileWisie : profile.getWisies()) {
            if (wisieTypes.contains(profileWisie.getWisie().getType())) {
                logger.error("Trying to add wisie that profile already has, profileId: {}", profile.getId());
                return putErrorCode(model);
            }
        }
        List<Wisie> wisies = wisieService.getWisies(wisieTypes);
        wisies.sort(Comparator.comparingInt(o -> wisieTypes.indexOf(o.getType())));
        List<Category> categories = Category.list();
        Collections.shuffle(categories);
        categories = categories.subList(0, PICK_WISIES_COUNT);
        List<ProfileWisie> profileWisies = new ArrayList<>(PICK_WISIES_COUNT);
        for (int i = 0; i < PICK_WISIES_COUNT; i++) {
            ProfileWisie profileWisie = profileWisieService.createWisie(profile, wisies.get(i));
            profileWisieService.initWisieHobbies(profileWisie, Arrays.asList(categories.get(i)));
            profileWisieService.upgradeWisie(profileWisie, ((PICK_WISIES_COUNT - 1) - i) * 50);
            profileWisies.add(profileWisie);
        }
        profile.setIntroductionStepIndex(profile.getIntroductionStepIndex() + 1);
        profileWisieService.save(profileWisies);
        profileService.save(profile);
        return putSuccessCode(model);
    }

}
