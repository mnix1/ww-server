package com.ww.service.hero;

import com.ww.helper.HeroHelper;
import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.ProfileHeroRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.NumberHelper.smartRound;
import static com.ww.helper.RandomHelper.randomDouble;

@Service
public class ProfileHeroService {

    public static final int HERO_TEAM_COUNT = 4;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileHeroRepository profileHeroRepository;

    public List<ProfileHeroDTO> list() {
        return profileHeroRepository.findAllByProfile_Id(sessionService.getProfileId()).stream()
                .map(ProfileHeroDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProfileHero> listTeam(Long profileId) {
        return profileHeroRepository.findAllByProfile_IdAndInTeam(profileId, true);
    }

    public synchronized void saveTeam(List<ProfileHero> team) {
        profileHeroRepository.saveAll(team);
    }

    public synchronized Map<String, Object> teamSave(List<Long> ids) {
        Map<String, Object> model = new HashMap<>();
        if (ids.size() > HERO_TEAM_COUNT) {
            model.put("code", -1);
            return model;
        }
        List<ProfileHero> heroes = profileHeroRepository.findAllByProfile_IdAndIdIn(sessionService.getProfileId(), ids);
        if (ids.size() != heroes.size()) {
            model.put("code", -1);
            return model;
        }
        List<ProfileHero> actualInTeam = profileHeroRepository.findAllByProfile_IdAndInTeam(sessionService.getProfileId(), true);
        for (ProfileHero profileHero : actualInTeam) {
            profileHero.setInTeam(false);
        }
        for (ProfileHero profileHero : heroes) {
            profileHero.setInTeam(true);
        }
        List<ProfileHero> changes = new ArrayList<>();
        changes.addAll(actualInTeam);
        changes.addAll(heroes);
        profileHeroRepository.saveAll(changes);
        model.put("code", 1);
        return model;
    }

    public synchronized Map<String, Object> upgradeHero(Long profileHeroId, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        Long wisdom = profile.getWisdom();
        if (wisdomAttribute != null && wisdom >= WisdomAttribute.UPGRADE_COST) {
            profile.changeResources(null, null, -WisdomAttribute.UPGRADE_COST, null);
        } else if (mentalAttribute != null && wisdom >= MentalAttribute.UPGRADE_COST) {
            profile.changeResources(null, null, -MentalAttribute.UPGRADE_COST, null);
        } else {
            model.put("code", -1);
            return model;
        }
        Optional<ProfileHero> optionalProfileHero = profileHeroRepository.findByIdAndProfile_Id(profileHeroId, profile.getId());
        if (!optionalProfileHero.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileHero profileHero = optionalProfileHero.get();
        double attributeChange = calculateHeroAttributeChange(profileHero, wisdomAttribute, mentalAttribute);
        if (wisdomAttribute != null) {
            profileHero.upgradeWisdomAttribute(wisdomAttribute, attributeChange);
        } else if (mentalAttribute != null) {
            profileHero.upgradeMentalAttribute(mentalAttribute, attributeChange);
        }
        profileService.save(profile);
        profileHeroRepository.save(profileHero);
        model.put("attributeChange", smartRound(attributeChange));
        model.put("profileHero", new ProfileHeroDTO(profileHero));
        model.put("profile", new ProfileResourcesDTO(profile));
        model.put("code", 1);
        return model;
    }

    private double calculateHeroAttributeChange(ProfileHero profileHero, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        double value = 0;
        if (wisdomAttribute != null) {
            value = profileHero.getWisdomAttributeValue(wisdomAttribute);
        } else if (mentalAttribute != null) {
            value = profileHero.getMentalAttributeValue(mentalAttribute);
        }
        double f = HeroHelper.f1(value);
        double k = 1 - f;
        return value * Math.pow(k, 6) + k;
    }

    public List<ProfileHero> findAll(Long profileId) {
        return profileHeroRepository.findAllByProfile_Id(profileId);
    }

    public ProfileHero addHero(Profile profile, Hero hero) {
        ProfileHero profileHero = new ProfileHero(profile, hero);
        initHeroStats(profileHero);
        profileHeroRepository.save(profileHero);
        return profileHero;
    }

    public void initHeroStats(ProfileHero hero) {
        hero.setWisdomAttributeMemory(randomDouble(1, 10));
        hero.setWisdomAttributeLogic(randomDouble(1, 10));
        hero.setWisdomAttributePerceptivity(randomDouble(1, 10));
        hero.setWisdomAttributeCounting(randomDouble(1, 10));
        hero.setWisdomAttributeCombiningFacts(randomDouble(1, 10));
        hero.setWisdomAttributePatternRecognition(randomDouble(1, 10));
        hero.setWisdomAttributeImagination(randomDouble(1, 10));

        hero.setMentalAttributeSpeed(randomDouble(1, 10));
        hero.setMentalAttributeReflex(randomDouble(1, 10));
        hero.setMentalAttributeConcentration(randomDouble(1, 10));
        hero.setMentalAttributeConfidence(randomDouble(1, 10));
        hero.setMentalAttributeIntuition(randomDouble(1, 10));
    }
}
