package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.init.RivalOnePlayerInit;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class RivalInitRandomOpponentService {
    private final Map<Long, RivalOnePlayerInit> waitingForRivalProfiles = new ConcurrentHashMap<>();

    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;

    public Map start(RivalType type, RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (!waitingForRivalProfiles.containsKey(profile.getId())) {
            waitingForRivalProfiles.put(profile.getId(), new RivalOnePlayerInit(type, importance, profile));
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

    public Map<Long,RivalOnePlayerInit> getWaitingForRivalProfiles(){
        return waitingForRivalProfiles;
    }

}
