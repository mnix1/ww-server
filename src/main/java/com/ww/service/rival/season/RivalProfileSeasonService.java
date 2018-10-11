package com.ww.service.rival.season;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.season.ProfileSeasonRepository;
import com.ww.repository.outside.rival.season.SeasonRepository;
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

import static com.ww.helper.EloHelper.*;
import static com.ww.helper.EloHelper.LOOSER;
import static com.ww.service.rival.global.RivalClassificationService.CLASSIFICATION_POSITIONS_COUNT;

@Service
public class RivalProfileSeasonService {
    @Autowired
    private ProfileSeasonRepository profileSeasonRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private RivalSeasonService rivalSeasonService;

    public List<ProfileSeason> findProfileSeasons(Long seasonId) {
        return profileSeasonRepository.findAllBySeason_IdOrderByEloDescPreviousEloDescUpdateDateAsc(seasonId);
    }

//    public ProfileSeason createProfileSeason(Season season, Profile profile){
////        return new ProfileSeason()
//    }
//
//    public ProfileSeason findOrCreateProfileSeason(Season season, Profile profile){
//        Optional<ProfileSeason> optionalProfileSeason = profileSeasonRepository.findFirstBySeason_IdAndProfile_Id(season.getId(), profile.getId());
//    }

    @Transactional
    public void updateProfilesElo(RivalModel model) {
//        RivalType type = model.getType();
//        Season season = rivalSeasonService.actual(type);
//        Profile winner = model.getWinner();
//        Profile creator = model.getCreatorProfile();
//        ProfileSeason creatorProfileSeason = rivalSeasonService.findOrCreateProfileSeason(season, creator);
//        Profile opponent = model.getOpponentProfile();
//        Instant lastPlay = Instant.now();
//        Long creatorEloChange = 0L;
//        Long opponentEloChange = 0L;
//        if (type == RivalType.BATTLE) {
//            if (model.getDraw()) {
//                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), DRAW);
//                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), DRAW);
//            } else {
//                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), creator.equals(winner) ? WINNER : LOOSER);
//                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), opponent.equals(winner) ? WINNER : LOOSER);
//            }
//            creator.setBattleLastPlay(lastPlay);
//            opponent.setBattleLastPlay(lastPlay);
//        } else if (type == RivalType.WAR) {
//            if (model.getDraw()) {
//                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), DRAW);
//                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), DRAW);
//            } else {
//                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), creator.equals(winner) ? WINNER : LOOSER);
//                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), opponent.equals(winner) ? WINNER : LOOSER);
//            }
//            creator.setWarLastPlay(lastPlay);
//            opponent.setWarLastPlay(lastPlay);
//        }
//        updateElo(creator, creatorEloChange, type);
//        updateElo(opponent, opponentEloChange, type);
//        model.getCreatorProfile().setElo(type, creator.getElo(type));
//        model.getOpponentProfile().setElo(type, opponent.getElo(type));
//        profileService.save(creator);
//        profileService.save(opponent);
    }
}
