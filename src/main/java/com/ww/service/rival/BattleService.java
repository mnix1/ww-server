package com.ww.service.rival;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleContainer;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        cleanBattles();
        BattleContainer battleContainer = prepareBattleContainer(tag);
        if (battleContainer == null) {
            model.put("code", -1);
            return model;
        }
        battles.add(battleContainer);
        model.put("code", 1);
        return model;
    }

    private void cleanBattles(){
        battles.removeIf(battleContainer -> battleContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancel() {
        Map<String, Object> model = new HashMap<>();
        battles.stream().filter(battleContainer -> battleContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanBattles();
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
        ProfileConnection creatorProfileConnection = profileConnectionService.findByProfileId();
        BattleContainer battle = new BattleContainer(creatorProfile, creatorProfileConnection, opponentProfile, opponentProfileConnection);
        sendInvite(battle);
        return battle;
    }

    private void sendInvite(BattleContainer battleContainer) {
        battleContainer.getOpponentProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_INVITE, new FriendDTO(battleContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true).toString()).toString());
    }

    private void sendCancelInvite(BattleContainer battleContainer) {
        battleContainer.getOpponentProfileConnection().sendMessage(new MessageDTO(Message.BATTLE_CANCEL_INVITE, "").toString());
    }

}
