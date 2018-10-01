package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.init.RivalOnePlayerInitContainer;
import com.ww.model.container.rival.init.RivalOnePlayerInitContainer;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class RivalInitRandomOpponentService {
    private static final Logger logger = LoggerFactory.getLogger(RivalInitRandomOpponentService.class);

    private final ConcurrentHashMap<Long, RivalOnePlayerInitContainer> waitingForRivalProfiles = new ConcurrentHashMap<>();
    private static final int RIVAL_INIT_JOB_RATE = 2000;

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileConnectionService profileConnectionService;
    @Autowired
    private RivalRunService rivalRunService;
    @Autowired
    private RivalGlobalService rivalGlobalService;

    public Map start(RivalType type, RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (!waitingForRivalProfiles.containsKey(profile.getId())) {
            waitingForRivalProfiles.put(profile.getId(), new RivalOnePlayerInitContainer(type, importance, profile));
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
        Collection<RivalOnePlayerInitContainer> waitingContainers = waitingForRivalProfiles.values();
        RivalOnePlayerInitContainer waitingContainer = waitingContainers.stream().findFirst().get();
        Profile profile = waitingContainer.getCreatorProfile();
        Optional<ProfileConnection> profileConnection = profileConnectionService.findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForRivalProfiles.remove(profile.getId());
            maybeInitRival();
            return;
        }
        List<Profile> availableOpponentProfiles = waitingContainers.stream()
                .filter(container -> container.getImportance() == waitingContainer.getImportance()
                        && container.getType() == waitingContainer.getType()
                        && !container.getCreatorProfile().equals(profile))
                .map(RivalOnePlayerInitContainer::getCreatorProfile)
                .collect(Collectors.toList());
        if (availableOpponentProfiles.size() < 1) {
            return;
        }
        Profile opponent = findOpponentForRival(availableOpponentProfiles, waitingContainer.getCreatorProfile());
        Optional<ProfileConnection> opponentConnection = profileConnectionService.findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForRivalProfiles.remove(opponent.getId());
            maybeInitRival();
            return;
        }
        logger.trace("Matched profiles {} and {}, now creating rival rivalManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile.getId());
        waitingForRivalProfiles.remove(opponent.getId());
        RivalTwoPlayerInitContainer rival = new RivalTwoPlayerInitContainer(waitingContainer.getType(), waitingContainer.getImportance(), profile, opponent);
        rivalRunService.run(rival);
        maybeInitRival();
        return;
    }

//    private RivalManager createManager(RivalInitContainer rival) {
//        if (rival.getType() == RivalType.WAR) {
//            return new WarManager(rival, rivalWarService, profileConnectionService);
//        } else if (rival.getType() == RivalType.BATTLE) {
//            return new BattleManager(rival, rivalBattleService, profileConnectionService);
//        }
//        throw new IllegalArgumentException();
//    }

    private Profile findOpponentForRival(List<Profile> availableOpponentProfiles, Profile profile) {
        // TODO add more logic
        return availableOpponentProfiles.get(0);
    }


}
