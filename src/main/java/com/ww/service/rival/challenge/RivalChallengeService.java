package com.ww.service.rival.challenge;

import com.ww.helper.RandomHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ChallengePhaseWisie;
import com.ww.play.PlayChallengeManager;
import com.ww.play.PlayManager;
import com.ww.repository.outside.rival.challenge.ChallengePhaseRepository;
import com.ww.repository.outside.rival.challenge.ChallengePhaseWisieRepository;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.model.constant.rival.RivalType.CHALLENGE;

@Service
public class RivalChallengeService extends RivalWarService {
    private final ChallengeProfileRepository challengeProfileRepository;
    private final ChallengePhaseRepository challengePhaseRepository;
    private final ChallengeCloseService challengeCloseService;
    private final ChallengePhaseWisieRepository challengePhaseWisieRepository;

    public RivalChallengeService(ProfileConnectionService profileConnectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RewardService rewardService, RivalSeasonService rivalSeasonService, ProfileService profileService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, ProfileWisieService profileWisieService, TaskService taskService, ChallengeProfileRepository challengeProfileRepository, ChallengePhaseRepository challengePhaseRepository, ChallengeCloseService challengeCloseService, ChallengePhaseWisieRepository challengePhaseWisieRepository) {
        super(profileConnectionService, taskGenerateService, taskRendererService, rewardService, rivalSeasonService, profileService, rivalGlobalService, rivalProfileSeasonService, profileWisieService, taskService);
        this.challengeProfileRepository = challengeProfileRepository;
        this.challengePhaseRepository = challengePhaseRepository;
        this.challengeCloseService = challengeCloseService;
        this.challengePhaseWisieRepository = challengePhaseWisieRepository;
    }

    public RivalChallengeInit init(ChallengeProfile challengeProfile) {
        List<ChallengePhase> challengePhases = new CopyOnWriteArrayList<>(challengeProfile.getChallenge().getPhases());
        sortChallengePhases(challengePhases);
        return new RivalChallengeInit(CHALLENGE, RivalImportance.FAST, challengeProfile.getProfile(), prepareComputerProfile(), challengeProfile, challengePhases);
    }

    @Override
    public void addRewardFromWin(Profile profile) {
    }

    public synchronized ChallengePhase preparePhase(Challenge challenge, int taskIndex, Category category, DifficultyLevel difficultyLevel) {
        List<ChallengePhase> challengePhases = challengePhaseRepository.findAllByChallenge_Id(challenge.getId());
        sortChallengePhases(challengePhases);
        if (challengePhases.size() > taskIndex) {
            return challengePhases.get(taskIndex);
        }
        ChallengePhase challengePhase = new ChallengePhase(challenge, getTaskGenerateService().findProperTaskType(category, difficultyLevel), difficultyLevel, preparePhaseWisie(taskIndex));
        challengePhaseRepository.save(challengePhase);
        return challengePhase;
    }

    public ChallengePhaseWisie preparePhaseWisie(int taskIndex) {
        ChallengePhaseWisie phaseWisie = new ChallengePhaseWisie(true, new HashSet<>(Category.random(Math.max(1, Math.min(3, taskIndex / 4)))), new HashSet<>(), WisieType.random());
        getProfileWisieService().initWisieAttributes(phaseWisie);
        double promo = RandomHelper.randomDouble(0.8, 1) * taskIndex * 20 + taskIndex * 10;
        for (MentalAttribute attribute : MentalAttribute.values()) {
            phaseWisie.setMentalAttributeValue(attribute, Math.pow(phaseWisie.getMentalAttributeValue(attribute), 1.1) + promo);
        }
        for (WisdomAttribute attribute : WisdomAttribute.values()) {
            phaseWisie.setWisdomAttributeValue(attribute, Math.pow(phaseWisie.getWisdomAttributeValue(attribute), 1.1) + promo);
        }
        challengePhaseWisieRepository.save(phaseWisie);
        return phaseWisie;
    }

    private void sortChallengePhases(List<ChallengePhase> challengePhases) {
        challengePhases.sort(Comparator.comparing(ChallengePhase::getId));
    }

    @Override
    public void disposeManager(PlayManager manager) {
        super.disposeManager(manager);
        PlayChallengeManager challengeManager = (PlayChallengeManager) manager;
        ChallengeProfile challengeProfile = ((RivalChallengeInit) challengeManager.getContainer().getInit()).getChallengeProfile();
        challengeProfile.setResponseEnd(Instant.now());
        challengeProfile.setResponseStatus(ChallengeProfileResponse.CLOSED);
        challengeProfile.setScore(Math.max(0, ((ChallengeTeam) manager.getContainer().getTeams().team(challengeProfile.getProfile().getId())).getScore()));
        challengeProfileRepository.save(challengeProfile);
        challengeCloseService.maybeCloseChallenge(challengeProfile.getChallenge(), Instant.now());
    }

}
