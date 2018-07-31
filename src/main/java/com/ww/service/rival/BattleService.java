package com.ww.service.rival;

import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BattleService {
    private static final Logger logger = LoggerFactory.getLogger(BattleService.class);

    private final CopyOnWriteArrayList<BattleContainer> battles = new CopyOnWriteArrayList<BattleContainer>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRendererService taskRendererService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    public Map start(String tag) {
        Map<String, Object> model = new HashMap<>();
        BattleContainer battleContainer = prepareBattleContainer(tag);
        if (battleContainer == null) {
            model.put("code", -1);
            return model;
        }
        battles.add(battleContainer);
        model.put("code", 1);
        return model;
    }

    private BattleContainer prepareBattleContainer(String tag) {
        ProfileConnection opponentProfileConnection = null;
        Profile opponentProfile = null;
        if (tag == null) {
            //TODO autobinding profiles
//            List<ProfileConnection> profileConnections = profileConnectionService.getProfileConnections();
//            if (profileConnections.isEmpty()) {
//                //TODO add bot because no users
//                logger.error("No connected profiles: {}", sessionService.getProfileId());
//                return null;
//            }
//            //TODO add logic to find closest skill opponent
//            opponentProfileConnection = profileConnections.get(0);
//            opponentProfile = profileService.getProfile(opponentProfileConnection.getProfileId());
        } else {
            opponentProfile = profileService.getProfile(tag);
            opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
            if (opponentProfileConnection == null) {
                logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
                return null;
            }
        }
        Profile creatorProfile = profileService.getProfile();
        ProfileConnection creatorProfileConnection =  profileConnectionService.findByProfileId();
        BattleContainer battle = new BattleContainer(creatorProfile, creatorProfileConnection, opponentProfile, opponentProfileConnection);
        return battle;
    }

}
