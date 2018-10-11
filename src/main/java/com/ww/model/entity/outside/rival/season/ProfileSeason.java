package com.ww.model.entity.outside.rival.season;

import com.ww.model.constant.Grade;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static com.ww.helper.EloHelper.*;

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
    protected Grade grade;
    protected Instant updateDate;
    protected Boolean rewarded = false;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;
    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false, updatable = false)
    protected Season season;

    public ProfileSeason(Profile profile, Season season, List<SeasonGrade> seasonGrades) {
        this.elo = 0L;
        this.previousElo = this.elo;
        this.highestElo = this.elo;
        this.updateDate = Instant.now();
        this.profile = profile;
        this.season = season;
        this.grade = findGrade(elo, seasonGrades);
    }

    public ProfileSeason(ProfileSeason previousProfileSeason, Season season, List<SeasonGrade> seasonGrades) {
        this.elo = eloSeasonEndChange(previousProfileSeason.getElo());
        this.previousElo = this.elo;
        this.highestElo = this.elo;
        this.updateDate = Instant.now();
        this.profile = previousProfileSeason.getProfile();
        this.season = season;
        this.grade = findGrade(elo, seasonGrades);
    }

    public void updateElo(Long change, List<SeasonGrade> seasonGrades) {
        previousElo = elo;
        elo = Math.max(MIN_ELO, elo + change);
        highestElo = Math.max(elo, previousElo);
        grade = findGrade(elo, seasonGrades);
    }
}
