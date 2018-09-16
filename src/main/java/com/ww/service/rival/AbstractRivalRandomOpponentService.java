package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalSearchingOpponentContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putSuccessCode;

public abstract class AbstractRivalRandomOpponentService {
    private final ConcurrentHashMap<Long, RivalSearchingOpponentContainer> waitingForRivalProfiles = new ConcurrentHashMap<>();

    private static final int RIVAL_INIT_JOB_RATE = 2000;

    protected abstract SessionService getSessionService();

    protected abstract ProfileService getProfileService();

    protected abstract AbstractRivalService getRivalService();

    protected abstract RivalType getRivalType();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract RivalManager createManager(RivalInitContainer rival);

    public Map start(RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = getProfileService().getProfile();
        if (!waitingForRivalProfiles.containsKey(profile.getId())) {
            waitingForRivalProfiles.put(profile.getId(), new RivalSearchingOpponentContainer(importance, profile));
        }
        return putSuccessCode(model);
    }

    public Map cancel(RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        waitingForRivalProfiles.remove(getSessionService().getProfileId());
        return model;
    }

    @Scheduled(fixedRate = RIVAL_INIT_JOB_RATE)
    private synchronized void maybeInitRival() {
        if (waitingForRivalProfiles.isEmpty()) {
//            logger.debug("No waiting for rival profiles");
            return;
        }
        if (waitingForRivalProfiles.size() == 1) {
//            logger.debug("Only one waiting for rival profile");
            return;
        }
        Collection<RivalSearchingOpponentContainer> waitingContainers = waitingForRivalProfiles.values();
        RivalSearchingOpponentContainer waitingContainer = waitingContainers.stream().findFirst().get();
        Profile profile = waitingContainer.getProfile();
        Optional<ProfileConnection> profileConnection = getProfileConnectionService().findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForRivalProfiles.remove(profile.getId());
            maybeInitRival();
            return;
        }
        List<Profile> availableOpponentProfiles = waitingContainers.stream()
                .filter(container -> container.getImportance() == waitingContainer.getImportance() && !container.getProfile().equals(profile))
                .map(RivalSearchingOpponentContainer::getProfile)
                .collect(Collectors.toList());
        if (availableOpponentProfiles.size() < 1) {
            return;
        }
        Profile opponent = findOpponentForRival(availableOpponentProfiles, waitingContainer.getProfile());
        Optional<ProfileConnection> opponentConnection = getProfileConnectionService().findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForRivalProfiles.remove(opponent.getId());
            maybeInitRival();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating rival rivalManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile.getId());
        waitingForRivalProfiles.remove(opponent.getId());
        RivalInitContainer rival = new RivalInitContainer(getRivalType(), waitingContainer.getImportance(), profile, opponent);
        RivalManager rivalManager = createManager(rival);
        getRivalService().getGlobalRivalService().put(rival.getCreatorProfile().getId(), rivalManager);
        getRivalService().getGlobalRivalService().put(rival.getOpponentProfile().getId(), rivalManager);
        rivalManager.start();
        maybeInitRival();
        return;
    }

    private Profile findOpponentForRival(List<Profile> availableOpponentProfiles, Profile profile) {
        // TODO add more logic
        return availableOpponentProfiles.get(0);
    }


}
