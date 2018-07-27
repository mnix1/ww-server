package com.ww.service.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.battle.BattleAnswerResult;
import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.task.BattleDTO;
import com.ww.model.dto.task.PractiseDTO;
import com.ww.model.entity.rival.battle.Battle;
import com.ww.model.entity.rival.battle.BattleAnswer;
import com.ww.model.entity.rival.battle.BattleProfile;
import com.ww.model.entity.rival.battle.BattleQuestion;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.repository.rival.battle.BattleAnswerRepository;
import com.ww.repository.rival.battle.BattleProfileRepository;
import com.ww.repository.rival.battle.BattleQuestionRepository;
import com.ww.repository.rival.battle.BattleRepository;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BattleService {
    private static final Logger logger = LoggerFactory.getLogger(BattleService.class);

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private BattleAnswerRepository battleAnswerRepository;

    @Autowired
    private BattleProfileRepository battleProfileRepository;

    @Autowired
    private BattleQuestionRepository battleQuestionRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRendererService taskRendererService;

    @Autowired
    private ProfileService profileService;

    public BattleDTO startFriend(List<String> tags) {
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
        List<Category> categories = IntStream.rangeClosed(1, 3).mapToObj(e -> Category.random()).collect(Collectors.toList());
        List<Question> questions = taskService.generateQuestions(categories);
        taskService.saveProfilesUsedQuestions(profiles, questions);
        Battle battle = create(profile, profiles, questions);
        return new BattleDTO(battle, questions.stream().map(question -> taskRendererService.prepareQuestionDTO(question)).collect(Collectors.toList()));
    }

    public Map endFriend(Long battleId, Map<String, Integer> questionIdAnswerIdMap) throws IllegalArgumentException {
        Profile profile = profileService.getProfile();
        Date closeDate = new Date();
        Battle battle = battleRepository.findById(battleId).orElseThrow(() -> {
            logger.error("Not existing battle: {}", battleId);
            return new IllegalArgumentException();
        });
        if (battle.getStatus() == BattleStatus.CLOSED) {
            logger.error("Battle already closed: {}", battleId);
            throw new IllegalArgumentException();
        }
        BattleProfile battleProfile = battle.getProfiles().stream().
                filter(e -> e.getStatus() == BattleProfileStatus.IN_PROGRESS && e.getProfile().getId().equals(profile.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Battle not for this profile or already answered: {}, {}", battleId, profile.getId());
                    return new IllegalArgumentException();
                });
        if (battleProfile.getStatus() == BattleProfileStatus.CLOSED) {
            logger.error("BattleProfile already closed: {}, {}, {}", battleId, profile.getId(), battleProfile.getId());
            throw new IllegalArgumentException();
        }
        battleProfile.setCloseDate(closeDate);
        battleProfile.setStatus(BattleProfileStatus.CLOSED);
        Map<Long, Long> questionIdCorrectAnswerIdMap = new HashMap<>();
        List<BattleAnswer> battleAnswers = new ArrayList<>();
        List<Question> questions = battle.getQuestions().stream().map(battleQuestion -> battleQuestion.getQuestion()).collect(Collectors.toList());
        questions.forEach(question -> {
            if (!questionIdAnswerIdMap.containsKey(question.getId().toString())) {
                logger.error("questionIdAnswerIdMap doesn't contains answer for: {}, {}}", question.getId(), profile.getId());
                throw new IllegalArgumentException();
            }
            Long answerId = questionIdAnswerIdMap.get(question.getId().toString()).longValue();
            Answer correctAnswer = taskService.findCorrectAnswer(question);
            questionIdCorrectAnswerIdMap.put(question.getId(), correctAnswer.getId());
            BattleAnswer battleAnswer = new BattleAnswer(answerId.equals(correctAnswer.getId()) ? BattleAnswerResult.CORRECT : BattleAnswerResult.WRONG, battle, profile, question);
            battleAnswers.add(battleAnswer);
        });
        battleRepository.save(battle);
        battleProfileRepository.save(battleProfile);
        battleAnswerRepository.saveAll(battleAnswers);
        Map<String, Object> model = new HashMap<>();
        model.put("questionIdCorrectAnswerIdMap", questionIdCorrectAnswerIdMap);
        model.put("answerInterval", battleProfile.inProgressInterval());
        return model;
    }

    private Battle create(Profile creator, List<Profile> profiles, List<Question> questions) {
        Battle battle = new Battle();
        battle.setCreatorProfile(creator);
        battle.setProfiles(profiles.stream()
                .map(profile -> new BattleProfile(battle, profile, profile.getId().equals(creator.getId())
                        ? BattleProfileStatus.IN_PROGRESS
                        : BattleProfileStatus.OPEN))
                .collect(Collectors.toSet()));
        battle.setQuestions(questions.stream().map(question -> new BattleQuestion(battle, question)).collect(Collectors.toSet()));
        battleRepository.save(battle);
        battleProfileRepository.saveAll(battle.getProfiles());
        battleQuestionRepository.saveAll(battle.getQuestions());
        return battle;
    }


}
