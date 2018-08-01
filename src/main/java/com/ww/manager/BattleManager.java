package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.battle.BattleInitContainer;
import com.ww.model.container.battle.BattleProfileContainer;
import com.ww.model.dto.rival.task.QuestionDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.BattleService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BattleManager {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    private static final Integer MAX_SCORE = 5;

    private final Map<String, BattleProfileContainer> profileMap = new HashMap<>();
    private int questionId = 1;
    private Question question;
    private String winnerTag;
    private BattleService battleService;
    private boolean locked = false;

    public BattleManager(BattleInitContainer bic, BattleService battleService) {
        this.profileMap.put(bic.getCreatorProfileConnection().getSessionId(), new BattleProfileContainer(bic.getCreatorProfile(), bic.getCreatorProfileConnection()));
        this.profileMap.put(bic.getOpponentProfileConnection().getSessionId(), new BattleProfileContainer(bic.getOpponentProfile(), bic.getOpponentProfileConnection()));
        this.battleService = battleService;
    }

    public boolean isLock() {
        return locked;
    }

    public void setLock(boolean locked) {
        this.locked = locked;
    }

    public void maybeStart(String sessionId) {
        profileMap.get(sessionId).setStatus(BattleProfileStatus.READY);
        int readySize = profileMap.values().stream()
                .filter(battleProfileContainer -> battleProfileContainer.getStatus() == BattleProfileStatus.READY)
                .collect(Collectors.toList()).size();
        if (readySize == profileMap.size()) {
            start();
        }
    }

    private ProfileDTO prepareProfile(String sessionId) {
        return new ProfileDTO(profileMap.get(sessionId).getProfile());
    }

    private String getOpponentSessionId(String sessionId) {
        return profileMap.keySet().stream().filter(s -> !s.equals(sessionId)).findFirst().get();
    }

    private QuestionDTO prepareQuestion() {
        Question question = battleService.prepareQuestion();
        question.setId((long) questionId);
        this.question = question;
        return battleService.prepareQuestionDTO(question);
    }

    private synchronized void start() {
        QuestionDTO questionDTO = prepareQuestion();
        profileMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            String opponentSessionId = getOpponentSessionId(battleProfileContainer.getProfileConnection().getSessionId());
            model.put("opponent", prepareProfile(opponentSessionId));
            model.put("question", questionDTO);
            model.put("score", profileMap.get(battleProfileContainer.getProfileConnection().getSessionId()).getScore());
            model.put("opponentScore", profileMap.get(opponentSessionId).getScore());
            send(model, Message.BATTLE_START, battleProfileContainer.getProfileConnection());
        });
    }

    public synchronized void answer(String sessionId, Map<String, Object> content) {
        Answer correctAnswer = question.getAnswers().stream().filter(Answer::getCorrect).findFirst().get();
        Boolean isAnswerCorrect = false;
        Long markedAnswerId = null;
        if (content.containsKey("answerId")) {
            markedAnswerId = ((Integer) content.get("answerId")).longValue();
            isAnswerCorrect = correctAnswer.getId().equals(markedAnswerId);
        }
        String winnerName = null;
        BattleProfileContainer container;
        if (isAnswerCorrect) {
            container = profileMap.get(sessionId);
        } else {
            String opponentSessionId = getOpponentSessionId(sessionId);
            container = profileMap.get(opponentSessionId);
        }
        Integer score = container.increaseScore();
        if (MAX_SCORE.equals(score)) {
            winnerTag = container.getProfile().getTag();
            winnerName = container.getProfile().getName();
        }
        Integer nextQuestionInterval = 5000;
        Long finalMarkedAnswerId = markedAnswerId;
        String finalWinnerName = winnerName;
        profileMap.values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            String mySessionId = battleProfileContainer.getProfileConnection().getSessionId();
            String opponentSessionId = getOpponentSessionId(mySessionId);
            model.put("correctAnswerId", correctAnswer.getId());
            model.put("markedAnswerId", finalMarkedAnswerId);
            model.put("meAnswered", sessionId.equals(mySessionId));
            model.put("score", profileMap.get(mySessionId).getScore());
            model.put("opponentScore", profileMap.get(opponentSessionId).getScore());
            model.put("winner", finalWinnerName);
            model.put("nextQuestionInterval", nextQuestionInterval);
            send(model, Message.BATTLE_ANSWER, battleProfileContainer.getProfileConnection());
        });
        if (winnerTag != null) {
            return;
        }
        questionId++;
        QuestionDTO questionDTO = prepareQuestion();
        Flowable.intervalRange(0L, 1L, nextQuestionInterval, nextQuestionInterval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("correctAnswerId", null);
                    model.put("markedAnswerId", null);
                    model.put("meAnswered", null);
                    model.put("question", questionDTO);
                    model.put("nextQuestionInterval", null);
                    profileMap.values().stream().forEach(battleProfileContainer -> {
                        send(model, Message.BATTLE_NEXT_QUESTION, battleProfileContainer.getProfileConnection());
                    });
                    setLock(false);
                });
    }

    private void send(Map<String, Object> model, Message message, ProfileConnection profileConnection) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            profileConnection.sendMessage(new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }
}
