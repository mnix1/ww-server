package com.ww.service.rival.season;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileMail;
import com.ww.game.play.container.PlayContainer;
import com.ww.repository.outside.rival.season.ProfileSeasonRepository;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.MailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.EloHelper.*;

@Service
@AllArgsConstructor
public class RivalProfileSeasonService {
    private static final int RIVAL_REWARD_JOB_RATE = 300000;

    private final ProfileSeasonRepository profileSeasonRepository;
    private final RivalSeasonService rivalSeasonService;
    private final RivalGlobalService rivalGlobalService;
    private final MailService mailService;

    public List<ProfileSeason> findProfileSeasons(Long seasonId) {
        return profileSeasonRepository.findAllBySeason_IdOrderByEloDescPreviousEloDescUpdateDateAsc(seasonId);
    }

    public void save(ProfileSeason profileSeason) {
        profileSeasonRepository.save(profileSeason);
    }

    public void save(List<ProfileSeason> profileSeasons) {
        profileSeasonRepository.saveAll(profileSeasons);
    }

    @Transactional
    public void addProfileSeasons(RivalTwoInit init) {
        Season season = rivalSeasonService.actual(init.getType());
        List<SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGrades(season.getType());
        init.setSeason(season);
        init.setCreatorProfileSeason(findOrCreateProfileSeason(season, seasonGrades, init.getCreatorProfile()));
        init.setOpponentProfileSeason(findOrCreateProfileSeason(season, seasonGrades, init.getOpponentProfile()));
    }

    public ProfileSeason findOrCreateProfileSeason(Season season, List<SeasonGrade> seasonGrades, Profile profile) {
        return profileSeasonRepository.findFirstBySeason_IdAndProfile_Id(season.getId(), profile.getId()).orElseGet(() -> createProfileSeason(season, seasonGrades, profile));
    }

    public ProfileSeason createProfileSeason(Season season, List<SeasonGrade> seasonGrades, Profile profile) {
        Optional<ProfileSeason> optionalPreviousProfileSeason = findPreviousProfileSeason(season.getType(), profile.getId());
        ProfileSeason profileSeason;
        if (optionalPreviousProfileSeason.isPresent()) {
            ProfileSeason previousProfileSeason = optionalPreviousProfileSeason.get();
            profileSeason = new ProfileSeason(previousProfileSeason, season, seasonGrades);
        } else {
            profileSeason = new ProfileSeason(profile, season, seasonGrades);
        }
        profileSeasonRepository.save(profileSeason);
        return profileSeason;
    }

    public Optional<ProfileSeason> findPreviousProfileSeason(RivalType type, Long profileId) {
        List<ProfileSeason> profileSeasons = profileSeasonRepository.findAllBySeason_TypeAndProfile_IdOrderBySeason_OpenDateDesc(type, profileId);
        if (profileSeasons.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(profileSeasons.get(0));
    }

    @Transactional
    public void update(Season season) {
        if (rivalSeasonService.update(season)) {
            rewardProfiles(season);
        }
    }

    public List<ProfileSeason> findAllNotRewardedProfileSeasons() {
        return filterProfileSeasonsNotPlaying(profileSeasonRepository.findAllBySeason_CloseDateNotNullAndRewarded(false));
    }

    public List<ProfileSeason> findSeasonNotRewardedProfileSeasons(Season season) {
        return filterProfileSeasonsNotPlaying(profileSeasonRepository.findAllBySeason_IdAndRewarded(season.getId(), false));
    }

    public List<ProfileSeason> filterProfileSeasonsNotPlaying(List<ProfileSeason> profileSeasons) {
        return profileSeasons.stream().filter(profileSeason -> {
            if (!rivalGlobalService.contains(profileSeason.getProfile().getId())) {
                return true;
            }
            return rivalGlobalService.get(profileSeason.getProfile().getId()).getContainer().getInit().getImportance() != RivalImportance.RANKING;
        }).collect(Collectors.toList());
    }

    public void rewardProfiles(Season season) {
        Map<Grade, SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGradesMap(season.getType());
        List<ProfileSeason> profileSeasons = findSeasonNotRewardedProfileSeasons(season);
        rewardProfiles(profileSeasons, seasonGrades);
    }

    public void rewardProfiles(List<ProfileSeason> profileSeasons, Map<Grade, SeasonGrade> seasonGrades) {
        List<ProfileMail> mails = new ArrayList<>();
        for (ProfileSeason profileSeason : profileSeasons) {
            profileSeason.setRewarded(true);
            SeasonGrade seasonGrade = seasonGrades.get(profileSeason.getGrade());
            mails.add(mailService.prepareSeasonResultsMail(profileSeason, seasonGrade));
        }
        mailService.save(mails);
        save(profileSeasons);
        mailService.sendNewMailMessage(mails);
    }

    @Transactional
    public void updateProfilesElo(PlayContainer container) {
        List<SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGrades(container.getInit().getType());
        Profile winner = container.getResult().getWinner();
        ProfileSeason creator = container.getInit().getCreatorProfileSeason();
        ProfileSeason opponent = container.getInit().getOpponentProfileSeason();
        Long creatorEloChange;
        Long opponentEloChange;
        if (container.getResult().getDraw()) {
            creatorEloChange = prepareEloChange(creator.getElo(), opponent.getElo(), DRAW);
            opponentEloChange = prepareEloChange(opponent.getElo(), creator.getElo(), DRAW);
        } else {
            creatorEloChange = prepareEloChange(creator.getElo(), opponent.getElo(), creator.getProfile().equals(winner) ? WINNER : LOOSER);
            opponentEloChange = prepareEloChange(opponent.getElo(), creator.getElo(), opponent.getProfile().equals(winner) ? WINNER : LOOSER);
        }
        Instant updateDate = Instant.now();
        creator.setUpdateDate(updateDate);
        opponent.setUpdateDate(updateDate);
        creator.updateElo(creatorEloChange, seasonGrades);
        opponent.updateElo(opponentEloChange, seasonGrades);
        save(creator);
        save(opponent);
    }

    @Transactional
    @Scheduled(fixedRate = RIVAL_REWARD_JOB_RATE)
    public synchronized void maybeRewardProfiles() {
        List<ProfileSeason> profileSeasons = findAllNotRewardedProfileSeasons();
        if (profileSeasons.isEmpty()) {
            return;
        }
        List<ProfileSeason> warProfileSeasons = new ArrayList<>();
        List<ProfileSeason> battleProfileSeasons = new ArrayList<>();
        for (ProfileSeason profileSeason : profileSeasons) {
            if (profileSeason.getSeason().getType() == RivalType.WAR) {
                warProfileSeasons.add(profileSeason);
            } else if (profileSeason.getSeason().getType() == RivalType.BATTLE) {
                battleProfileSeasons.add(profileSeason);
            }
        }
        if (!warProfileSeasons.isEmpty()) {
            Map<Grade, SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGradesMap(RivalType.WAR);
            rewardProfiles(warProfileSeasons, seasonGrades);
        }
        if (!battleProfileSeasons.isEmpty()) {
            Map<Grade, SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGradesMap(RivalType.BATTLE);
            rewardProfiles(battleProfileSeasons, seasonGrades);
        }
    }
}
