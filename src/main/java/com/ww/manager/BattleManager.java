package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.constant.rival.battle.BattleStatus;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BattleManager {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    private static final Integer MAX_SCORE = 5;
    private static final Integer NEXT_TASK_INTERVAL = 7000;

    private final Map<Long, BattleProfileContainer> profileIdBattleProfileContainerMap = new HashMap<>();
    private int questionId = 1;
    private TaskDTO previousTaskDTO;
    private Question question;
    private TaskDTO taskDTO;
    private String winnerTag;
    private String winnerName;
    private Instant nextTaskDate;
    private Answer correctAnswer;
    private Long markedAnswerId;
    private Long answeredProfileId;

    private BattleStatus status = BattleStatus.OPEN;

    private BattleService battleService;
    private ProfileConnectionService profileConnectionService;


    public BattleManager(BattleFriendContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.profileIdBattleProfileContainerMap.put(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.profileIdBattleProfileContainerMap.put(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
        this.battleService = battleService;
        this.profileConnectionService = profileConnectionService;
    }

    public boolean isLock() {
        return status != BattleStatus.ANSWERING;
    }

    public boolean isClosed() {
        return status == BattleStatus.CLOSED;
    }

    public String getWinnerTag() {
        return winnerTag;
    }

    public List<BattleProfileContainer> getBattleProfileContainers() {
        return new ArrayList<>(profileIdBattleProfileContainerMap.values());
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

    private void prepareNewTask() {
        this.previousTaskDTO = this.taskDTO;
        Question question = battleService.prepareQuestion();
        question.setId((long) questionId);
        this.question = question;
        this.taskDTO = battleService.prepareTaskDTO(question);
    }

    public void sendReadyFast() {
        profileIdBattleProfileContainerMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, Message.BATTLE_READY_FAST, battleProfileContainer.getProfileId());
        });
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        BattleProfileContainer battleProfileContainer = profileIdBattleProfileContainerMap.get(profileId);
        fillModelAnswering(model, battleProfileContainer);
        if (status == BattleStatus.PREPARING_NEXT_TASK) {
            model.remove("question");
            model.put("question", previousTaskDTO);
        }
        if (status != BattleStatus.ANSWERING) {
            fillModelAnswered(model, battleProfileContainer);
        }
        return model;
    }

    private synchronized void start() {
        prepareNewTask();
        status = BattleStatus.ANSWERING;
        profileIdBattleProfileContainerMap.values().parallelStream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            fillModelAnswering(model, battleProfileContainer);
            send(model, Message.BATTLE_START, battleProfileContainer.getProfileId());
        });
    }

    private void fillModelAnswering(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("opponent", prepareProfile(battleProfileContainer.getOpponentId()));
        model.put("question", taskDTO);
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
    }

    private void fillModelAnswered(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("correctAnswerId", correctAnswer.getId());
        model.put("markedAnswerId", markedAnswerId);
        model.put("meAnswered", answeredProfileId.equals(battleProfileContainer.getProfileId()));
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
        model.put("winner", winnerName);
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        status = BattleStatus.ANSWERED;
        correctAnswer = question.getAnswers().stream().filter(Answer::getCorrect).findFirst().get();
        Boolean isAnswerCorrect = false;
        answeredProfileId = profileId;
        markedAnswerId = null;
        if (content.containsKey("answerId")) {
            markedAnswerId = ((Integer) content.get("answerId")).longValue();
            isAnswerCorrect = correctAnswer.getId().equals(markedAnswerId);
        }
        BattleProfileContainer container = profileIdBattleProfileContainerMap.get(answeredProfileId);
        if (!isAnswerCorrect) {
            container = profileIdBattleProfileContainerMap.get(container.getOpponentId());
        }
        Integer score = container.increaseScore();
        if (MAX_SCORE.equals(score)) {
            winnerTag = container.getProfile().getTag();
            winnerName = container.getProfile().getName();
        }
        nextTaskDate = Instant.now().plus(NEXT_TASK_INTERVAL, ChronoUnit.MILLIS);
        profileIdBattleProfileContainerMap.values().parallelStream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            fillModelAnswered(model, battleProfileContainer);
            send(model, Message.BATTLE_ANSWER, battleProfileContainer.getProfileId());
        });
        if (winnerTag != null) {
            status = BattleStatus.CLOSED;
            battleService.disposeManager(this);
            return;
        }
        status = BattleStatus.PREPARING_NEXT_TASK;
        questionId++;
        prepareNewTask();
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    status = BattleStatus.ANSWERING;
                    Map<String, Object> model = new HashMap<>();
                    model.put("correctAnswerId", null);
                    model.put("markedAnswerId", null);
                    model.put("meAnswered", null);
                    model.put("question", taskDTO);
                    model.put("nextTaskInterval", null);
                    profileIdBattleProfileContainerMap.values().parallelStream().forEach(battleProfileContainer -> {
                        send(model, Message.BATTLE_NEXT_QUESTION, battleProfileContainer.getProfileId());
                    });
                });
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            profileConnectionService.sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }
}
