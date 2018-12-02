package com.ww.service.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.Resources;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
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
import static com.ww.service.social.IntroService.PICK_WISIES_COUNT;

@Service
public class ProfileWisieService {

    public static final int HERO_TEAM_COUNT = 4;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieRepository profileWisieRepository;

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
        WisieType type = randomWisieForProfile(profile.getId());
        if (type == null) {
            //all discovered
            return putCode(model, -2);
        }
        model.put("type", type);
        addWisie(profile, type);
        profile.subtractResources(experimentCostResources);
        profileService.save(profile);
        model.put("profile", new ExtendedProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    public WisieType randomWisieForProfile(Long profileId) {
        Set<WisieType> profileWisieTypes = findAll(profileId).stream().map(OwnedWisie::getType).collect(Collectors.toSet());
        if (WisieType.values().length == profileWisieTypes.size()) {
            return null;
        }
        WisieType type = WisieType.random();
        while (profileWisieTypes.contains(type)) {
            type = WisieType.random();
        }
        return type;
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

    public ProfileWisie addWisie(Profile profile, WisieType type) {
        ProfileWisie profileWisie = createWisie(profile, type);
        initWisieHobbies(profileWisie);
        initWisieSkills(profileWisie);
        save(profileWisie);
        return profileWisie;
    }

    public ProfileWisie createWisie(Profile profile, WisieType type) {
        ProfileWisie profileWisie = new ProfileWisie(profile, type);
        initWisieAttributes(profileWisie);
        return profileWisie;
    }

    public void initWisieAttributes(OwnedWisie wisie) {
        initWisieAttributes(wisie, 1, 9);
    }

    public void initWisieAttributes(OwnedWisie wisie, int min, int max) {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            wisie.setWisdomAttributeValue(wisdomAttribute, randomDouble(min, max));
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            wisie.setMentalAttributeValue(mentalAttribute, randomDouble(min, max));
        }
    }

    public void initWisieHobbies(OwnedWisie wisie) {
        initWisieHobbies(wisie, Arrays.asList(Category.random()));
    }

    public void initWisieHobbies(OwnedWisie wisie, List<Category> hobbies) {
        wisie.setHobbies(new HashSet<>(hobbies));
    }

    public void initWisieSkills(OwnedWisie wisie) {
        initWisieSkills(wisie, Arrays.asList(Skill.random()));
    }

    public void initWisieSkills(OwnedWisie wisie, List<Skill> skills) {
        wisie.setSkills(new HashSet<>(skills));
    }

    public List<ProfileWisie> createWisies(Profile profile, List<WisieType> wisieTypes) {
        List<Category> categories = Category.list();
        Collections.shuffle(categories);
        List<Skill> skills = Skill.list();
        Collections.shuffle(skills);
        List<ProfileWisie> profileWisies = new ArrayList<>(PICK_WISIES_COUNT);
        for (int i = 0; i < PICK_WISIES_COUNT; i++) {
            ProfileWisie profileWisie = createWisie(profile, wisieTypes.get(i));
            initWisieHobbies(profileWisie, Arrays.asList(categories.get(i)));
            initWisieSkills(profileWisie, Arrays.asList(skills.get(i)));
            profileWisies.add(profileWisie);
        }
        return profileWisies;
    }
}
