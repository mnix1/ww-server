package com.ww.model.entity.outside.rival;

import com.ww.model.constant.rival.RivalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.ww.service.rival.global.RivalSeasonService.SEASON_RIVAL_COUNT;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private RivalType rivalType;
    private Integer monthId = 0;
    private Long remaining = SEASON_RIVAL_COUNT;
    private Instant startDate = Instant.now();
    private Instant closeDate;
    private String positions;

    public Season(Season previousSeason) {
        this.rivalType = previousSeason.getRivalType();
        LocalDateTime previousStartLocalDateTime = previousSeason.startLocalDateTime();
        if (previousStartLocalDateTime.getMonthValue() == startLocalDateTime().getMonthValue()) {
            this.monthId = previousSeason.getMonthId() + 1;
        } else {
            this.monthId = 1;
        }
    }

    public LocalDateTime startLocalDateTime() {
        return LocalDateTime.ofInstant(startDate, ZoneId.systemDefault());
    }

    public boolean rivalPlayed() {
        remaining--;
        return remaining <= 0;
    }

    public String getName() {
        LocalDateTime ldt = startLocalDateTime();
        return ldt.getYear() + "/" + ldt.getMonthValue() + "/" + monthId;
    }
}
