package com.ww.service.shop;

import com.ww.model.Session;
import com.ww.model.constant.shop.ChestType;
import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.dto.shop.ChestDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.shop.Chest;
import com.ww.model.entity.shop.ProfileChest;
import com.ww.model.entity.social.Profile;
import com.ww.repository.hero.HeroRepository;
import com.ww.repository.hero.ProfileHeroRepository;
import com.ww.repository.shop.ChestRepository;
import com.ww.repository.shop.ProfileChestRepository;
import com.ww.service.SessionService;
import com.ww.service.hero.HeroService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private Session session = new Session();

    @Autowired
    private SessionService sessionService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private HeroService heroService;

    @Autowired
    ChestRepository chestRepository;
    @Autowired
    ProfileChestRepository profileChestRepository;

    public List<ChestDTO> list() {
        return profileService.getProfile().getChests().stream().map(profileChest -> new ChestDTO(profileChest)).collect(Collectors.toList());
    }

    public Map<String, Object> open(Long profileChestId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileChest> optionalProfileChest = profileChestRepository.findById(profileChestId);
        if (!optionalProfileChest.isPresent()) {
            model.put("code", -1);
            return model;
        }
        Profile profile = profileService.getProfile();
        ProfileChest profileChest = optionalProfileChest.get();
        ChestType chestType = profileChest.getChest().getType();
        if (chestType == ChestType.HERO) {
            ProfileHero profileHero = heroService.addHero(profile, heroService.random());
            model.put("hero", new HeroDTO(profileHero));
        }
        remove(profileChest);
        return model;
    }

    public void remove(ProfileChest profileChest) {
        profileChestRepository.delete(profileChest);
    }

    public void addChest(String profileTag) {
        Chest chest = chestRepository.findFirstByType(ChestType.HERO);
        Profile profile = profileService.getProfile(profileTag);
        ProfileChest profileChest = new ProfileChest(profile, chest);
        profileChestRepository.save(profileChest);
    }
}
