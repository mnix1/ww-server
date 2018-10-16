package com.ww.service.rival.challenge;

import com.ww.helper.RandomHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ChallengePhaseWisie;
import com.ww.model.entity.outside.wisie.Wisie;
import com.ww.repository.outside.rival.challenge.ChallengePhaseRepository;
import com.ww.repository.outside.rival.challenge.ChallengePhaseWisieRepository;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.wisie.WisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.model.constant.rival.RivalType.CHALLENGE;

@Service
public class RivalChallengeService extends RivalWarService {
    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;
    @Autowired
    private ChallengePhaseRepository challengePhaseRepository;
    @Autowired
    private ChallengeCloseService challengeCloseService;
    @Autowired
    private ChallengePhaseWisieRepository challengePhaseWisieRepository;
    @Autowired
    private WisieService wisieService;

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
        ChallengePhase challengePhase = new ChallengePhase(challenge, getTaskGenerateService().findProperTaskType(category, difficultyLevel), difficultyLevel, Language.NO_COMMON, preparePhaseWisie(taskIndex));
        challengePhaseRepository.save(challengePhase);
        return challengePhase;
    }

    public ChallengePhaseWisie preparePhaseWisie(int taskIndex) {
        Wisie wisie = wisieService.getWisie(WisieType.random());
        ChallengePhaseWisie phaseWisie = new ChallengePhaseWisie(true, new HashSet<>(Category.random(Math.max(1, Math.min(3, taskIndex / 4)))), new HashSet<>(), wisie);
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
