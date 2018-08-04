package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleFriendContainer;
import com.ww.model.container.battle.BattleProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.service.rival.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BattleManager {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    private static final Integer MAX_SCORE = 5;
    private static final Integer NEXT_TASK_INTERVAL = 7000;

    private final Map<Long, BattleProfileContainer> profileIdBattleProfileContainerMap = new HashMap<>();
    private int questionId = 1;
    private Question question;
    private String winnerTag;
    private BattleService battleService;
    private ProfileConnectionService profileConnectionService;
    private boolean locked = false;

    public BattleManager(BattleFriendContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.profileIdBattleProfileContainerMap.put(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.profileIdBattleProfileContainerMap.put(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
        this.battleService = battleService;
        this.profileConnectionService = profileConnectionService;
    }

    public boolean isLock() {
        return locked;
    }

    public void setLock(boolean locked) {
        this.locked = locked;
    }

    public void maybeStart(Long profileId) {
        profileIdBattleProfileContainerMap.get(profileId).setStatus(BattleProfileStatus.READY);
        int readySize = profileIdBattleProfileContainerMap.values().stream()
                .filter(battleProfileContainer -> battleProfileContainer.getStatus() == BattleProfileStatus.READY)
                .collect(Collectors.toList()).size();
        if (readySize == profileIdBattleProfileContainerMap.size()) {
            start();
        }
    }

    private ProfileDTO prepareProfile(Long profileId) {
        return new ProfileDTO(profileIdBattleProfileContainerMap.get(profileId).getProfile());
    }

    private TaskDTO prepareQuestion() {
        Question question = battleService.prepareQuestion();
        question.setId((long) questionId);
        this.question = question;
        return battleService.prepareQuestionDTO(question);
    }

    public void startFast() {
        profileIdBattleProfileContainerMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, Message.BATTLE_START_FAST, battleProfileContainer.getProfileId());
        });
    }

    private synchronized void start() {
        TaskDTO taskDTO = prepareQuestion();
        profileIdBattleProfileContainerMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            model.put("opponent", prepareProfile(battleProfileContainer.getOpponentId()));
            model.put("question", taskDTO);
            model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
            model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
            send(model, Message.BATTLE_START, battleProfileContainer.getProfileId());
        });
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        Answer correctAnswer = question.getAnswers().stream().filter(Answer::getCorrect).findFirst().get();
        Boolean isAnswerCorrect = false;
        Long markedAnswerId = null;
        if (content.containsKey("answerId")) {
            markedAnswerId = ((Integer) content.get("answerId")).longValue();
            isAnswerCorrect = correctAnswer.getId().equals(markedAnswerId);
        }
        String winnerName = null;
        BattleProfileContainer container = profileIdBattleProfileContainerMap.get(profileId);
        if (!isAnswerCorrect) {
            container = profileIdBattleProfileContainerMap.get(container.getOpponentId());
        }
        Integer score = container.increaseScore();
        if (MAX_SCORE.equals(score)) {
            winnerTag = container.getProfile().getTag();
            winnerName = container.getProfile().getName();
        }
        Long finalMarkedAnswerId = markedAnswerId;
        String finalWinnerName = winnerName;
        profileIdBattleProfileContainerMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            model.put("correctAnswerId", correctAnswer.getId());
            model.put("markedAnswerId", finalMarkedAnswerId);
            model.put("meAnswered", profileId.equals(battleProfileContainer.getProfileId()));
            model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
            model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
            model.put("winner", finalWinnerName);
            model.put("nextTaskInterval", NEXT_TASK_INTERVAL);
            send(model, Message.BATTLE_ANSWER, battleProfileContainer.getProfileId());
        });
        if (winnerTag != null) {
            return;
        }
        questionId++;
        TaskDTO taskDTO = prepareQuestion();
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("correctAnswerId", null);
                    model.put("markedAnswerId", null);
                    model.put("meAnswered", null);
                    model.put("question", taskDTO);
                    model.put("nextTaskInterval", null);
                    profileIdBattleProfileContainerMap.values().stream().forEach(battleProfileContainer -> {
                        send(model, Message.BATTLE_NEXT_QUESTION, battleProfileContainer.getProfileId());
                    });
                    setLock(false);
                });
    }

    private void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            profileConnectionService.sendMessage(profileId,new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }
}
