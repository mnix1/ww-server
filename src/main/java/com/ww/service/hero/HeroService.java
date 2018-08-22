package com.ww.service.hero;

import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class HeroService {

    public static Long EXPERIMENT_CRYSTAL_COST = 100L;
    public static Long EXPERIMENT_ELIXIR_COST = 100L;
    public static Long EXPERIMENT_WISDOM_COST = 100L;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileHeroService profileHeroService;

    public List<HeroDTO> list() {
        return heroRepository.findAll().stream()
                .map(HeroDTO::new)
                .collect(Collectors.toList());
    }

    public boolean checkEnoughResourcesToExperiment(Profile profile) {
        return profile.getCrystal() >= EXPERIMENT_CRYSTAL_COST
                && profile.getElixir() >= EXPERIMENT_ELIXIR_COST
                && profile.getWisdom() >= EXPERIMENT_WISDOM_COST;
    }

    public Hero randomHeroForProfile(Profile profile) {
        List<Hero> allHeroes = heroRepository.findAll();
        Set<Long> profileHeroesIds = profile.getHeroes().stream().map(profileHero -> profileHero.getHero().getId()).collect(Collectors.toSet());
        if (allHeroes.size() == profileHeroesIds.size()) {
            return null;
        }
        Hero hero = randomElement(allHeroes);
        while (profileHeroesIds.contains(hero.getId())) {
            hero = randomElement(allHeroes);
        }
        return hero;
    }

    public synchronized Map<String, Object> experiment() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        if (!checkEnoughResourcesToExperiment(profile)) {
            model.put("code", -3);//no resources
            return model;
        }
        Hero hero = randomHeroForProfile(profile);
        if (hero == null) {
            model.put("code", -2);//all discovered
            return model;
        }
        model.put("code", 1);
        model.put("heroType", hero.getType());
        profileHeroService.addHero(profile, hero);
        profile.changeResources(null, -EXPERIMENT_CRYSTAL_COST, -EXPERIMENT_WISDOM_COST, -EXPERIMENT_ELIXIR_COST);
        profileService.save(profile);
        return model;
    }

}
