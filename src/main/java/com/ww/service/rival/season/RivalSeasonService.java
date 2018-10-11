package com.ww.service.rival.season;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.repository.outside.rival.season.ProfileSeasonRepository;
import com.ww.repository.outside.rival.season.SeasonRepository;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
public class RivalSeasonService {
    public static final Long SEASON_RIVAL_COUNT = 3L;

    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private ProfileSeasonRepository profileSeasonRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RewardService rewardService;

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
//        List<Profile> profiles = profileService.classification(season.getType());
//        String positions = StringUtils.join(profiles.stream().limit(CLASSIFICATION_POSITIONS_COUNT).map(Profile::getTag).collect(Collectors.toList()), ",");
//        season.setPositions(positions);
//        updateEloGiveReward(profiles, season.getType());
//        profileService.save(profiles);
    }

//    public void updateEloGiveReward(List<Profile> profiles, RivalType type) {
//        profiles.parallelStream().forEach(profile -> {
//            Grade grade = Grade.fromElo(profile.getElo(type));
//            profile.setHalfElo(type);
//            rewardService.addRewardFromSeason(profile, grade);
//        });
//    }
}
