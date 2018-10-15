package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ChallengePrivateDTO {

    private Long id;
    private String name;
    private WisorType wisorType;
    private Instant creationDate;
    private Long timeoutInterval;
    private ChallengeAccess access;
    private Long participants;
    private Resources cost;
    private Resources gain;

    public ChallengePrivateDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.name = challenge.getCreatorProfile().getName();
        this.wisorType = challenge.getCreatorProfile().getWisorType();
        this.creationDate = challenge.getCreationDate();
        this.timeoutInterval = challenge.getTimeoutInterval();
        this.access = challenge.getAccess();
        this.participants = challenge.getParticipants();
        this.cost = challenge.getCostResources();
        this.gain = challenge.getGainResources();
    }
}
