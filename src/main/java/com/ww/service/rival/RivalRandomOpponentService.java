package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalSearchingOpponentContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class RivalRandomOpponentService {
    private final ConcurrentHashMap<Long, RivalSearchingOpponentContainer> waitingForRivalProfiles = new ConcurrentHashMap<>();
    private static final int RIVAL_INIT_JOB_RATE = 2000;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileConnectionService profileConnectionService;
    @Autowired
    private RivalWarService rivalWarService;
    @Autowired
    private RivalBattleService rivalBattleService;
    @Autowired
    private GlobalRivalService globalRivalService;

    public Map start(RivalType type, RivalImportance importance) {
        Map<String, Object> model = new HashMap<>();
        if (globalRivalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
        if (!waitingForRivalProfiles.containsKey(profile.getId())) {
            waitingForRivalProfiles.put(profile.getId(), new RivalSearchingOpponentContainer(type, importance, profile));
        }
        return putSuccessCode(model);
    }

    public boolean contains(Long profileId){
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
        Collection<RivalSearchingOpponentContainer> waitingContainers = waitingForRivalProfiles.values();
        RivalSearchingOpponentContainer waitingContainer = waitingContainers.stream().findFirst().get();
        Profile profile = waitingContainer.getProfile();
        Optional<ProfileConnection> profileConnection = profileConnectionService.findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForRivalProfiles.remove(profile.getId());
            maybeInitRival();
            return;
        }
        List<Profile> availableOpponentProfiles = waitingContainers.stream()
                .filter(container -> container.getImportance() == waitingContainer.getImportance()
                        && container.getType() == waitingContainer.getType()
                        && !container.getProfile().equals(profile))
                .map(RivalSearchingOpponentContainer::getProfile)
                .collect(Collectors.toList());
        if (availableOpponentProfiles.size() < 1) {
            return;
        }
        Profile opponent = findOpponentForRival(availableOpponentProfiles, waitingContainer.getProfile());
        Optional<ProfileConnection> opponentConnection = profileConnectionService.findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForRivalProfiles.remove(opponent.getId());
            maybeInitRival();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating rival rivalManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile.getId());
        waitingForRivalProfiles.remove(opponent.getId());
        RivalInitContainer rival = new RivalInitContainer(waitingContainer.getType(), waitingContainer.getImportance(), profile, opponent);
        RivalManager rivalManager = createManager(rival);
        AbstractRivalService abstractRivalService = null;
        if (waitingContainer.getType() == RivalType.WAR) {
            abstractRivalService = rivalWarService;
        } else if (waitingContainer.getType() == RivalType.BATTLE) {
            abstractRivalService = rivalBattleService;
        } else {
            throw new IllegalArgumentException();
        }
        abstractRivalService.getGlobalRivalService().put(rival.getCreatorProfile().getId(), rivalManager);
        abstractRivalService.getGlobalRivalService().put(rival.getOpponentProfile().getId(), rivalManager);
        rivalManager.start();
        maybeInitRival();
        return;
    }

    private RivalManager createManager(RivalInitContainer rival) {
        if (rival.getType() == RivalType.WAR) {
            return new WarManager(rival, rivalWarService, profileConnectionService);
        } else if (rival.getType() == RivalType.BATTLE) {
            return new BattleManager(rival, rivalBattleService, profileConnectionService);
        }
        throw new IllegalArgumentException();
    }

    private Profile findOpponentForRival(List<Profile> availableOpponentProfiles, Profile profile) {
        // TODO add more logic
        return availableOpponentProfiles.get(0);
    }


}
