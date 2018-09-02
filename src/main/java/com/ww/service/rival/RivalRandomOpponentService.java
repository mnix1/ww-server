package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
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

public abstract class RivalRandomOpponentService {
    private final CopyOnWriteArrayList<Profile> waitingForRivalProfiles = new CopyOnWriteArrayList<>();

    private static final int RIVAL_INIT_JOB_RATE = 2000;

    protected abstract SessionService getSessionService();

    protected abstract ProfileService getProfileService();

    protected abstract RivalService getRivalService();

    protected abstract RivalType getRivalType();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract RivalManager createManager(RivalInitContainer rival);

    protected boolean checkIfCanPlay(Profile profile) {
        return true;
    }

    public Map start(RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = getProfileService().getProfile();
        if (!checkIfCanPlay(profile)) {
            model.put("code", -1);
            return model;
        }
        if (!waitingForRivalProfiles.contains(profile)) {
            waitingForRivalProfiles.add(profile);
        }
        model.put("code", 1);
        return model;
    }

    public Map cancel(RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        waitingForRivalProfiles.removeIf(profile -> profile.getId().equals(getSessionService().getProfileId()));
        return model;
    }

    @Scheduled(fixedRate = RIVAL_INIT_JOB_RATE)
    private void maybeInitRival() {
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
            maybeInitRival();
            return;
        }
        Profile opponent = findOpponentForRival(profile);
        Optional<ProfileConnection> opponentConnection = getProfileConnectionService().findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForRivalProfiles.removeIf(e -> e.getId().equals(opponent.getId()));
            maybeInitRival();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating rival rivalManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile);
        waitingForRivalProfiles.remove(opponent);
        RivalInitContainer rival = new RivalInitContainer(getRivalType(), profile, opponent);
        RivalManager rivalManager = createManager(rival);
        getRivalService().getProfileIdToRivalManagerMap().put(rival.getCreatorProfile().getId(), rivalManager);
        getRivalService().getProfileIdToRivalManagerMap().put(rival.getOpponentProfile().getId(), rivalManager);
        rivalManager.sendReadyFast();
        return;
    }

    private Profile findOpponentForRival(Profile profile) {
        // TODO add more logic
        for (Profile p : waitingForRivalProfiles) {
            if (!p.getId().equals(profile.getId())) {
                return p;
            }
        }
        return null;
    }


}
