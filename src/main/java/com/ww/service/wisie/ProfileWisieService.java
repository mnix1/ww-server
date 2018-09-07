package com.ww.service.wisie;

import com.ww.helper.WisieHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.wisie.Wisie;
import com.ww.repository.wisie.ProfileWisieRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.NumberHelper.smartRound;
import static com.ww.helper.RandomHelper.randomDouble;

@Service
public class ProfileWisieService {

    public static final int HERO_TEAM_COUNT = 4;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileWisieRepository profileWisieRepository;

    public List<ProfileWisieDTO> list() {
        return profileWisieRepository.findAllByProfile_Id(sessionService.getProfileId()).stream()
                .map(ProfileWisieDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProfileWisie> listTeam(Long profileId) {
        return profileWisieRepository.findAllByProfile_IdAndInTeam(profileId, true);
    }

    public synchronized void saveTeam(List<ProfileWisie> team) {
        profileWisieRepository.saveAll(team);
    }

    public List<ProfileWisie> findByIds(List<Long> ids) {
        if (new HashSet<>(ids).size() != HERO_TEAM_COUNT) {
            return null;
        }
        List<ProfileWisie> wisies = profileWisieRepository.findAllByProfile_IdAndIdIn(sessionService.getProfileId(), ids);
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
        List<ProfileWisie> actualInTeam = profileWisieRepository.findAllByProfile_IdAndInTeam(sessionService.getProfileId(), true);
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

    public synchronized Map<String, Object> upgradeWisie(Long profileWisieId, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        Long wisdom = profile.getWisdom();
        if (wisdomAttribute != null && wisdom >= WisdomAttribute.UPGRADE_COST) {
            profile.changeResources(null, null, -WisdomAttribute.UPGRADE_COST, null);
        } else if (mentalAttribute != null && wisdom >= MentalAttribute.UPGRADE_COST) {
            profile.changeResources(null, null, -MentalAttribute.UPGRADE_COST, null);
        } else {
            return putErrorCode(model);
        }
        Optional<ProfileWisie> optionalProfileWisie = profileWisieRepository.findByIdAndProfile_Id(profileWisieId, profile.getId());
        if (!optionalProfileWisie.isPresent()) {
            return putErrorCode(model);
        }
        ProfileWisie profileWisie = optionalProfileWisie.get();
        double attributeChange = calculateWisieAttributeChange(profileWisie, wisdomAttribute, mentalAttribute);
        if (wisdomAttribute != null) {
            profileWisie.upgradeWisdomAttribute(wisdomAttribute, attributeChange);
        } else if (mentalAttribute != null) {
            profileWisie.upgradeMentalAttribute(mentalAttribute, attributeChange);
        }
        profileService.save(profile);
        profileWisieRepository.save(profileWisie);
        model.put("attributeChange", smartRound(attributeChange));
        model.put("profileWisie", new ProfileWisieDTO(profileWisie));
        model.put("profile", new ProfileResourcesDTO(profile));
        return putSuccessCode(model);
    }

    private double calculateWisieAttributeChange(ProfileWisie profileWisie, WisdomAttribute wisdomAttribute, MentalAttribute mentalAttribute) {
        double value = 0;
        if (wisdomAttribute != null) {
            value = profileWisie.getWisdomAttributeValue(wisdomAttribute);
        } else if (mentalAttribute != null) {
            value = profileWisie.getMentalAttributeValue(mentalAttribute);
        }
        double f = WisieHelper.f1(value);
        double k = 1 - f;
        return value * Math.pow(k, 6) + k;
    }

    public List<ProfileWisie> findAll(Long profileId) {
        return profileWisieRepository.findAllByProfile_Id(profileId);
    }

    public ProfileWisie addWisie(Profile profile, Wisie wisie) {
        ProfileWisie profileWisie = new ProfileWisie(profile, wisie);
        if (maybeAddWisieToTeam(profile, profileWisie)) {
            profile.setTeamInitialized(true);
            profileService.save(profile);
        }
        initWisieAttributes(profileWisie);
        initWisieHobbies(profileWisie);
        profileWisieRepository.save(profileWisie);
        return profileWisie;
    }

    //if not full actually
    public boolean maybeAddWisieToTeam(Profile profile, ProfileWisie profileWisie) {
        if (profile.getTeamInitialized()) {
            return false;
        }
        int actualTeamSize = listTeam(profile.getId()).size();
        if (actualTeamSize < HERO_TEAM_COUNT) {
            profileWisie.setInTeam(true);
        }
        return actualTeamSize + 1 == HERO_TEAM_COUNT;
    }

    public void initWisieAttributes(ProfileWisie wisie) {
        wisie.setWisdomAttributeMemory(randomDouble(1, 10));
        wisie.setWisdomAttributeLogic(randomDouble(1, 10));
        wisie.setWisdomAttributePerceptivity(randomDouble(1, 10));
        wisie.setWisdomAttributeCounting(randomDouble(1, 10));
        wisie.setWisdomAttributeCombiningFacts(randomDouble(1, 10));
        wisie.setWisdomAttributePatternRecognition(randomDouble(1, 10));
        wisie.setWisdomAttributeImagination(randomDouble(1, 10));

        wisie.setMentalAttributeSpeed(randomDouble(1, 10));
        wisie.setMentalAttributeReflex(randomDouble(1, 10));
        wisie.setMentalAttributeConcentration(randomDouble(1, 10));
        wisie.setMentalAttributeConfidence(randomDouble(1, 10));
        wisie.setMentalAttributeIntuition(randomDouble(1, 10));
    }

    public void initWisieHobbies(ProfileWisie wisie) {
        wisie.setHobbies(new HashSet<>(Arrays.asList(Category.random())));
    }
}
