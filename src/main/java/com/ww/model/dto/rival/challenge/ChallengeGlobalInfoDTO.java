package com.ww.model.dto.rival.challenge;

import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ChallengeGlobalInfoDTO {

    private Long id;
    private Instant timeoutDate;
    private Boolean canResponse = false;

    public ChallengeGlobalInfoDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.timeoutDate = challenge.getTimeoutDate();
    }

    public ChallengeGlobalInfoDTO(Challenge challenge, Boolean canResponse) {
        this(challenge);
        this.canResponse = canResponse;
    }
}
