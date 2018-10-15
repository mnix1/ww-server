package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import lombok.Getter;

@Getter
public class ChallengeActiveDTO extends ChallengePrivateDTO {

    Boolean canResponse;

    public ChallengeActiveDTO(ChallengeProfile challengeProfile) {
        super(challengeProfile.getChallenge());
        this.canResponse = challengeProfile.getResponseStatus() == ChallengeProfileResponse.OPEN;
    }
}
