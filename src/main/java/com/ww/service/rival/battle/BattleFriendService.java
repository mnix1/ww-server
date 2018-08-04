package com.ww.service.rival.battle;

import com.ww.manager.BattleManager;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleFriendContainer;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.BattleService;
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
public class BattleFriendService {
    private static final Logger logger = LoggerFactory.getLogger(BattleFriendService.class);

    private final CopyOnWriteArrayList<BattleFriendContainer> battleFriendContainers = new CopyOnWriteArrayList<BattleFriendContainer>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private BattleService battleService;

    public Map startFriend(String tag) {
        Map<String, Object> model = new HashMap<>();
        cleanBattlesCreator();
        cleanBattlesOpponent();
        BattleFriendContainer battleFriendContainer = prepareBattleContainer(tag);
        if (battleFriendContainer == null) {
            model.put("code", -1);
            return model;
        }
        battleFriendContainers.add(battleFriendContainer);
        model.put("code", 1);
        return model;
    }

    private void cleanBattlesCreator() {
        battleFriendContainers.removeIf(battleFriendContainer -> battleFriendContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    private void cleanBattlesOpponent() {
        battleFriendContainers.removeIf(battleFriendContainer -> battleFriendContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancelFriend() {
        Map<String, Object> model = new HashMap<>();
        battleFriendContainers.stream().filter(battleFriendContainer -> battleFriendContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanBattlesCreator();
        return model;
    }

    public Map acceptFriend() {
        Map<String, Object> model = new HashMap<>();
        BattleFriendContainer battle = battleFriendContainers.stream().filter(battleFriendContainer -> battleFriendContainer.getOpponentProfile().getId().equals(sessionService.getProfileId())).findFirst().get();
        BattleManager battleManager = new BattleManager(battle, battleService, profileConnectionService);
        battleService.getProfileIdToBattleManagerMap().put(battle.getCreatorProfile().getId(), battleManager);
        battleService.getProfileIdToBattleManagerMap().put(battle.getOpponentProfile().getId(), battleManager);
        this.sendAcceptInvite(battle);
        return model;
    }

    public Map rejectFriend() {
        Map<String, Object> model = new HashMap<>();
        battleFriendContainers.stream().filter(battleFriendContainer -> battleFriendContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanBattlesOpponent();
        return model;
    }

    private BattleFriendContainer prepareBattleContainer(String tag) {
        Profile opponentProfile = profileService.getProfile(tag);
        ProfileConnection opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
        if (opponentProfileConnection == null) {
            logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
            return null;
        }
        Profile creatorProfile = profileService.getProfile();
        BattleFriendContainer battle = new BattleFriendContainer(creatorProfile, opponentProfile);
        sendInvite(battle);
        return battle;
    }

    private void sendInvite(BattleFriendContainer battleFriendContainer) {
        profileConnectionService.findByProfileId( battleFriendContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_INVITE, new FriendDTO(battleFriendContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true).toString()).toString());
        });
    }

    private void sendCancelInvite(BattleFriendContainer battleFriendContainer) {
        profileConnectionService.findByProfileId( battleFriendContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_CANCEL_INVITE, "").toString());
        });
    }

    private void sendRejectInvite(BattleFriendContainer battleFriendContainer) {
        profileConnectionService.findByProfileId( battleFriendContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_REJECT_INVITE, "").toString());
        });
    }

    private void sendAcceptInvite(BattleFriendContainer battleFriendContainer) {
        profileConnectionService.findByProfileId( battleFriendContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_ACCEPT_INVITE, "").toString());
        });
    }


}
