package com.ww.model.dto.social;

import com.ww.model.constant.Grade;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.season.Season;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.ww.service.rival.season.RivalSeasonService.SEASON_RIVAL_COUNT;

@Getter
public class ClassificationDTO {

    private String name;
    private Instant startDate;
    private Instant closeDate;
    private int done;

    private Map<Grade, Resources> rewards;
    private List<ClassificationPositionDTO> positions;

    public ClassificationDTO(Season season, List<ClassificationPositionDTO> positions) {
        this.positions = positions;
        this.name = season.getName();
        this.startDate = season.getOpenDate();
        this.closeDate = season.getCloseDate();
        this.done = Math.min(99, (int) ((SEASON_RIVAL_COUNT - season.getRemaining()) * 100 / SEASON_RIVAL_COUNT));
        this.rewards = Grade.rewards();
    }
}
