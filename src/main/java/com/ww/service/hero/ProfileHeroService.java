package com.ww.service.hero;

import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.ProfileHeroRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;

@Service
public class ProfileHeroService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileHeroRepository profileHeroRepository;

    public List<ProfileHeroDTO> list() {
        return profileHeroRepository.findAllByProfile_Id(sessionService.getProfileId()).stream()
                .map(ProfileHeroDTO::new)
                .collect(Collectors.toList());
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
