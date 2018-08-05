package com.ww.service.rival.battle;

import com.ww.manager.BattleManager;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleFriendContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BattleFastService {
    private static final Logger logger = LoggerFactory.getLogger(BattleFastService.class);

    private final CopyOnWriteArrayList<Profile> waitingForBattleProfiles = new CopyOnWriteArrayList<>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private BattleService battleService;

    public Map startFast() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        if (!waitingForBattleProfiles.contains(profile)) {
            waitingForBattleProfiles.add(profile);
        }
        model.put("code", 1);
        return model;
    }

    public Map cancelFast() {
        Map<String, Object> model = new HashMap<>();
        waitingForBattleProfiles.removeIf(profile -> profile.getId().equals(sessionService.getProfileId()));
        return model;
    }

    @Scheduled(fixedRate = 5000)
    private void maybeInitFastBattle() {
        if (waitingForBattleProfiles.isEmpty()) {
//            logger.debug("No waiting for battle profiles");
            return;
        }
        if (waitingForBattleProfiles.size() == 1) {
//            logger.debug("Only one waiting for battle profile");
            return;
        }
        Profile profile = waitingForBattleProfiles.get(0);
        Optional<ProfileConnection> profileConnection = profileConnectionService.findByProfileId(profile.getId());
        if (!profileConnection.isPresent()) {
            waitingForBattleProfiles.removeIf(e -> e.getId().equals(profile.getId()));
            maybeInitFastBattle();
            return;
        }
        Profile opponent = findOpponentForFastBattle(profile);
        Optional<ProfileConnection> opponentConnection = profileConnectionService.findByProfileId(opponent.getId());
        if (!opponentConnection.isPresent()) {
            waitingForBattleProfiles.removeIf(e -> e.getId().equals(opponent.getId()));
            maybeInitFastBattle();
            return;
        }
//        logger.debug("Matched profiles {} and {}, now creating battle manager", profile.getId(), opponent.getId());
        waitingForBattleProfiles.remove(profile);
        waitingForBattleProfiles.remove(opponent);
        BattleFriendContainer battle = new BattleFriendContainer(profile, opponent);
        BattleManager battleManager = new BattleManager(battle, battleService, profileConnectionService);
        battleService.getProfileIdToBattleManagerMap().put(battle.getCreatorProfile().getId(), battleManager);
        battleService.getProfileIdToBattleManagerMap().put(battle.getOpponentProfile().getId(), battleManager);
        battleManager.sendReadyFast();
        return;
    }

    private Profile findOpponentForFastBattle(Profile profile) {
        // TODO add more logic
        for (Profile p : waitingForBattleProfiles) {
            if (!p.getId().equals(profile.getId())) {
                return p;
            }
        }
        return null;
    }


}
