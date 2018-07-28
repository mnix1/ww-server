package com.ww.model.dto.task;

import lombok.Getter;

import java.util.List;

@Getter
public class BattleSummaryDTO {

    private Long id;
    private List<BattlePositionDTO> positions;

    public BattleSummaryDTO(Long id, List<BattlePositionDTO> positions) {
        this.id = id;
        this.positions = positions;
    }
}
