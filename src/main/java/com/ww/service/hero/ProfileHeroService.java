package com.ww.service.hero;

import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.ProfileHeroRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;

@Service
public class ProfileHeroService {

    public static final int HERO_TEAM_COUNT = 4;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileHeroRepository profileHeroRepository;

    public List<ProfileHeroDTO> list() {
        return profileHeroRepository.findAllByProfile_Id(sessionService.getProfileId()).stream()
                .map(ProfileHeroDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProfileHero> listTeam(Long profileId){
        return profileHeroRepository.findAllByProfile_IdAndInTeam(profileId, true);
    }

    public Map<String, Object> teamSave(List<Long> ids) {
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

        hero.setMentalAttributeReflex(randomDouble(1, 10));
        hero.setMentalAttributeConcentration(randomDouble(1, 10));
        hero.setMentalAttributeLeadership(randomDouble(1, 10));
        hero.setMentalAttributeCharisma(randomDouble(1, 10));
        hero.setMentalAttributeIntuition(randomDouble(1, 10));
    }
}
