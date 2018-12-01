package com.ww.service.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.Connection;
import com.ww.model.container.MapModel;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.dto.social.FriendDTOExtended;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class RivalInitFriendService {
    private static final Logger logger = LoggerFactory.getLogger(RivalInitFriendService.class);

    private static final List<RivalTwoInit> waitingForActionProfiles = new CopyOnWriteArrayList<>();

    private final ProfileService profileService;
    private final ConnectionService connectionService;
    private final RivalRunService rivalRunService;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;
    private final RivalGlobalService rivalGlobalService;

    public Map startFriend(String tag, RivalType type) {
        Map<String, Object> model = new HashMap<>();
        cleanCreator();
        cleanOpponent();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        if (waitingForActionProfiles.stream().anyMatch(e -> e.getOpponentProfile().getTag().equals(tag) || e.getCreatorProfile().getTag().equals(tag))) {
            return putErrorCode(model);
        }
        Profile opponentProfile = profileService.getProfile(tag);
        if (rivalInitRandomOpponentService.contains(opponentProfile.getId()) || rivalGlobalService.contains(opponentProfile.getId())) {
            return putErrorCode(model);
        }
        RivalTwoInit rivalInitContainer = prepareContainer(opponentProfile, type);
        if (rivalInitContainer == null) {
            return putErrorCode(model);
        }
        waitingForActionProfiles.add(rivalInitContainer);
        return putSuccessCode(model);
    }

    private void cleanCreator() {
        waitingForActionProfiles.removeIf(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(profileService.getProfileId()));
    }

    private void cleanOpponent() {
        waitingForActionProfiles.removeIf(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(profileService.getProfileId()));
    }

    public Map cancelFriend() {
        Map<String, Object> model = new HashMap<>();
        waitingForActionProfiles.stream().filter(rivalInitContainer -> rivalInitContainer.getCreatorProfile().getId().equals(profileService.getProfileId()))
                .forEach(this::sendCancelInvite);
        this.cleanCreator();
        return model;
    }

    public Map acceptFriend() {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        RivalTwoInit rival = waitingForActionProfiles.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(profileService.getProfileId())).findFirst().get();
        this.sendAcceptInvite(rival);
        rivalRunService.run(rival);
        this.cleanCreator();
        this.cleanOpponent();
        model.put("type", rival.getType().name());
        return model;
    }

    public Map rejectFriend() {
        Map<String, Object> model = new HashMap<>();
        waitingForActionProfiles.stream().filter(rivalInitContainer -> rivalInitContainer.getOpponentProfile().getId().equals(profileService.getProfileId()))
                .forEach(this::sendRejectInvite);
        this.cleanOpponent();
        return model;
    }

    private RivalTwoInit prepareContainer(Profile opponentProfile, RivalType type) {
        Profile creatorProfile = profileService.getProfile();
        Connection opponentConnection = connectionService.findByProfileId(opponentProfile.getId()).orElseGet(null);
        if (opponentConnection == null) {
            logger.error("Not connected profile with tag: {}, sessionProfileId: {}", opponentProfile.getTag(), profileService.getProfileId());
            return null;
        }
        RivalTwoInit init = new RivalTwoInit(type, RivalImportance.FRIEND, creatorProfile, opponentProfile);
        sendInvite(init);
        return init;
    }

    private void sendInvite(RivalTwoInit rivalInitContainer) {
        connectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            FriendDTOExtended friendDTO = new FriendDTOExtended(rivalInitContainer.getCreatorProfile(), FriendStatus.ACCEPTED, true);
            String content = new MapModel("friend", friendDTO).put("type", rivalInitContainer.getType().name()).toString();
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_INVITE, content).toString());
        });
    }

    private void sendCancelInvite(RivalTwoInit rivalInitContainer) {
        connectionService.findByProfileId(rivalInitContainer.getOpponentProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_CANCEL_INVITE, "").toString());
        });
    }

    private void sendRejectInvite(RivalTwoInit rivalInitContainer) {
        connectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_REJECT_INVITE, "").toString());
        });
    }

    private void sendAcceptInvite(RivalTwoInit rivalInitContainer) {
        connectionService.findByProfileId(rivalInitContainer.getCreatorProfile().getId()).ifPresent(profileConnection -> {
            profileConnection.sendMessage(new MessageDTO(Message.RIVAL_ACCEPT_INVITE, rivalInitContainer.getType().name()).toString());
        });
    }
}
