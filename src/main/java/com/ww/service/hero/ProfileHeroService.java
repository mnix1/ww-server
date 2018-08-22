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

    public ProfileHero addHero(Profile profile, Hero hero) {
        ProfileHero profileHero = new ProfileHero(profile, hero);
        profileHeroRepository.save(profileHero);
        return profileHero;
    }
}
