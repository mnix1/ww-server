package com.ww.database;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.repository.outside.rival.season.SeasonGradeRepository;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Service
public class InitGradeService {
    @Autowired
    private SeasonGradeRepository seasonGradeRepository;

    public void initSeasonGrades() {
        List<SeasonGrade> seasonGrades = new ArrayList<>();
        seasonGrades.add(new SeasonGrade(Grade.A, RivalType.WAR, 1600L, null, new Resources(100L, 80L, 80L, 120L)));
        seasonGrades.add(new SeasonGrade(Grade.B, RivalType.WAR, 800L, 1599L, new Resources(50L, 40L, 40L, 60L)));
        seasonGrades.add(new SeasonGrade(Grade.C, RivalType.WAR, 400L, 799L, new Resources(25L, 20L, 20L, 30L)));
        seasonGrades.add(new SeasonGrade(Grade.D, RivalType.WAR, 200L, 399L, new Resources(13L, 10L, 10L, 15L)));
        seasonGrades.add(new SeasonGrade(Grade.E, RivalType.WAR, 100L, 199L, new Resources(7L, 5L, 5L, 8L)));
        seasonGrades.add(new SeasonGrade(Grade.F, RivalType.WAR, 0L, 99L, new Resources(0L, 0L, 0L, 0L)));

        seasonGrades.add(new SeasonGrade(Grade.A, RivalType.BATTLE, 2000L, null, new Resources(60L, 40L, 40L, 60L)));
        seasonGrades.add(new SeasonGrade(Grade.B, RivalType.BATTLE, 1000L, 1099L, new Resources(30L, 20L, 20L, 30L)));
        seasonGrades.add(new SeasonGrade(Grade.C, RivalType.BATTLE, 500L, 999L, new Resources(15L, 10L, 10L, 15L)));
        seasonGrades.add(new SeasonGrade(Grade.D, RivalType.BATTLE, 250L, 499L, new Resources(8L, 5L, 5L, 8L)));
        seasonGrades.add(new SeasonGrade(Grade.E, RivalType.BATTLE, 125L, 124L, new Resources(4L, 3L, 3L, 4L)));
        seasonGrades.add(new SeasonGrade(Grade.F, RivalType.BATTLE, 0L, 124L, new Resources(0L, 0L, 0L, 0L)));
        seasonGradeRepository.saveAll(seasonGrades);
    }

}
