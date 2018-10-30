package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class RivalInitRandomOpponentService {
    private final Map<Long, RivalOneInit> waitingForRivalProfiles = new ConcurrentHashMap<>();

    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;

    public Map start(RivalType type, RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (!waitingForRivalProfiles.containsKey(profile.getId())) {
            waitingForRivalProfiles.put(profile.getId(), new RivalOneInit(type, importance, profile));
        }
        return putSuccessCode(model);
    }

    public boolean contains(Long profileId) {
        return waitingForRivalProfiles.containsKey(profileId);
    }

    public Map cancel() {
        Map<String, Object> model = new HashMap<>();
        remove(profileService.getProfileId());
        return model;
    }

    public void remove(Long profileId) {
        waitingForRivalProfiles.remove(profileId);
    }

    public Map<Long, RivalOneInit> getWaitingForRivalProfiles(){
        return waitingForRivalProfiles;
    }

}
