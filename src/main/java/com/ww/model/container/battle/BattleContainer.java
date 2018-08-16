package com.ww.model.container.battle;

import com.ww.manager.BattleManager;
import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public class BattleContainer {
    private final Map<Long, BattleProfileContainer> profileIdBattleProfileContainerMap = new HashMap<>();
    private int taskCount = BattleManager.TASK_COUNT;
    private int currentTaskIndex = 0;

    private List<Question> questions = new ArrayList<>(taskCount);
    private List<TaskDTO> taskDTOs = new ArrayList<>(taskCount);

    private Instant nextTaskDate;
    private Instant endAnsweringDate;

    private Long answeredProfileId;
    private Long markedAnswerId;

    private String winnerTag;

    private BattleStatus status = BattleStatus.OPEN;

    public void addProfile(Long id, BattleProfileContainer battleProfileContainer) {
        this.profileIdBattleProfileContainerMap.put(id, battleProfileContainer);
    }

    public void setWinner(Long winnerId) {
        Profile profile = profileIdBattleProfileContainerMap.get(winnerId).getProfile();
        winnerTag = profile.getTag();
    }

    public Long findCorrectAnswerId(int taskIndex) {
        return questions.get(taskIndex).getAnswers().stream().filter(Answer::getCorrect).findFirst().get().getId();
    }

    public Long findCurrentCorrectAnswerId() {
        return findCorrectAnswerId(currentTaskIndex);
    }

    public int getCurrentTaskPoints() {
        return taskDTOs.get(currentTaskIndex).getPoints();
    }

    public void increaseCurrentTaskIndex() {
        currentTaskIndex++;
    }

    public void profileReady(Long profileId) {
        profileIdBattleProfileContainerMap.get(profileId).setStatus(BattleProfileStatus.READY);
    }

    public boolean isReady() {
        return profileIdBattleProfileContainerMap.values().stream()
                .filter(battleProfileContainer -> battleProfileContainer.getStatus() != BattleProfileStatus.READY)
                .collect(Collectors.toList()).size() == 0;
    }

    public ProfileDTO prepareProfile(Long profileId) {
        return new ProfileDTO(profileIdBattleProfileContainerMap.get(profileId).getProfile());
    }

    public void addTask(Question question, TaskDTO taskDTO) {
        questions.add(question);
        taskDTOs.add(taskDTO);
    }

    public void fillModelIntro(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("status", status);
        model.put("taskCount", taskCount);
        model.put("opponent", prepareProfile(battleProfileContainer.getOpponentId()));
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
    }

    public void fillModelPreparingNextTask(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex));
        model.put("correctAnswerId", null);
        model.put("markedAnswerId", null);
        model.put("meAnswered", null);
        model.put("endAnsweringInterval", Math.max(endAnsweringDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswered(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("status", status);
        model.put("correctAnswerId", findCorrectAnswerId(currentTaskIndex));
        model.put("markedAnswerId", markedAnswerId);
        model.put("meAnswered", answeredProfileId.equals(battleProfileContainer.getProfileId()));
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model) {
        model.put("status", status);
        model.put("correctAnswerId", findCorrectAnswerId(currentTaskIndex));
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelClosed(Map<String, Object> model) {
        model.put("status", status);
        model.put("winnerTag", winnerTag);
    }

    public void forEachProfile(Consumer<? super BattleProfileContainer> action) {
        profileIdBattleProfileContainerMap.values().parallelStream().forEach(action);
    }


    public void fillModel(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        fillModelIntro(model, battleProfileContainer);
        if (status != BattleStatus.INTRO) {
            model.remove("task");
        }
        if (status == BattleStatus.ANSWERING) {
            fillModelAnswering(model);
        } else if (status == BattleStatus.ANSWERED) {
            fillModelAnswered(model, battleProfileContainer);
        } else if (status == BattleStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model);
        } else if (status == BattleStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model);
            fillModelAnsweringTimeout(model);
        } else if (status == BattleStatus.CLOSED) {
            fillModelClosed(model);
        }
    }
}
