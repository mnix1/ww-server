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

    public final static int TASK_COUNT = 5;

    public ChallengeTaskDTO startFast() {
        Profile profile = profileService.getProfile();
        Profile opponentProfile = profileService.getActiveProfile();
        if (opponentProfile == null) {
            logger.error("Cant find active profile: {}", sessionService.getProfileId());
            throw new IllegalArgumentException();
        }
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        profiles.add(opponentProfile);
        List<Category> categories = IntStream.rangeClosed(1, TASK_COUNT).mapToObj(e -> Category.random()).collect(Collectors.toList());
        List<Question> questions = taskService.generateQuestions(categories);
        Integer taskIndex = 0;
        Question question = questions.get(taskIndex);
        Challenge challenge = create(profile, profiles, questions, question);
        return new ChallengeTaskDTO(challenge, taskRendererService.prepareTaskDTO(question), taskIndex, 0L, TASK_COUNT);
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
        List<Category> categories = IntStream.rangeClosed(1, TASK_COUNT).mapToObj(e -> Category.random()).collect(Collectors.toList());
        List<Question> questions = taskService.generateQuestions(categories);
        Integer taskIndex = 0;
        Question question = questions.get(taskIndex);
        Challenge challenge = create(profile, profiles, questions, question);
        return new ChallengeTaskDTO(challenge, taskRendererService.prepareTaskDTO(question), taskIndex, 0L, TASK_COUNT);
    }

    public ChallengeTaskDTO startResponse(Long challengeId) {
        Profile profile = profileService.getProfile();
        Challenge challenge = getChallenge(challengeId);
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
        List<Question> questions = getQuestions(challenge);
        List<ChallengeAnswer> challengeAnswers = getChallengeAnswers(challenge);
        Integer taskIndex = findTaskIndex(challengeAnswers);
        Question question = questions.get(taskIndex);
        long score = 0L;
        if (challengeProfile.getStatus() == ChallengeProfileStatus.OPEN) {
            challengeProfile.setStatus(ChallengeProfileStatus.IN_PROGRESS);
            challengeProfile.setInProgressDate(Instant.now());
            challengeProfileRepository.save(challengeProfile);
            challengeAnswerRepository.save(new ChallengeAnswer(challenge, profile, question, taskIndex));
        } else {
            score = getScore(challengeAnswers);
            if (challengeAnswers.stream().noneMatch(challengeAnswer -> challengeAnswer.getResult() == ChallengeAnswerResult.IN_PROGRESS)) {
                challengeAnswerRepository.save(new ChallengeAnswer(challenge, profile, question, taskIndex));
            }
        }
        return new ChallengeTaskDTO(challenge, taskRendererService.prepareTaskDTO(question), taskIndex, score, TASK_COUNT);
    }

    private List<Question> getQuestions(Challenge challenge) {
        return challenge.getQuestions().stream()
                .map(ChallengeQuestion::getQuestion)
                .sorted(Comparator.comparing(Question::getId))
                .collect(Collectors.toList());
    }

    private ChallengeProfile getChallengeProfile(Challenge challenge) {
        return challenge.getProfiles().stream()
                .filter(e -> e.getStatus() == ChallengeProfileStatus.IN_PROGRESS && e.getProfile().getId().equals(sessionService.getProfileId()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Challenge not for this profile or response already closed or not opened: {}, {}", challenge.getId(), sessionService.getProfileId());
                    return new IllegalArgumentException();
                });
    }

    private Integer findTaskIndex(List<ChallengeAnswer> challengeAnswers) {
        Integer taskIndex = 0;
        for (ChallengeAnswer challengeAnswer : challengeAnswers) {
            if (challengeAnswer.getResult() == ChallengeAnswerResult.IN_PROGRESS) {
                taskIndex = challengeAnswer.getTaskIndex();
                break;
            }
            taskIndex = Math.max(challengeAnswer.getTaskIndex() + 1, taskIndex);
        }
        return taskIndex;
    }

    private Challenge getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> {
            logger.error("Not existing challenge: {}", challengeId);
            return new IllegalArgumentException();
        });
        if (challenge.getStatus() == ChallengeStatus.CLOSED) {
            logger.error("Challenge already closed: {}", challengeId);
            throw new IllegalArgumentException();
        }
        return challenge;
    }

    private List<ChallengeAnswer> getChallengeAnswers(Challenge challenge) {
        return challenge.getAnswers().stream()
                .filter(e -> e.getProfile().getId().equals(sessionService.getProfileId()))
                .collect(Collectors.toList());
    }

    private Long calculateChallengeInteval(List<ChallengeAnswer> challengeAnswers) {
        long challengeInterval = 0;
        for (ChallengeAnswer challengeAnswer : challengeAnswers) {
            challengeInterval += challengeAnswer.inProgressInterval();
        }
        return challengeInterval;
    }

    private long getScore(List<ChallengeAnswer> challengeAnswers) {
        return challengeAnswers.stream().filter(e -> e.getResult() == ChallengeAnswerResult.CORRECT).count();
    }

    public ChallengeTaskDTO nextTask(Long challengeId) {
        Challenge challenge = getChallenge(challengeId);
        ChallengeProfile challengeProfile = getChallengeProfile(challenge);
        List<ChallengeAnswer> challengeAnswers = getChallengeAnswers(challenge);
        Integer taskIndex = findTaskIndex(challengeAnswers);
        Question question = getQuestions(challenge).get(taskIndex);
        challengeAnswerRepository.save(new ChallengeAnswer(challenge, challengeProfile.getProfile(), question, taskIndex));
        return new ChallengeTaskDTO(challenge, taskRendererService.prepareTaskDTO(question), taskIndex, getScore(challengeAnswers), TASK_COUNT);
    }

    public Map endTask(Long challengeId, Long answerId) {
        Instant closeDate = Instant.now();
        Profile profile = profileService.getProfile();
        Challenge challenge = getChallenge(challengeId);
        ChallengeProfile challengeProfile = getChallengeProfile(challenge);
        ChallengeAnswer challengeAnswer = challenge.getAnswers().stream()
                .filter(e -> e.getProfile().getId().equals(sessionService.getProfileId()) && e.getResult() == ChallengeAnswerResult.IN_PROGRESS)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Challenge stateAnswered not in progress: {}, {}", challengeId, profile.getId());
                    return new IllegalArgumentException();
                });
        Question question = challengeAnswer.getQuestion();
        Answer correctAnswer = taskService.findCorrectAnswer(question);
        boolean result = correctAnswer.getId().equals(answerId);
        challengeAnswer.setCloseDate(closeDate);
        challengeAnswer.setResult(result ? ChallengeAnswerResult.CORRECT : ChallengeAnswerResult.WRONG);
        challengeAnswerRepository.save(challengeAnswer);
        List<ChallengeAnswer> challengeAnswers = getChallengeAnswers(challenge);
        long score = getScore(challengeAnswers);
        Long challengeInterval = null;
        Boolean isAllTasksAnswered = challengeAnswers.size() == TASK_COUNT;
        if (isAllTasksAnswered) {
            challengeProfile.setCloseDate(closeDate);
            challengeProfile.setStatus(ChallengeProfileStatus.CLOSED);
            challengeProfileRepository.save(challengeProfile);
            challengeInterval = calculateChallengeInteval(challengeAnswers);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("correctAnswerId", correctAnswer.getId());
        model.put("challengeInterval", challengeInterval);
        model.put("answerInterval", challengeAnswer.inProgressInterval());
        model.put("isAllTasksAnswered", isAllTasksAnswered);
        model.put("score", score);
        maybeCloseChallenge(challenge, closeDate);
        return model;
    }

    private void maybeCloseChallenge(Challenge challenge, Instant closeDate) {
        if (challenge.getProfiles().stream().anyMatch(challengeProfile -> challengeProfile.getStatus() != ChallengeProfileStatus.CLOSED)) {
            return;
        }
        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setCloseDate(closeDate);
        // TODO ADD AUTO CLOSE WHEN TIMEOUT
        challengeRepository.save(challenge);
    }

    private Challenge create(Profile creator, List<Profile> profiles, List<Question> questions, Question question) {
        Challenge challenge = new Challenge();
        challenge.setCreatorProfile(creator);
        challenge.setProfiles(profiles.stream()
                .map(profile -> new ChallengeProfile(challenge, profile, profile.getId().equals(creator.getId())
                        ? ChallengeProfileStatus.IN_PROGRESS
                        : ChallengeProfileStatus.OPEN))
                .collect(Collectors.toSet()));
        challenge.setQuestions(questions.stream().map(q -> new ChallengeQuestion(challenge, q)).collect(Collectors.toSet()));
        challengeRepository.save(challenge);
        challengeProfileRepository.saveAll(challenge.getProfiles());
        challengeQuestionRepository.saveAll(challenge.getQuestions());
        challengeAnswerRepository.save(new ChallengeAnswer(challenge, creator, question, 0));
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
            List<ChallengeAnswer> profileChallengeAnswers = challengeAnswers.stream().filter(challengeAnswer -> challengeAnswer.getProfile().getId().equals(challengeProfile.getProfile().getId())).collect(Collectors.toList());
            if (challengeProfile.getStatus() == ChallengeProfileStatus.CLOSED) {
                position.setAnswerInterval(calculateChallengeInteval(profileChallengeAnswers));
            }
            profileChallengeAnswers.forEach(challengeAnswer -> {
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
            return o2.getScore().compareTo(o1.getScore());
        });
        return new ChallengeSummaryDTO(challengeId, positions);
    }

}
