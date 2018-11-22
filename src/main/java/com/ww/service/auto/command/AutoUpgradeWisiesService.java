package com.ww.service.auto.command;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.wisie.ProfileWisieEvolutionService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ww.helper.ModelHelper.success;
import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class AutoUpgradeWisiesService {

    private final ProfileWisieService profileWisieService;
    private final ProfileWisieEvolutionService profileWisieEvolutionService;

    public void upgrade(Profile profile) {
        List<ProfileWisie> wisies = profileWisieService.findAllInTeam(profile.getId());
        while (upgrade(randomElement(wisies))) {
        }
        addHobby(randomElement(wisies));
    }

    public boolean upgrade(ProfileWisie wisie) {
        WisdomAttribute wisdomAttribute = WisdomAttribute.random();
        MentalAttribute mentalAttribute = MentalAttribute.random();
        if (randomDouble() > 0.5) {
            mentalAttribute = null;
        } else {
            wisdomAttribute = null;
        }
        return success(profileWisieEvolutionService.upgradeAttribute(wisie.getId(), wisie.getProfile().getId(), wisdomAttribute, mentalAttribute));
    }

    public void addHobby(ProfileWisie wisie) {
        if (wisie.getHobbies().size() < 3) {
            profileWisieEvolutionService.changeHobby(wisie.getId(), null, wisie.getProfile().getId());
        }
    }
}
