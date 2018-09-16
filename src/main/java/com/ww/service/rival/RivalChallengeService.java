package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.dto.rival.challenge.ChallengeInfoDTO;
import com.ww.model.dto.rival.challenge.ChallengePositionDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.model.entity.rival.challenge.Challenge;
import com.ww.model.entity.rival.challenge.ChallengeProfile;
import com.ww.model.entity.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import com.ww.repository.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.rival.challenge.ChallengeQuestionRepository;
import com.ww.repository.rival.challenge.ChallengeRepository;
import com.ww.service.SessionService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
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
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Override
    public Message getMessageContent() {
        return Message.CHALLENGE_CONTENT;
    }

    public Map<String, Object> friendInit(List<String> tags) {
        Map<String, Object> model = new HashMap<>();
        if (tags.isEmpty()) {
            logger.error("Empty tags: {}", sessionService.getProfileId());
            return putErrorCode(model);
        }
        Set<String> tagSet = new HashSet<>(tags);
        Profile profile = profileService.getProfile();
        List<Profile> friends = profile.getFriends().stream()
                .filter(profileFriend -> profileFriend.getStatus() == FriendStatus.ACCEPTED)
                .map(ProfileFriend::getFriendProfile)
                .filter(e -> tagSet.contains(e.getTag()))
                .collect(Collectors.toList());
        if (friends.isEmpty()) {
            logger.error("Empty friends: {}", sessionService.getProfileId());
            return putErrorCode(model); //error no one to fight
        }
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        profiles.addAll(friends);
        create(profile, profiles);
        return putSuccessCode(model);
    }

    public Map<String, Object> response(Long challengeId) {
        Map<String, Object> model = new HashMap<>();
        ChallengeProfile challengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(sessionService.getProfileId(), challengeId);
        if (challengeProfile == null || challengeProfile.getStatus() != ChallengeProfileStatus.OPEN) {
            return putErrorCode(model);
        }
        Profile profile = challengeProfile.getProfile();
        challengeProfile.setStatus(ChallengeProfileStatus.IN_PROGRESS);
        challengeProfileRepository.save(challengeProfile);
        List<ChallengeQuestion> challengeQuestions = new ArrayList<>(challengeProfile.getChallenge().getQuestions());
        sortChallengeQuestions(challengeQuestions);
        RivalInitContainer rival = new RivalInitContainer(CHALLENGE, RivalImportance.FAST, profile, null);
        RivalManager rivalManager = new ChallengeManager(rival, this, profileConnectionService, challengeProfile, challengeQuestions);
        getGlobalRivalService().put(rival.getCreatorProfile().getId(), rivalManager);
        rivalManager.start();
        return putSuccessCode(model);
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
        challengeProfile.setScore(Math.max(0, rivalManager.getRivalContainer().getCurrentTaskIndex()));
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

    private void create(Profile creator, List<Profile> profiles) {
        Challenge challenge = new Challenge();
        challenge.setCreatorProfile(creator);
        challenge.setProfiles(profiles.stream()
                .map(profile -> new ChallengeProfile(challenge, profile, ChallengeProfileStatus.OPEN))
                .collect(Collectors.toSet()));
        challengeRepository.save(challenge);
        challengeProfileRepository.saveAll(challenge.getProfiles());
    }

    public List<ChallengeInfoDTO> list(ChallengeStatus status) {
        if (status == ChallengeStatus.CLOSED) {
            List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndStatusAndChallenge_Status(sessionService.getProfileId(), ChallengeProfileStatus.CLOSED, ChallengeStatus.CLOSED);
            return challengeProfiles.stream()
                    .map(challengeProfile -> new ChallengeInfoDTO(challengeProfile.getChallenge()))
                    .distinct()
                    .collect(Collectors.toList());
        }
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_Status(sessionService.getProfileId(), ChallengeStatus.IN_PROGRESS);
        return challengeProfiles.stream()
                .map(challengeProfile -> new ChallengeInfoDTO(challengeProfile.getChallenge(), challengeProfile.getStatus() != ChallengeProfileStatus.CLOSED))
                .distinct()
                .collect(Collectors.toList());
    }

    public ChallengeSummaryDTO summary(Long challengeId) {
        return summary(challengeProfileRepository.findByProfile_IdAndChallenge_Id(sessionService.getProfileId(), challengeId));
    }

    public ChallengeSummaryDTO summary(ChallengeProfile challengeProfile) {
        if (challengeProfile == null) {
            return null;
        }
        Challenge challenge = challengeProfile.getChallenge();
        List<ChallengePositionDTO> positions = new ArrayList<>();
        Set<ChallengeProfile> challengeProfiles = challenge.getProfiles();
        for (ChallengeProfile cp : challengeProfiles) {
            ChallengePositionDTO position = new ChallengePositionDTO(cp);
            positions.add(position);
        }
        positions.sort((o1, o2) -> {
            if (o1.getStatus() != ChallengeProfileStatus.CLOSED) {
                if (o2.getStatus() != ChallengeProfileStatus.CLOSED) {
                    return 0;
                }
                return 1;
            }
            if (o2.getStatus() != ChallengeProfileStatus.CLOSED) {
                return -1;
            }
            if (o1.getScore().equals(o2.getScore())) {
                return 0;
            }
            return o2.getScore().compareTo(o1.getScore());
        });
        return new ChallengeSummaryDTO(challenge.getId(), positions);
    }

}
