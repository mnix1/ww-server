package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.rival.challenge.ChallengeProfileType;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import lombok.Getter;

@Getter
public class ChallengeActiveDTO extends ChallengePrivateDTO {

    private Boolean canResponse;
    private Boolean joined;
    private Boolean canTryAgain;
    private ChallengeProfileType type;

    public ChallengeActiveDTO(ChallengeProfile challengeProfile) {
        super(challengeProfile.getChallenge());
        this.joined = challengeProfile.getJoined();
        this.type = challengeProfile.getType();
        this.canTryAgain = joined && approach == ChallengeApproach.MANY && challengeProfile.getResponseStatus() == ChallengeProfileResponse.CLOSED;
        this.canResponse = joined && challengeProfile.getResponseStatus() == ChallengeProfileResponse.OPEN;
    }
}
