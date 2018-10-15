package com.ww.model.dto.rival.challenge;

import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

import java.util.List;

@Getter
public class ChallengeSummaryDTO extends ChallengePrivateDTO {

    private List<ChallengePositionDTO> positions;

    public ChallengeSummaryDTO(Challenge challenge, List<ChallengePositionDTO> positions) {
        super(challenge);
        this.positions = positions;
    }
}
