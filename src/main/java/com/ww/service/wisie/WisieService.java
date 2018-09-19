package com.ww.service.wisie;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.dto.wisie.WisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.wisie.Wisie;
import com.ww.repository.wisie.WisieRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.RandomHelper.randomElement;

@Service
public class WisieService {

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

    public Wisie getWisie(WisieType type) {
        return wisieRepository.findByType(type);
    }

    public List<Wisie> getWisies(List<WisieType> types) {
        return wisieRepository.findByTypeIn(types);
    }

    public synchronized Map<String, Object> experiment(Profile profile) {
        Map<String, Object> model = new HashMap<>();
        if (profile == null) {
            profile = profileService.getProfile();
        }
        List<ProfileWisie> profileWisies = profileWisieService.findAll(profile.getId());
        Long experimentCostImpact = profileWisieCountExperimentCostImpact(profileWisies);
        Long crystalCost = 30 + experimentCostImpact;
        Long wisdomCost = 20 + experimentCostImpact;
        Long elixirCost = 10 + experimentCostImpact;
        if (!profile.hasEnoughResources(null, crystalCost, wisdomCost, elixirCost)) {
            //no resources
            return putCode(model, -3);
        }
        Wisie wisie = randomWisieForProfile(profile.getId());
        if (wisie == null) {
            //all discovered
            return putCode(model, -2);
        }
        model.put("wisieType", wisie.getType());
        profileWisieService.addWisie(profile, wisie);
        profile.changeResources(null, -crystalCost, -wisdomCost, -elixirCost);
        profileService.save(profile);
        model.put("profile", new ProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    private Long profileWisieCountExperimentCostImpact(List<ProfileWisie> profileWisies) {
        if (profileWisies.size() <= 5) {
            return 0L;
        }
        return (profileWisies.size() - 5) * 10L;
    }

}
