package com.ww.model.dto.social;

import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.service.rival.season.RivalSeasonService.SEASON_RIVAL_COUNT;

@Getter
public class ClassificationDTO {

    private String name;
    private Instant startDate;
    private Instant closeDate;
    private int done;

    private List<SeasonGradeDTO> grades;
    private List<ClassificationPositionDTO> positions;

    public ClassificationDTO(Season season, List<SeasonGrade> seasonGrades, List<ClassificationPositionDTO> positions) {
        this.positions = positions;
        this.name = season.getName();
        this.startDate = season.getOpenDate();
        this.closeDate = season.getCloseDate();
        this.done = Math.min(99, (int) ((SEASON_RIVAL_COUNT - season.getRemaining()) * 100 / SEASON_RIVAL_COUNT));
        this.grades = seasonGrades.stream().map(SeasonGradeDTO::new).collect(Collectors.toList());
    }
}
