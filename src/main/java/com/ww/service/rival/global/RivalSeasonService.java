package com.ww.service.rival.global;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.Season;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.SeasonRepository;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.rival.global.RivalClassificationService.CLASSIFICATION_POSITIONS_COUNT;

@Service
public class RivalSeasonService {
    public static final Long SEASON_RIVAL_COUNT = 3L;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RewardService rewardService;

    public Optional<Season> previous(RivalType type) {
        return seasonRepository.findFirstByRivalTypeOrderByStartDateDesc(type);
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
        return seasonRepository.findFirstByRivalTypeAndCloseDateIsNull(type).orElseGet(() -> create(type));
    }

    @Transactional
    public void update(RivalType type) {
        Season season = actual(type);
        if (season.rivalPlayed()) {
            endSeason(season);
        }
        seasonRepository.save(season);
    }

    public void endSeason(Season season) {
        season.setCloseDate(Instant.now());
        List<Profile> profiles = profileService.classification(season.getRivalType());
        String positions = StringUtils.join(profiles.stream().limit(CLASSIFICATION_POSITIONS_COUNT).map(Profile::getTag).collect(Collectors.toList()), ",");
        season.setPositions(positions);
        updateEloGiveReward(profiles, season.getRivalType());
        profileService.save(profiles);
    }

    public void updateEloGiveReward(List<Profile> profiles, RivalType type) {
        profiles.parallelStream().forEach(profile -> {
            Grade grade = Grade.fromElo(profile.getElo(type));
            profile.setHalfElo(type);
            rewardService.addRewardFromSeason(profile, grade);
        });
    }
}
