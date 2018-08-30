package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class RivalFastService {
    private final CopyOnWriteArrayList<Profile> waitingForRivalProfiles = new CopyOnWriteArrayList<>();

    private static final int FAST_RIVAL_JOB_RATE = 2000;

    protected abstract SessionService getSessionService();

    protected abstract ProfileService getProfileService();

    protected abstract RivalService getRivalService();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract RivalManager createManager(RivalInitContainer rival);

    public Map startFast() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = getProfileService().getProfile();
        if (!waitingForRivalProfiles.contains(profile)) {
            waitingForRivalProfiles.add(profile);
        }
        model.put("code", 1);
        return model;
    }

    public Map cancelFast() {
        Map<String, Object> model = new HashMap<>();
        waitingForRivalProfiles.removeIf(profile -> profile.getId().equals(getSessionService().getProfileId()));
        return model;
    }

    @Scheduled(fixedRate = FAST_RIVAL_JOB_RATE)
    private void maybeInitFastRival() {
        if (waitingForRivalProfiles.isEmpty()) {
//            logger.debug("No waiting for rival profiles");
            return;
        }
        if (waitingForRivalProfiles.size() == 1) {
//            logger.debug("Only one waiting for rival profile");
            return;
        }
        Profile profile = waitingForRivalProfiles.get(0);
        Optional<ProfileConnection> profileConnection = getProfileConnectionService().findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForRivalProfiles.removeIf(e -> e.getId().equals(profile.getId()));
            maybeInitFastRival();
            return;
        }
        Profile opponent = findOpponentForFastRival(profile);
        Optional<ProfileConnection> opponentConnection = getProfileConnectionService().findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForRivalProfiles.removeIf(e -> e.getId().equals(opponent.getId()));
            maybeInitFastRival();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating rival rivalManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile);
        waitingForRivalProfiles.remove(opponent);
        RivalInitContainer rival = new RivalInitContainer(profile, opponent);
        RivalManager rivalManager = createManager(rival);
        getRivalService().getProfileIdToRivalManagerMap().put(rival.getCreatorProfile().getId(), rivalManager);
        getRivalService().getProfileIdToRivalManagerMap().put(rival.getOpponentProfile().getId(), rivalManager);
        rivalManager.sendReadyFast();
        return;
    }

    private Profile findOpponentForFastRival(Profile profile) {
        // TODO add more logic
        for (Profile p : waitingForRivalProfiles) {
            if (!p.getId().equals(profile.getId())) {
                return p;
            }
        }
        return null;
    }


}
