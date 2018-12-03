package com.ww.controller;

import com.ww.game.auto.AutoManager;
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
    private final AutoService autoService;

    @RequestMapping(value = "/changeMaxAutoManagers", method = RequestMethod.GET)
    public Map<String, Object> changeMaxAutoManagers(@RequestParam int max) {
        int current = AutoService.MAX_ACTIVE_AUTO_MANAGERS;
        AutoService.MAX_ACTIVE_AUTO_MANAGERS = max;
        return new MapModel("was", current).put("now", max).get();
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

    @RequestMapping(value = "/storeRivalInfo", method = RequestMethod.GET)
    public Map storeRivalInfo(@RequestParam(required = false) Boolean human, @RequestParam(required = false) Boolean auto) {
        boolean wasHuman = Rival.storeHumanInfo;
        boolean wasAuto = Rival.storeAutoInfo;
        if (human != null) {
            Rival.storeHumanInfo = human;
        }
        if (auto != null) {
            Rival.storeAutoInfo = auto;
        }
        return new MapModel("wasHuman", wasHuman).put("wasAuto", wasAuto).put("nowHuman", Rival.storeHumanInfo).put("nowAuto", Rival.storeAutoInfo).get();
    }

    @RequestMapping(value = "/stopRival", method = RequestMethod.GET)
    public Map stopRival(@RequestParam Long profileId) {
        RivalGlobalService.managerMap.get(profileId).dispose();
        return new MapModel("stopped", true).get();
    }

    @RequestMapping(value = "/cleanAutos", method = RequestMethod.GET)
    public Map cleanAutos() {
        for (AutoManager manager : AutoService.activeAutoManagersMap.values()) {
            autoService.disposeManager(manager);
        }
        return new MapModel("cleaned", true).get();
    }

    @RequestMapping(value = "/rivalStatus", method = RequestMethod.GET)
    public Map rivalStatus() {
        Set<PlayManager> managers = new HashSet<>(RivalGlobalService.managerMap.values());
        return new MapModel("rivals", StringUtils.join(managers, ",^_^"))
                .put("rivalCount", managers.size())
                .put("autos", StringUtils.join(AutoService.activeAutoManagersMap, ",^_^"))
                .put("autoCount", AutoService.activeAutoManagersMap.size())
                .get();
    }

    @RequestMapping(value = "/simpleRivalStatus", method = RequestMethod.GET)
    public Map simpleRivalStatus() {
        Set<PlayManager> managers = new HashSet<>(RivalGlobalService.managerMap.values());
        return new MapModel("rivalCount", managers.size())
                .put("autoCount", AutoService.activeAutoManagersMap.size())
                .get();
    }
}
