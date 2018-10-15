package com.ww.service.rival.challenge;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeQuestionRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
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
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ChallengeQuestionRepository challengeQuestionRepository;
    @Autowired
    private ChallengeCloseService challengeCloseService;

    public RivalChallengeInit init(ChallengeProfile challengeProfile) {
        List<ChallengeQuestion> challengeQuestions = new ArrayList<>(challengeProfile.getChallenge().getQuestions());
        sortChallengeQuestions(challengeQuestions);
        return new RivalChallengeInit(CHALLENGE, RivalImportance.FAST, challengeProfile.getProfile(), challengeProfile, challengeQuestions);
    }

    public synchronized Question prepareQuestion(ChallengeProfile challengeProfile, int taskIndex, Category category, DifficultyLevel difficultyLevel) {
        List<ChallengeQuestion> challengeQuestions = challengeQuestionRepository.findAllByChallenge_Id(challengeProfile.getChallenge().getId());
        sortChallengeQuestions(challengeQuestions);
        if (challengeQuestions.size() > taskIndex) {
            return challengeQuestions.get(taskIndex).getQuestion();
        }
        Question question = getTaskGenerateService().generate(category, difficultyLevel, findQuestionLanguage(challengeProfile));
        getTaskService().save(question);
        Challenge challenge = challengeProfile.getChallenge();
        ChallengeQuestion challengeQuestion = new ChallengeQuestion(challenge, question);
        challengeQuestionRepository.save(challengeQuestion);
        return question;
    }

    protected Language findQuestionLanguage(ChallengeProfile challengeProfile) {
        return challengeProfile.getProfile().getLanguage();
    }

    private void sortChallengeQuestions(List<ChallengeQuestion> challengeQuestions) {
        challengeQuestions.sort(Comparator.comparing(ChallengeQuestion::getId));
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
