package com.ww.service.wisie;

import com.ww.helper.WisieHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.container.Resources;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.NumberHelper.smartRound;

@Service
public class ProfileWisieEvolutionService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieService profileWisieService;

    public synchronized Map<String, Object> upgradeAttribute(Long profileWisieId, Long profileId, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile(profileId);
        if (wisdomAttribute != null && profile.hasEnoughResources(WisdomAttribute.UPGRADE_COST)) {
            profile.subtractResources(WisdomAttribute.UPGRADE_COST);
        } else if (mentalAttribute != null && profile.hasEnoughResources(MentalAttribute.UPGRADE_COST)) {
            profile.subtractResources(MentalAttribute.UPGRADE_COST);
        } else {
            return putErrorCode(model);
        }
        Optional<ProfileWisie> optionalProfileWisie = profileWisieService.findByIdAndProfileId(profileWisieId, profile.getId());
        if (!optionalProfileWisie.isPresent()) {
            return putErrorCode(model);
        }
        ProfileWisie profileWisie = optionalProfileWisie.get();
        double attributeChange = calculateAttributeChange(profileWisie, wisdomAttribute, mentalAttribute);
        if (wisdomAttribute != null) {
            profileWisie.upgradeWisdomAttribute(wisdomAttribute, attributeChange);
        } else if (mentalAttribute != null) {
            profileWisie.upgradeMentalAttribute(mentalAttribute, attributeChange);
        }
        profileService.save(profile);
        profileWisieService.save(profileWisie);
        model.put("attributeChange", smartRound(attributeChange));
        model.put("profileWisie", new ProfileWisieDTO(profileWisie));
        model.put("profile", new ExtendedProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    private double calculateAttributeChange(ProfileWisie profileWisie, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        double value = 0;
        if (wisdomAttribute != null) {
            value = profileWisie.getWisdomAttributeValue(wisdomAttribute);
        } else if (mentalAttribute != null) {
            value = profileWisie.getMentalAttributeValue(mentalAttribute);
        }
        double f = WisieHelper.f1(value);
        double k = 1 - f;
        return value * Math.pow(k, 6) + k;
    }

    public synchronized Map<String, Object> changeHobby(Long profileWisieId, String hobby, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile(profileId);
        Optional<ProfileWisie> optionalProfileWisie = profileWisieService.findByIdAndProfileId(profileWisieId, profileId);
        if (!optionalProfileWisie.isPresent()) {
            return putErrorCode(model);
        }
        ProfileWisie profileWisie = optionalProfileWisie.get();
        Resources changeHobbyCostResources = changeHobbyCostResources(profileWisie);
        if (!profile.hasEnoughResources(changeHobbyCostResources)) {
            return putErrorCode(model);
        }
        changeHobby(profileWisie, hobby == null ? null : Category.fromString(hobby));
        profile.subtractResources(changeHobbyCostResources);
        profileService.save(profile);
        profileWisieService.save(profileWisie);
        model.put("profileWisie", new ProfileWisieDTO(profileWisie));
        model.put("profile", new ExtendedProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    public synchronized Map<String, Object> changeSkill(Long profileWisieId, Skill skill) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        Optional<ProfileWisie> optionalProfileWisie = profileWisieService.findByIdAndProfileId(profileWisieId, profile.getId());
        if (!optionalProfileWisie.isPresent()) {
            return putErrorCode(model);
        }
        ProfileWisie profileWisie = optionalProfileWisie.get();
        Resources changeSkillCostResources = changeSkillCostResources();
        if (!profile.hasEnoughResources(changeSkillCostResources)) {
            return putErrorCode(model);
        }
        changeSkill(profileWisie, skill);
        profile.subtractResources(changeSkillCostResources);
        profileService.save(profile);
        profileWisieService.save(profileWisie);
        model.put("profileWisie", new ProfileWisieDTO(profileWisie));
        model.put("profile", new ExtendedProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    private Resources changeHobbyCostResources(ProfileWisie profleWisie) {
        return new Resources(null, (long) profleWisie.getHobbies().size() * 20, null, (long) profleWisie.getHobbies().size() * 10);
    }

    private Resources changeSkillCostResources() {
        return new Resources(null, 50L, null, 25L);
    }

    private void changeHobby(ProfileWisie profileWisie, Category hobby) {
        Set<Category> hobbies = profileWisie.getHobbies();
        List<Category> hobbyList = new ArrayList<>(hobbies);
        if (hobbies.size() == ProfileWisie.MAX_HOBBY_COUNT) {
            if (hobbies.contains(hobby)) {
                hobbies.remove(hobby);
            } else {
                hobbies.remove(hobbyList.get(0));
            }
        }
        Category newHobby = Category.next(hobby);
        boolean added = false;
        while (!added) {
            if (hobby == newHobby) {
                newHobby = Category.random();
                continue;
            }
            added = hobbies.add(newHobby);
            newHobby = Category.next(newHobby);
        }
    }

    private void changeSkill(ProfileWisie profileWisie, Skill skill) {
        Set<Skill> skills = profileWisie.getSkills();
        if (!skills.contains(skill)) {
            return;
        }
        skills.remove(skill);
        skills.add(Skill.next(skill));
    }
}
