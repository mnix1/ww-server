package com.ww.service.rival;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.rival.war.WarService;
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

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class RivalFriendService {
    private static final Logger logger = LoggerFactory.getLogger(RivalFriendService.class);

    private final CopyOnWriteArrayList<RivalInitContainer> rivalInitContainers = new CopyOnWriteArrayList<RivalInitContainer>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private BattleService battleService;
    @Autowired
    private WarService warService;

    public Map startFriend(String tag, RivalType type) {
        Map<String, Object> model = new HashMap<>();
        cleanCreator();
        cleanOpponent();
        if (rivalInitContainers.stream().anyMatch(e -> e.getOpponentProfile().getTag().equals(tag) || e.getCreatorProfile().getTag().equals(tag))) {
            return putErrorCode(model);
        }
        RivalInitContainer rivalInitContainer = prepareContainer(tag, type);
        if (rivalInitContainer == null) {
            return putErrorCode(model);
        }
        rivalInitContainers.add(rivalInitContainer);
        return putSuccessCode(model);
    }

    private void cleanCreator() {
        rivalInitContainers.removeIf(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()));
    }

    private void cleanOpponent() {
        rivalInitContainers.removeIf(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()));
    }

    public Map cancelFriend() {
        Map<String, Object> model = new HashMap<>();
        rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanCreator();
        return model;
    }

    public Map acceptFriend() {
        Map<String, Object> model = new HashMap<>();
        RivalInitContainer rival = rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId())).findFirst().get();
        if (rival.getType() == RivalType.BATTLE) {
            BattleManager battleManager = new BattleManager(rival, battleService, profileConnectionService);
            battleService.getProfileIdToRivalManagerMap().put(rival.getCreatorProfile().getId(), battleManager);
            battleService.getProfileIdToRivalManagerMap().put(rival.getOpponentProfile().getId(), battleManager);
        } else if (rival.getType() == RivalType.WAR) {
            WarManager warManager = new WarManager(rival, warService, profileConnectionService);
            warService.getProfileIdToRivalManagerMap().put(rival.getCreatorProfile().getId(), warManager);
            warService.getProfileIdToRivalManagerMap().put(rival.getOpponentProfile().getId(), warManager);
        }
        this.sendAcceptInvite(rival);
        model.put("type", rival.getType().name());
        return model;
    }

    public Map rejectFriend() {
        Map<String, Object> model = new HashMap<>();
        rivalInitContainers.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(sessionService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanOpponent();
        return model;
    }

    private RivalInitContainer prepareContainer(String tag, RivalType type) {
        Profile opponentProfile = profileService.getProfile(tag);
        Profile creatorProfile = profileService.getProfile();
        if (type == RivalType.WAR && !opponentProfile.getTeamInitialized() || !creatorProfile.getTeamInitialized()) {
            return null;
        }
        ProfileConnection opponentProfileConnection = profileConnectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
        if (opponentProfileConnection == null) {
            logger.error("Not connected profile with tag: {}, sessionProfileId: {}", tag, sessionService.getProfileId());
            return null;
        }
        RivalInitContainer battle = new RivalInitContainer(type, RivalImportance.FRIEND, creatorProfile, opponentProfile);
        sendInvite(battle);
        return battle;
    }

    private void sendInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            FriendDTO friendDTO = new FriendDTO(rivalInitContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true);
            Map<String, Object> model = new HashMap<>();
            model.put("friend", friendDTO);
            model.put("type", rivalInitContainer.getType().name());
            String content = null;
            try {
                content = new ObjectMapper().writeValueAsString(model);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_INVITE, content).toString());
        });
    }

    private void sendCancelInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_CANCEL_INVITE, "").toString());
        });
    }

    private void sendRejectInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_REJECT_INVITE, "").toString());
        });
    }

    private void sendAcceptInvite(RivalInitContainer rivalInitContainer) {
        profileConnectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_ACCEPT_INVITE, rivalInitContainer.getType().name()).toString());
        });
    }


}
