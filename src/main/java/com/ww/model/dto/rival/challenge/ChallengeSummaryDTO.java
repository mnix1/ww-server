package com.ww.model.dto.rival.challenge;

import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChallengeSummaryDTO extends ChallengePrivateDTO {

    private List<ChallengePositionDTO> positions;

    public ChallengeSummaryDTO(Challenge challenge, List<ChallengePosition> positions, String tag) {
        super(challenge);
        this.positions = positions.stream().limit(20).map((ChallengePosition challengePosition) -> new ChallengePositionDTO(challengePosition, tag)).collect(Collectors.toList());
    }
}
