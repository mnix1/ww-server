package com.ww.service.rival.challenge;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengePhaseRepository;
import com.ww.service.rival.war.RivalWarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ww.model.constant.rival.RivalType.CHALLENGE;

@Service
public class RivalChallengeService extends RivalWarService {
    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;
    @Autowired
    private ChallengePhaseRepository challengePhaseRepository;
    @Autowired
    private ChallengeCloseService challengeCloseService;

    public RivalChallengeInit init(ChallengeProfile challengeProfile) {
        List<ChallengePhase> challengePhases = new ArrayList<>(challengeProfile.getChallenge().getQuestions());
        sortChallengePhases(challengePhases);
        return new RivalChallengeInit(CHALLENGE, RivalImportance.FAST, challengeProfile.getProfile(), challengeProfile, challengePhases);
    }

    public synchronized ChallengePhase preparePhase(ChallengeProfile challengeProfile, int taskIndex, Category category, DifficultyLevel difficultyLevel) {
        List<ChallengePhase> challengePhases = challengePhaseRepository.findAllByChallenge_Id(challengeProfile.getChallenge().getId());
        sortChallengePhases(challengePhases);
        if (challengePhases.size() > taskIndex) {
            return challengePhases.get(taskIndex);
        }
        Challenge challenge = challengeProfile.getChallenge();
        ChallengePhase challengePhase = new ChallengePhase(challenge, getTaskGenerateService().findProperTaskType(category, difficultyLevel), difficultyLevel, Language.NO_COMMON);
        challengePhaseRepository.save(challengePhase);
        return challengePhase;
    }

    private void sortChallengePhases(List<ChallengePhase> challengePhases) {
        challengePhases.sort(Comparator.comparing(ChallengePhase::getId));
    }

    @Override
    public void disposeManager(RivalManager manager) {
        super.disposeManager(manager);
        ChallengeManager challengeManager = (ChallengeManager) manager;
        ChallengeProfile challengeProfile = challengeManager.challengeProfile;
        challengeProfile.setResponseEnd(Instant.now());
        challengeProfile.setResponseStatus(ChallengeProfileResponse.CLOSED);
        challengeProfile.setScore(Math.max(0, manager.getModel().getCurrentTaskIndex()));
        challengeProfileRepository.save(challengeProfile);
        challengeCloseService.maybeCloseChallenge(challengeProfile.getChallenge(), Instant.now());
    }

}
