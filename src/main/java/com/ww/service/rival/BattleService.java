package com.ww.service.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.task.BattleDTO;
import com.ww.model.dto.task.PractiseDTO;
import com.ww.model.entity.rival.battle.Battle;
import com.ww.model.entity.rival.battle.BattleProfile;
import com.ww.model.entity.rival.battle.BattleQuestion;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private Battle create(Profile creator, List<Profile> profiles, List<Question> questions) {
        Battle battle = new Battle();
        battle.setCreatorProfile(creator);
        battle.setProfiles(profiles.stream().map(profile -> new BattleProfile(battle, profile)).collect(Collectors.toSet()));
        battle.setQuestions(questions.stream().map(question -> new BattleQuestion(battle, question)).collect(Collectors.toSet()));
        battleRepository.save(battle);
        battleProfileRepository.saveAll(battle.getProfiles());
        battleQuestionRepository.saveAll(battle.getQuestions());
        return battle;
    }


}
