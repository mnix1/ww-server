package com.ww.controller;

import com.ww.game.play.PlayManager;
import com.ww.model.container.MapModel;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.wisie.ProfileWisieRepository;
import com.ww.service.auto.AutoService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/_dev")
@AllArgsConstructor
public class DevelopmentController {

    private final ProfileService profileService;
    private final ProfileWisieRepository profileWisieRepository;

    @RequestMapping(value = "/zeroStepIndex", method = RequestMethod.GET)
    public Map zeroStepIndex(@RequestParam Long profileId) {
        Profile profile = profileService.getProfile(profileId);
        profile.setIntroductionStepIndex(0);
        profileService.save(profile);
        return null;
    }

    @RequestMapping(value = "/cleanProfile", method = RequestMethod.GET)
    public Map cleanProfile(@RequestParam Long profileId) {
        Profile profile = profileService.getProfile(profileId);
        Profile newProfile = new Profile(profile.getAuthId(), profile.getName(), profile.getEmail(), profile.getLanguage());
        newProfile.setId(profile.getId());
        profileWisieRepository.deleteAll(profile.getWisies());
        profileService.save(newProfile);
        return null;
    }

    @RequestMapping(value = "/storeRivalModel", method = RequestMethod.GET)
    public Map storeRivalModel(@RequestParam(required = false) Boolean store) {
        boolean was = Rival.storeModel;
        if (store == null) {
            store = true;
        }
        Rival.storeModel = store;
        return new MapModel("was", was).put("now", store).get();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public Map stop(@RequestParam Long profileId) {
        RivalGlobalService.managerMap.get(profileId).dispose();
        return new MapModel("stopped", true).get();
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Map status() {
        Set<PlayManager> managers = new HashSet<>(RivalGlobalService.managerMap.values());
        return new MapModel("rivals", StringUtils.join(managers, ",^_^"))
                .put("rivalCount", managers.size())
                .put("autos", StringUtils.join(AutoService.activeAutoManagers, ",^_^"))
                .put("autoCount", AutoService.activeAutoManagers.size())
                .get();
    }
}
