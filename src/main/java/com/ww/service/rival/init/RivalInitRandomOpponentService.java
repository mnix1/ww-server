package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.service.rival.init.RivalInitRandomOpponentJobService.RIVAL_INIT_JOB_RATE;

@Service
@AllArgsConstructor
public class RivalInitRandomOpponentService {
    private final Map<Long, RivalOneInit> waitingForRivalProfiles = new ConcurrentHashMap<>();

    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;

    public Map start(RivalType type, RivalImportance importance, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileId)) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile(profileId);
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

    public Map<Long, RivalOneInit> getWaitingForRivalProfiles() {
        return waitingForRivalProfiles;
    }

    public Optional<RivalOneInit> maybeGetRivalInitWaitingLong() {
        if (waitingForRivalProfiles.size() != 1) {
            return Optional.empty();
        }
        RivalOneInit onlyRivalOneInit = waitingForRivalProfiles.values().iterator().next();
        if (Instant.now().toEpochMilli() - onlyRivalOneInit.getDate().toEpochMilli() > RIVAL_INIT_JOB_RATE * 2) {
            return Optional.of(onlyRivalOneInit);
        }
        return Optional.empty();
    }

}
