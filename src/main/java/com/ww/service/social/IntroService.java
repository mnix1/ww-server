package com.ww.service.social;

import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ProfileIntroDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileIntro;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.repository.outside.social.ProfileIntroRepository;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class IntroService {
    private static final Logger logger = LoggerFactory.getLogger(IntroService.class);
    public static final Integer PICK_WISIES_COUNT = 4;
    public static final Integer PROFILE_WISIES_COUNT = 1;
    public static final Integer PICK_WISIES_INTRODUCTION_STEP_INDEX = 11;
    public static final Integer END_INTRODUCTION_STEP_INDEX = 15;

    private final ProfileIntroRepository profileIntroRepository;
    private final ProfileService profileService;
    private final ProfileWisieService profileWisieService;

    @Transactional
    public Map<String, Object> changeIntroStepIndex(Integer introductionStepIndex) {
        Map<String, Object> model = new HashMap<>();
        ProfileIntro intro = profileIntroRepository.findByProfile_Id(profileService.getProfileId()).orElseThrow(IllegalAccessError::new);
        if (introductionStepIndex <= intro.getIntroductionStepIndex()) {
            return putErrorCode(model);
        }
        intro.setIntroductionStepIndex(introductionStepIndex);
        profileIntroRepository.save(intro);
        model.put("intro", new ProfileIntroDTO(intro));
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> pickWisies(Profile profile, List<String> wisieStringTypes) {
        Map<String, Object> model = new HashMap<>();
        if (wisieStringTypes.size() != PICK_WISIES_COUNT) {
            return putErrorCode(model);
        }
        List<WisieType> wisieTypes = wisieStringTypes.stream().map(WisieType::fromString).filter(Objects::nonNull).collect(Collectors.toList());
        if (wisieTypes.size() != PICK_WISIES_COUNT) {
            return putErrorCode(model);
        }
        if (profile == null) {
            profile = profileService.getProfile();
        }
        ProfileIntro intro = profileIntroRepository.findByProfile_Id(profile.getId()).orElseThrow(IllegalAccessError::new);
        if (!intro.getIntroductionStepIndex().equals(PICK_WISIES_INTRODUCTION_STEP_INDEX)
                || profile.getWisies().size() != PROFILE_WISIES_COUNT) {
            logger.error("Hacker trying to get extra wisies, profileId: {}", profile.getId());
            return putErrorCode(model);
        }
        for (ProfileWisie profileWisie : profile.getWisies()) {
            if (wisieTypes.contains(profileWisie.getType())) {
                logger.error("Trying to add wisie that profile already has, profileId: {}", profile.getId());
                return putErrorCode(model);
            }
        }
        List<Category> categories = Category.list();
        Collections.shuffle(categories);
        List<Skill> skills = Skill.list();
        Collections.shuffle(skills);
        List<ProfileWisie> profileWisies = new ArrayList<>(PICK_WISIES_COUNT);
        for (int i = 0; i < PICK_WISIES_COUNT; i++) {
            ProfileWisie profileWisie = profileWisieService.createWisie(profile, wisieTypes.get(i));
            profileWisieService.initWisieHobbies(profileWisie, Arrays.asList(categories.get(i)));
            profileWisieService.initWisieSkills(profileWisie, Arrays.asList(skills.get(i)));
            profileWisies.add(profileWisie);
        }
        intro.setIntroductionStepIndex(intro.getIntroductionStepIndex() + 1);
        profileWisieService.save(profileWisies);
        profileIntroRepository.save(intro);
        return putSuccessCode(model);
    }

}
