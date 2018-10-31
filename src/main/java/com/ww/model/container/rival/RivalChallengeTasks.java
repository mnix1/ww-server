package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.service.rival.challenge.RivalChallengeService;

import java.util.List;

public class RivalChallengeTasks extends RivalTasks {
    private ChallengeProfile challengeProfile;
    private List<ChallengePhase> challengePhases;

    public RivalChallengeTasks(RivalChallengeService service, ChallengeProfile challengeProfile, List<ChallengePhase> challengePhases) {
        super(service);
        this.challengeProfile = challengeProfile;
        this.challengePhases = challengePhases;
    }

    @Override
    public void prepareNext(Category category, DifficultyLevel difficultyLevel, Language language) {
        ChallengePhase phase = findPhase(category, difficultyLevel);
        super.prepareNext(phase.getTaskType().getCategory(), phase.getDifficultyLevel(), language);
    }

    public ChallengePhase findPhase(Category category, DifficultyLevel difficultyLevel) {
        if (challengePhases.size() <= nextTaskIndex) {
            challengePhases.add(((RivalChallengeService) service).preparePhase(challengeProfile.getChallenge(), nextTaskIndex, category, difficultyLevel));
        }
        return challengePhases.get(nextTaskIndex);
    }
}
