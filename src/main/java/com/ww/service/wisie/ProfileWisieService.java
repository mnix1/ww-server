package com.ww.service.wisie;

import com.ww.model.constant.Category;
import com.ww.model.container.Resources;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.model.entity.outside.wisie.Wisie;
import com.ww.repository.outside.wisie.ProfileWisieRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

@Service
public class ProfileWisieService {

    public static final int HERO_TEAM_COUNT = 4;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieRepository profileWisieRepository;

    @Autowired
    private WisieService wisieService;

    public List<ProfileWisieDTO> list() {
        return profileWisieRepository.findAllByProfile_Id(profileService.getProfileId()).stream()
                .map(ProfileWisieDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProfileWisie> findAll(Long profileId) {
        return profileWisieRepository.findAllByProfile_Id(profileId);
    }

    public Optional<ProfileWisie> findByIdAndProfileId(Long profileWisieId, Long profileId) {
        return profileWisieRepository.findByIdAndProfile_Id(profileWisieId, profileId);
    }

    public List<ProfileWisie> findAllInTeam(Long profileId) {
        return profileWisieRepository.findAllByProfile_IdAndInTeam(profileId, true);
    }

    public List<ProfileWisie> findAllNotInTeam(Long profileId) {
        return profileWisieRepository.findAllByProfile_IdAndInTeam(profileId, false);
    }

    public synchronized void save(List<ProfileWisie> wisies) {
        profileWisieRepository.saveAll(wisies);
    }

    public synchronized void save(ProfileWisie wisie) {
        profileWisieRepository.save(wisie);
    }

    public List<ProfileWisie> findByIds(List<Long> ids) {
        if (new HashSet<>(ids).size() != HERO_TEAM_COUNT) {
            return null;
        }
        List<ProfileWisie> wisies = profileWisieRepository.findAllByProfile_IdAndIdIn(profileService.getProfileId(), ids);
        if (ids.size() != wisies.size()) {
            return null;
        }
        return wisies;
    }

    public synchronized Map<String, Object> teamSave(List<Long> ids) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileWisie> wisies = findByIds(ids);
        if (wisies == null) {
            return putErrorCode(model);
        }
        List<ProfileWisie> actualInTeam = profileWisieRepository.findAllByProfile_IdAndInTeam(profileService.getProfileId(), true);
        for (ProfileWisie profileWisie : actualInTeam) {
            profileWisie.setInTeam(false);
        }
        for (ProfileWisie profileWisie : wisies) {
            profileWisie.setInTeam(true);
        }
        List<ProfileWisie> changes = new ArrayList<>();
        changes.addAll(actualInTeam);
        changes.addAll(wisies);
        profileWisieRepository.saveAll(changes);
        return putSuccessCode(model);
    }

    public synchronized Map<String, Object> experiment(Profile profile) {
        Map<String, Object> model = new HashMap<>();
        if (profile == null) {
            profile = profileService.getProfile();
        }
        List<ProfileWisie> profileWisies = findAll(profile.getId());
        Resources experimentCostResources = experimentCostResources(profileWisies);
        if (!profile.hasEnoughResources(experimentCostResources)) {
            //no resources
            return putCode(model, -3);
        }
        Wisie wisie = randomWisieForProfile(profile.getId());
        if (wisie == null) {
            //all discovered
            return putCode(model, -2);
        }
        model.put("wisieType", wisie.getType());
        addWisie(profile, wisie);
        profile.subtractResources(experimentCostResources);
        profileService.save(profile);
        model.put("profile", new ProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    public Wisie randomWisieForProfile(Long profileId) {
        List<Wisie> allWisies = wisieService.list();
        Set<Long> profileWisiesIds = findAll(profileId).stream().map(profileWisie -> profileWisie.getWisie().getId()).collect(Collectors.toSet());
        if (allWisies.size() == profileWisiesIds.size()) {
            return null;
        }
        Wisie wisie = randomElement(allWisies);
        while (profileWisiesIds.contains(wisie.getId())) {
            wisie = randomElement(allWisies);
        }
        return wisie;
    }

    private Resources experimentCostResources(List<ProfileWisie> profileWisies) {
        Long experimentCostImpact = profileWisieCountExperimentCostImpact(profileWisies);
        Long crystalCost = 30 + experimentCostImpact;
        Long wisdomCost = 20 + experimentCostImpact;
        Long elixirCost = 10 + experimentCostImpact;
        return new Resources(null, crystalCost, wisdomCost, elixirCost);
    }

    private Long profileWisieCountExperimentCostImpact(List<ProfileWisie> profileWisies) {
        if (profileWisies.size() <= 5) {
            return 0L;
        }
        return (profileWisies.size() - 5) * 10L;
    }

    public ProfileWisie addWisie(Profile profile, Wisie wisie) {
        ProfileWisie profileWisie = createWisie(profile, wisie);
        initWisieHobbies(profileWisie);
        save(profileWisie);
        return profileWisie;
    }

    public ProfileWisie createWisie(Profile profile, Wisie wisie) {
        ProfileWisie profileWisie = new ProfileWisie(profile, wisie);
        initWisieAttributes(profileWisie);
        return profileWisie;
    }

    public void initWisieAttributes(ProfileWisie wisie) {
        wisie.setMemory(randomDouble(1, 10));
        wisie.setLogic(randomDouble(1, 10));
        wisie.setPerceptivity(randomDouble(1, 10));
        wisie.setCounting(randomDouble(1, 10));
        wisie.setCombiningFacts(randomDouble(1, 10));
        wisie.setPatternRecognition(randomDouble(1, 10));
        wisie.setImagination(randomDouble(1, 10));

        wisie.setSpeed(randomDouble(1, 10));
        wisie.setReflex(randomDouble(1, 10));
        wisie.setConcentration(randomDouble(1, 10));
        wisie.setConfidence(randomDouble(1, 10));
        wisie.setIntuition(randomDouble(1, 10));
    }

    public void initWisieHobbies(ProfileWisie wisie) {
        initWisieHobbies(wisie, Arrays.asList(Category.random()));
    }

    public void initWisieHobbies(ProfileWisie wisie, List<Category> hobbies) {
        wisie.setHobbies(new HashSet<>(hobbies));
    }
}
