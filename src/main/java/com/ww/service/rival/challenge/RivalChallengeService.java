package com.ww.service.rival.challenge;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.container.rival.init.RivalChallengeInitContainer;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeQuestionRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.rival.init.RivalRunService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.websocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ww.model.constant.rival.RivalType.CHALLENGE;

@Service
public class RivalChallengeService extends RivalWarService {
    private static final Logger logger = LoggerFactory.getLogger(RivalChallengeService.class);

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ChallengeQuestionRepository challengeQuestionRepository;

    @Autowired
    protected RivalRunService rivalRunService;

    public void init(ChallengeProfile challengeProfile) {
        List<ChallengeQuestion> challengeQuestions = new ArrayList<>(challengeProfile.getChallenge().getQuestions());
        sortChallengeQuestions(challengeQuestions);
        RivalChallengeInitContainer rival = new RivalChallengeInitContainer(CHALLENGE, RivalImportance.FAST, challengeProfile.getProfile(), challengeProfile, challengeQuestions);
        rivalRunService.run(rival);
    }

    @Override
    public Message getMessageContent() {
        return Message.CHALLENGE_CONTENT;
    }

    public synchronized Question prepareQuestion(ChallengeProfile challengeProfile, int taskIndex, Category category, DifficultyLevel difficultyLevel) {
        List<ChallengeQuestion> challengeQuestions = challengeQuestionRepository.findAllByChallenge_Id(challengeProfile.getChallenge().getId());
        sortChallengeQuestions(challengeQuestions);
        if (challengeQuestions.size() > taskIndex) {
            return challengeQuestions.get(taskIndex).getQuestion();
        }
        Question question = getTaskGenerateService().generate(category, difficultyLevel);
        taskService.save(question);
        Challenge challenge = challengeProfile.getChallenge();
        ChallengeQuestion challengeQuestion = new ChallengeQuestion(challenge, question);
        challengeQuestionRepository.save(challengeQuestion);
        return question;
    }

    private void sortChallengeQuestions(List<ChallengeQuestion> challengeQuestions) {
        challengeQuestions.sort(Comparator.comparing(ChallengeQuestion::getId));
    }

    @Override
    public synchronized void disposeManager(RivalManager rivalManager) {
        super.disposeManager(rivalManager);
        ChallengeManager challengeManager = (ChallengeManager) rivalManager;
        ChallengeProfile challengeProfile = challengeManager.challengeProfile;
        challengeProfile.setStatus(ChallengeProfileStatus.CLOSED);
        challengeProfile.setScore(Math.max(0, rivalManager.getContainer().getCurrentTaskIndex()));
        challengeProfileRepository.save(challengeProfile);
        maybeCloseChallenge(challengeProfile.getChallenge(), Instant.now());
    }

    @Override
    protected void addRewardFromWin(Profile winner) {
    }

    private void maybeCloseChallenge(Challenge challenge, Instant closeDate) {
        if (challengeProfileRepository.findAllByChallenge_Id(challenge.getId()).stream().anyMatch(challengeProfile -> challengeProfile.getStatus() != ChallengeProfileStatus.CLOSED)) {
            return;
        }
        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setCloseDate(closeDate);
        // TODO ADD AUTO CLOSE WHEN TIMEOUT
        challengeRepository.save(challenge);
    }

}
