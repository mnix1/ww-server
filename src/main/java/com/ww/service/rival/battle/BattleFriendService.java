package com.ww.service.rival.battle;

import com.ww.manager.BattleManager;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
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

    private final CopyOnWriteArrayList<RivalInitContainer> rivalInitContainers = new CopyOnWriteArrayList<RivalInitContainer>();

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
        if (rivalInitContainers.stream().anyMatch(e -> e.getOpponentProfile().getTag().equals(tag) || e.getCreatorProfile().getTag().equals(tag))) {
            model.put("code", -1);
            return model;
        }
        RivalInitContainer rivalInitContainer = prepareBattleContainer(tag);
        if (rivalInitContainer == null) {
            model.put("code", -1);
            return model;
        }
        rivalInitContainers.add(rivalInitContainer);
        model.put("code", 1);
        return model;
    }

    private void cleanBattlesCreator() {
        rivalInitContainers.removeIf(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    private void cleanBattlesOpponent() {
        rivalInitContainers.removeIf(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancelFriend() {
        Map<String, Object> model = new HashMap<>();
        rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanBattlesCreator();
        return model;
    }

    public Map acceptFriend() {
        Map<String, Object> model = new HashMap<>();
        RivalInitContainer battle = rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId())).findFirst().get();
        BattleManager battleManager = new BattleManager(battle, battleService, profileConnectionService);
        battleService.getProfileIdToRivalManagerMap().put(battle.getCreatorProfile().getId(), battleManager);
        battleService.getProfileIdToRivalManagerMap().put(battle.getOpponentProfile().getId(), battleManager);
        this.sendAcceptInvite(battle);
        return model;
    }

    public Map rejectFriend() {
        Map<String, Object> model = new HashMap<>();
        rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanBattlesOpponent();
        return model;
    }

    private RivalInitContainer prepareBattleContainer(String tag) {
        Profile opponentProfile = profileService.getProfile(tag);
        ProfileConnection opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
        if (opponentProfileConnection == null) {
            logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
            return null;
        }
        Profile creatorProfile = profileService.getProfile();
        RivalInitContainer battle = new RivalInitContainer(creatorProfile, opponentProfile);
        sendInvite(battle);
        return battle;
    }

    private void sendInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_INVITE, new FriendDTO(rivalInitContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true).toString()).toString());
        });
    }

    private void sendCancelInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_CANCEL_INVITE, "").toString());
        });
    }

    private void sendRejectInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_REJECT_INVITE, "").toString());
        });
    }

    private void sendAcceptInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.BATTLE_ACCEPT_INVITE, "").toString());
        });
    }


}
