package com.ww.model.entity.outside.rival.season;

import com.ww.model.constant.rival.RivalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static com.ww.service.rival.season.RivalSeasonService.SEASON_RIVAL_COUNT;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private RivalType type;
    private Integer monthId = 0;
    private Long remaining = SEASON_RIVAL_COUNT;
    private Instant openDate = Instant.now();
    private Instant closeDate;

    @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
    private Set<ProfileSeason> profiles = new HashSet<>();

    public Season(Season previousSeason) {
        this.type = previousSeason.getType();
        LocalDateTime previousStartLocalDateTime = previousSeason.openLocalDateTime();
        if (previousStartLocalDateTime.getMonthValue() == openLocalDateTime().getMonthValue()) {
            this.monthId = previousSeason.getMonthId() + 1;
        } else {
            this.monthId = 1;
        }
    }

    public Season(RivalType type) {
        this.type = type;
    }

    public LocalDateTime openLocalDateTime() {
        return LocalDateTime.ofInstant(openDate, ZoneId.systemDefault());
    }

    public boolean rivalPlayed() {
        remaining--;
        return remaining <= 0;
    }

    public String getName() {
        LocalDateTime ldt = openLocalDateTime();
        return ldt.getYear() + "/" + ldt.getMonthValue() + " (" + monthId + ")";
    }
}
