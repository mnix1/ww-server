package com.ww.service.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.challenge.ChallengeAnswerResult;
import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.rival.challenge.ChallengeInfoDTO;
import com.ww.model.dto.rival.challenge.ChallengePositionDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.model.dto.rival.challenge.ChallengeTaskDTO;
import com.ww.model.dto.rival.task.*;
import com.ww.model.entity.rival.challenge.Challenge;
import com.ww.model.entity.rival.challenge.ChallengeAnswer;
import com.ww.model.entity.rival.challenge.ChallengeProfile;
import com.ww.model.entity.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.repository.rival.challenge.ChallengeAnswerRepository;
import com.ww.repository.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.rival.challenge.ChallengeQuestionRepository;
import com.ww.repository.rival.challenge.ChallengeRepository;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ChallengeService {
    private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ChallengeQuestionRepository challengeQuestionRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRendererService taskRendererService;

    @Autowired
    private ProfileService profileService;

    public final static int QUESTION_COUNT = 5;

    public ChallengeTaskDTO startFast() {
        Profile profile = profileService.getProfile();
        Profile opponentProfile = profileService.getActiveProfile();
        if(opponentProfile == null){
            logger.error("Cant find active profile: {}", sessionService.getProfileId());
            throw new IllegalArgumentException();
        }
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        profiles.add(opponentProfile);
        List<Category> categories = IntStream.rangeClosed(1, QUESTION_COUNT).mapToObj(e -> Category.random()).collect(Collectors.toList());
        List<Question> questions = taskService.generateQuestions(categories);
        Challenge challenge = create(profile, profiles, questions);
        return new ChallengeTaskDTO(challenge, questions.stream().map(question -> taskRendererService.prepareQuestionDTO(question)).collect(Collectors.toList()));
    }

    public ChallengeTaskDTO startFriend(List<String> tags) {
        if (tags.isEmpty()) {
            logger.error("Empty tags: {}", sessionService.getProfileId());
            return null;
        }
        Set<String> tagSet = new HashSet<>(tags);
        Profile profile = profileService.getProfile();
        List<Profile> friends = profile.getFriends().stream()
                .filter(profileFriend -> profileFriend.getStatus() == FriendStatus.ACCEPTED)
                .map(profileFriend -> profileFriend.getFriendProfile())
                .filter(e -> tagSet.contains(e.getTag()))
                .collect(Collectors.toList());
        if (friends.isEmpty()) {
            logger.error("Empty friends: {}", sessionService.getProfileId());
            return null; //error no one to fight
        }
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        profiles.addAll(friends);
        List<Category> categories = IntStream.rangeClosed(1, QUESTION_COUNT).mapToObj(e -> Category.random()).collect(Collectors.toList());
        List<Question> questions = taskService.generateQuestions(categories);
        Challenge challenge = create(profile, profiles, questions);
        return new ChallengeTaskDTO(challenge, questions.stream().map(question -> taskRendererService.prepareQuestionDTO(question)).collect(Collectors.toList()));
    }

    public ChallengeTaskDTO startResponse(Long challengeId) {
        Profile profile = profileService.getProfile();
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> {
            logger.error("Not existing challenge: {}", challengeId);
            return new IllegalArgumentException();
        });
        if (challenge.getStatus() == ChallengeStatus.CLOSED) {
            logger.error("Challenge already closed: {}", challengeId);
            throw new IllegalArgumentException();
        }
        ChallengeProfile challengeProfile = challenge.getProfiles().stream().
                filter(e -> e.getProfile().getId().equals(profile.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Challenge not for this profile: {}, {}", challengeId, profile.getId());
                    return new IllegalArgumentException();
                });
        if (challengeProfile.getStatus() == ChallengeProfileStatus.CLOSED) {
            logger.error("Already response for this challenge: {}, {}", challengeId, profile.getId());
            throw new IllegalArgumentException();
        }
        List<QuestionDTO> questions = challenge.getQuestions().stream()
                .map(challengeQuestion -> taskRendererService.prepareQuestionDTO(challengeQuestion.getQuestion()))
                .collect(Collectors.toList());
        if (challengeProfile.getStatus() == ChallengeProfileStatus.OPEN) {
            challengeProfile.setStatus(ChallengeProfileStatus.IN_PROGRESS);
            challengeProfile.setInProgressDate(Instant.now());
            challengeProfileRepository.save(challengeProfile);
        }
        return new ChallengeTaskDTO(challenge, questions);
    }

    public Map end(Long challengeId, Map<String, Integer> questionIdAnswerIdMap) throws IllegalArgumentException {
        Instant closeDate = Instant.now();
        Profile profile = profileService.getProfile();
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> {
            logger.error("Not existing challenge: {}", challengeId);
            return new IllegalArgumentException();
        });
        if (challenge.getStatus() == ChallengeStatus.CLOSED) {
            logger.error("Challenge already closed: {}", challengeId);
            throw new IllegalArgumentException();
        }
        ChallengeProfile challengeProfile = challenge.getProfiles().stream().
                filter(e -> e.getStatus() == ChallengeProfileStatus.IN_PROGRESS && e.getProfile().getId().equals(profile.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Challenge not for this profile or response already closed or not opened: {}, {}", challengeId, profile.getId());
                    return new IllegalArgumentException();
                });
        challengeProfile.setCloseDate(closeDate);
        challengeProfile.setStatus(ChallengeProfileStatus.CLOSED);
        Map<Long, Long> questionIdCorrectAnswerIdMap = new HashMap<>();
        List<ChallengeAnswer> challengeAnswers = new ArrayList<>();
        List<Question> questions = challenge.getQuestions().stream().map(challengeQuestion -> challengeQuestion.getQuestion()).collect(Collectors.toList());
        questions.forEach(question -> {
            if (!questionIdAnswerIdMap.containsKey(question.getId().toString())) {
                logger.error("questionIdAnswerIdMap doesn't contains answer for: {}, {}}", question.getId(), profile.getId());
                throw new IllegalArgumentException();
            }
            Long answerId = questionIdAnswerIdMap.get(question.getId().toString()).longValue();
            Answer correctAnswer = taskService.findCorrectAnswer(question);
            questionIdCorrectAnswerIdMap.put(question.getId(), correctAnswer.getId());
            ChallengeAnswer challengeAnswer = new ChallengeAnswer(answerId.equals(correctAnswer.getId()) ? ChallengeAnswerResult.CORRECT : ChallengeAnswerResult.WRONG, challenge, profile, question);
            challengeAnswers.add(challengeAnswer);
        });
        challengeProfileRepository.save(challengeProfile);
        challengeAnswerRepository.saveAll(challengeAnswers);
        Map<String, Object> model = new HashMap<>();
        model.put("questionIdCorrectAnswerIdMap", questionIdCorrectAnswerIdMap);
        model.put("answerInterval", challengeProfile.inProgressInterval());
        maybeCloseChallenge(challenge, closeDate);
        return model;
    }

    private void maybeCloseChallenge(Challenge challenge, Instant closeDate) {
        if (challenge.getProfiles().stream().filter(challengeProfile -> challengeProfile.getStatus() != ChallengeProfileStatus.CLOSED).findFirst().isPresent()) {
            return;
        }
        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setCloseDate(closeDate);
        // TODO ADD AUTO CLOSE WHEN TIMEOUT
        challengeRepository.save(challenge);
    }

    private Challenge create(Profile creator, List<Profile> profiles, List<Question> questions) {
        Challenge challenge = new Challenge();
        challenge.setCreatorProfile(creator);
        challenge.setProfiles(profiles.stream()
                .map(profile -> new ChallengeProfile(challenge, profile, profile.getId().equals(creator.getId())
                        ? ChallengeProfileStatus.IN_PROGRESS
                        : ChallengeProfileStatus.OPEN))
                .collect(Collectors.toSet()));
        challenge.setQuestions(questions.stream().map(question -> new ChallengeQuestion(challenge, question)).collect(Collectors.toSet()));
        challengeRepository.save(challenge);
        challengeProfileRepository.saveAll(challenge.getProfiles());
        challengeQuestionRepository.saveAll(challenge.getQuestions());
        return challenge;
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
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> {
            logger.error("Not existing challenge: {}", challengeId);
            return new IllegalArgumentException();
        });
        List<ChallengePositionDTO> positions = new ArrayList<>();
        Set<ChallengeAnswer> challengeAnswers = challenge.getAnswers();
        Set<ChallengeProfile> challengeProfiles = challenge.getProfiles();
        for (ChallengeProfile challengeProfile : challengeProfiles) {
            ChallengePositionDTO position = new ChallengePositionDTO(challengeProfile);
            challengeAnswers.stream().filter(challengeAnswer -> challengeAnswer.getProfile().getId().equals(challengeProfile.getProfile().getId()))
                    .forEach(challengeAnswer -> {
                        if (challengeAnswer.getResult() == ChallengeAnswerResult.CORRECT) {
                            position.increaseScore();
                        }
                    });
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
                return o1.getAnswerInterval().compareTo(o2.getAnswerInterval());
            }
            return o1.getScore().compareTo(o2.getScore());
        });

        return new ChallengeSummaryDTO(challengeId, positions);
    }

}
