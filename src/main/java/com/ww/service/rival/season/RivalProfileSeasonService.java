package com.ww.service.rival.season;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.season.ProfileSeasonRepository;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.ww.helper.EloHelper.*;

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
    @Autowired
    private RivalProfileSeasonService rivalProfileSeasonService;

    public List<ProfileSeason> findProfileSeasons(Long seasonId) {
        return profileSeasonRepository.findAllBySeason_IdOrderByEloDescPreviousEloDescUpdateDateAsc(seasonId);
    }

    public void save(ProfileSeason profileSeason) {
        profileSeasonRepository.save(profileSeason);
    }

    @Transactional
    public void addProfileSeasons(RivalTwoPlayerInit init) {
        Season season = rivalSeasonService.actual(init.getType());
        init.setCreatorProfileSeason(findOrCreateProfileSeason(season, init.getCreatorProfile()));
        init.setOpponentProfileSeason(findOrCreateProfileSeason(season, init.getOpponentProfile()));
    }

    public ProfileSeason findOrCreateProfileSeason(Season season, Profile profile) {
        return profileSeasonRepository.findFirstBySeason_IdAndProfile_Id(season.getId(), profile.getId()).orElseGet(() -> createProfileSeason(season, profile));
    }

    public ProfileSeason createProfileSeason(Season season, Profile profile) {
        Optional<ProfileSeason> optionalPreviousProfileSeason = findPreviousProfileSeason(season.getType(), profile.getId());
        if (optionalPreviousProfileSeason.isPresent()) {
            ProfileSeason previousProfileSeason = optionalPreviousProfileSeason.get();
            return new ProfileSeason(previousProfileSeason, season);
        }
        return new ProfileSeason(profile, season);
    }

    public Optional<ProfileSeason> findPreviousProfileSeason(RivalType type, Long profileId) {
        List<ProfileSeason> profileSeasons = profileSeasonRepository.findAllBySeason_TypeAndProfile_IdOrderBySeason_StartDateDesc(type, profileId);
        if (profileSeasons.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(profileSeasons.get(0));
    }

    @Transactional
    public void updateProfilesElo(RivalModel model) {
//        RivalType type = model.getType();
        //TODO add fix when season changes during rival
//        Season season = rivalSeasonService.actual(type);
        Profile winner = model.getWinner();
        ProfileSeason creator = model.getCreatorProfileSeason();
        ProfileSeason opponent = model.getOpponentProfileSeason();
        Long creatorEloChange;
        Long opponentEloChange;
        if (model.getDraw()) {
            creatorEloChange = prepareEloChange(creator.getElo(), opponent.getElo(), DRAW);
            opponentEloChange = prepareEloChange(opponent.getElo(), creator.getElo(), DRAW);
        } else {
            creatorEloChange = prepareEloChange(creator.getElo(), opponent.getElo(), creator.getProfile().equals(winner) ? WINNER : LOOSER);
            opponentEloChange = prepareEloChange(opponent.getElo(), creator.getElo(), opponent.getProfile().equals(winner) ? WINNER : LOOSER);
        }
        Instant updateDate = Instant.now();
        creator.setUpdateDate(updateDate);
        opponent.setUpdateDate(updateDate);
        creator.updateElo(creatorEloChange);
        opponent.updateElo(opponentEloChange);
        save(creator);
        save(opponent);
    }
}
