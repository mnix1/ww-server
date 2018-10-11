package com.ww.model.entity.outside.rival.season;

import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static com.ww.helper.EloHelper.MIN_ELO;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileSeason {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    protected Long elo;
    protected Long previousElo;
    protected Long highestElo;
    protected Instant updateDate;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;
    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false, updatable = false)
    protected Season season;

    public ProfileSeason(Long elo, Long previousElo, Long highestElo, Instant updateDate, Profile profile, Season season) {
        this.elo = elo;
        this.previousElo = previousElo;
        this.highestElo = highestElo;
        this.updateDate = updateDate;
        this.profile = profile;
        this.season = season;
    }

    public void updateElo(Long change) {
        previousElo = elo;
        elo = Math.max(MIN_ELO, elo + change);
        highestElo = Math.max(elo, previousElo);
    }
}
