package com.ww.service.wisie;

import com.ww.model.dto.wisie.WisieDTO;
import com.ww.model.entity.wisie.Wisie;
import com.ww.model.entity.social.Profile;
import com.ww.repository.wisie.WisieRepository;
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
public class WisieService {

    public static Long EXPERIMENT_CRYSTAL_COST = 100L;
    public static Long EXPERIMENT_ELIXIR_COST = 100L;
    public static Long EXPERIMENT_WISDOM_COST = 100L;

    @Autowired
    private WisieRepository wisieRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieService profileWisieService;

    public List<WisieDTO> list() {
        return wisieRepository.findAll().stream()
                .map(WisieDTO::new)
                .collect(Collectors.toList());
    }

    public boolean checkEnoughResourcesToExperiment(Profile profile) {
        return profile.getCrystal() >= EXPERIMENT_CRYSTAL_COST
                && profile.getElixir() >= EXPERIMENT_ELIXIR_COST
                && profile.getWisdom() >= EXPERIMENT_WISDOM_COST;
    }

    public Wisie randomWisieForProfile(Long profileId) {
        List<Wisie> allWisies = wisieRepository.findAll();
        Set<Long> profileWisiesIds = profileWisieService.findAll(profileId).stream().map(profileWisie -> profileWisie.getWisie().getId()).collect(Collectors.toSet());
        if (allWisies.size() == profileWisiesIds.size()) {
            return null;
        }
        Wisie wisie = randomElement(allWisies);
        while (profileWisiesIds.contains(wisie.getId())) {
            wisie = randomElement(allWisies);
        }
        return wisie;
    }

    public synchronized Map<String, Object> experiment() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        if (!checkEnoughResourcesToExperiment(profile)) {
            model.put("code", -3);//no resources
            return model;
        }
        Wisie wisie = randomWisieForProfile(profile.getId());
        if (wisie == null) {
            model.put("code", -2);//all discovered
            return model;
        }
        model.put("code", 1);
        model.put("wisieType", wisie.getType());
        profileWisieService.addWisie(profile, wisie);
        profile.changeResources(null, -EXPERIMENT_CRYSTAL_COST, -EXPERIMENT_WISDOM_COST, -EXPERIMENT_ELIXIR_COST);
        profileService.save(profile);
        return model;
    }

}
