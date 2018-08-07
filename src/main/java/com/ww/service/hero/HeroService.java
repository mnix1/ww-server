package com.ww.service.hero;

import com.ww.model.Session;
import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
import com.ww.repository.hero.ProfileHeroRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HeroService {

    private Session session = new Session();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private ProfileHeroRepository profileHeroRepository;

    public List<HeroDTO> list() {
        List<Hero> allHeroes = heroRepository.findAll();
        List<ProfileHero> profileHeroes = profileHeroRepository.findAllByProfile_Id(sessionService.getProfileId());
        Map<Long, ProfileHero> profileHeroMap = profileHeroes.stream().collect(Collectors.toMap(o -> o.getHero().getId(), o -> o));
        return allHeroes.stream().map(hero -> {
            ProfileHero profileHero = null;
            if (profileHeroMap.containsKey(hero.getId())) {
                profileHero = profileHeroMap.get(hero.getId());
            }
            return new HeroDTO(hero, profileHero);
        }).collect(Collectors.toList());
    }
}
