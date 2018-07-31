package com.ww.model.dto.rival.challenge;

import lombok.Getter;

import java.util.List;

@Getter
public class ChallengeSummaryDTO {

    private Long id;
    private List<ChallengePositionDTO> positions;

    public ChallengeSummaryDTO(Long id, List<ChallengePositionDTO> positions) {
        this.id = id;
        this.positions = positions;
    }
}
