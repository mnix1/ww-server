package com.ww.service.rival.season;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.season.ProfileSeasonRepository;
import com.ww.repository.outside.rival.season.SeasonGradeRepository;
import com.ww.repository.outside.rival.season.SeasonRepository;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RivalSeasonService {
    public static final Long SEASON_RIVAL_COUNT = 1000L;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeasonGradeRepository seasonGradeRepository;

    public List<SeasonGrade> findSeasonGrades(RivalType type) {
        return seasonGradeRepository.findAllByType(type);
    }

    public Optional<Season> findSeason(Long seasonId) {
        return seasonRepository.findById(seasonId);
    }

    public Map<Grade, SeasonGrade> findSeasonGradesMap(RivalType type) {
        return findSeasonGrades(type).stream().collect(Collectors.toMap(SeasonGrade::getGrade, o -> o));
    }

    public Optional<Season> previous(RivalType type) {
        return seasonRepository.findFirstByTypeOrderByOpenDateDesc(type);
    }

    public Season create(RivalType type) {
        return create(previous(type).orElse(new Season(type)));
    }

    public Season create(Season previous) {
        Season season = new Season(previous);
        seasonRepository.save(season);
        return season;
    }

    @Transactional
    public Season actual(RivalType type) {
        return seasonRepository.findFirstByTypeAndCloseDateIsNull(type).orElseGet(() -> create(type));
    }

    public boolean update(Season season) {
        season = findSeason(season.getId()).get();
        boolean shouldReward = false;
        if (season.rivalPlayed()) {
            shouldReward = true;
            if (!season.isClosed()) {
                season.setCloseDate(Instant.now());
            }
        }
        seasonRepository.save(season);
        return shouldReward;
    }
}
