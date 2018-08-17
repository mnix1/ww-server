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
    private Instant endChoosingTaskPropsDate;
    private Instant endAnsweringDate;

    private Long answeredProfileId;
    private Long markedAnswerId;

    private String winnerTag;
    private Boolean resigned;

    private BattleStatus status = BattleStatus.OPEN;

    public void addProfile(Long id, BattleProfileContainer battleProfileContainer) {
        this.profileIdBattleProfileContainerMap.put(id, battleProfileContainer);
    }

    public BattleProfileContainer getBattleProfileContainer(Long id) {
        return this.profileIdBattleProfileContainerMap.get(id);
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

    public String findChoosingTaskPropsTag() {
        List<BattleProfileContainer> battleProfileContainers = new ArrayList<>(profileIdBattleProfileContainerMap.values());
        Integer p1Score = battleProfileContainers.get(0).getScore();
        Integer p2Score = battleProfileContainers.get(1).getScore();
        if (p1Score.equals(p2Score)) {
            return null;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return battleProfileContainers.get(0).getProfile().getTag();
        }
        return battleProfileContainers.get(1).getProfile().getTag();
    }

    public String findWinnerTag() {
        List<BattleProfileContainer> battleProfileContainers = new ArrayList<>(profileIdBattleProfileContainerMap.values());
        Integer p1Score = battleProfileContainers.get(0).getScore();
        Integer p2Score = battleProfileContainers.get(1).getScore();
        if (p1Score.equals(p2Score)) {
            return null;
        }
        if (p1Score.compareTo(p2Score) < 0) {
            return battleProfileContainers.get(1).getProfile().getTag();
        }
        return battleProfileContainers.get(0).getProfile().getTag();
    }

    public boolean randomChooseTaskProps() {
        return findChoosingTaskPropsTag() == null;
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

    private void fillModelBasic(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("status", status);
        model.put("taskCount", taskCount);
        model.put("opponent", prepareProfile(battleProfileContainer.getOpponentId()));
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
    }

    public void fillModelIntro(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        fillModelBasic(model, battleProfileContainer);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelPreparingNextTask(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelAnswering(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
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
        model.put("newScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("newOpponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model) {
        model.put("status", status);
        model.put("correctAnswerId", findCorrectAnswerId(currentTaskIndex));
        model.put("markedAnswerId", null);
        model.put("meAnswered",null);
//        model.put("nextTaskInterval", Math.max(nextTaskDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
    }

    public void fillModelChoosingTaskPropsTimeout(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        model.put("status", status);
        model.put("score", profileIdBattleProfileContainerMap.get(battleProfileContainer.getProfileId()).getScore());
        model.put("opponentScore", profileIdBattleProfileContainerMap.get(battleProfileContainer.getOpponentId()).getScore());
        model.put("choosingTaskPropsInterval", Math.max(endChoosingTaskPropsDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("choosingTaskPropsTag", findChoosingTaskPropsTag());
        model.put("taskId", currentTaskIndex + 1);
        if (randomChooseTaskProps()) {
            model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
        } else {
            model.put("task", null);
        }
    }

    public void fillModelChosenTaskProps(Map<String, Object> model) {
        model.put("status", status);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModelClosed(Map<String, Object> model) {
        model.put("status", status);
        model.put("winnerTag", winnerTag);
        model.put("resigned", resigned);
    }

    public void forEachProfile(Consumer<? super BattleProfileContainer> action) {
        profileIdBattleProfileContainerMap.values().parallelStream().forEach(action);
    }


    public void fillModel(Map<String, Object> model, BattleProfileContainer battleProfileContainer) {
        fillModelBasic(model, battleProfileContainer);
        if (status == BattleStatus.ANSWERING) {
            fillModelAnswering(model, battleProfileContainer);
        } else if (status == BattleStatus.ANSWERED) {
            model.put("task", taskDTOs.get(currentTaskIndex));
            fillModelAnswered(model, battleProfileContainer);
        } else if (status == BattleStatus.PREPARING_NEXT_TASK) {
            fillModelPreparingNextTask(model);
        } else if (status == BattleStatus.ANSWERING_TIMEOUT) {
            fillModelAnswering(model, battleProfileContainer);
            fillModelAnsweringTimeout(model);
        } else if (status == BattleStatus.CLOSED) {
            fillModelClosed(model);
        } else if (status == BattleStatus.CHOOSING_TASK_PROPS) {
            fillModelChoosingTaskProps(model, battleProfileContainer);
        } else if (status == BattleStatus.CHOOSING_TASK_PROPS_TIMEOUT) {
            fillModelChoosingTaskPropsTimeout(model);
        } else if (status == BattleStatus.CHOSEN_TASK_PROPS) {
            fillModelChosenTaskProps(model);
        } else if (status == BattleStatus.INTRO) {
            fillModelIntro(model, battleProfileContainer);
        }
    }
}
