package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.*;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.container.rival.init.RivalOnePlayerInit;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public abstract class RivalModel {
    protected RivalType type;
    protected RivalImportance importance;
    protected Profile creatorProfile;
    protected Profile opponentProfile;

    protected Long creatorEloChange;
    protected Long opponentEloChange;

    protected int currentTaskIndex = -1;

    protected List<Question> questions = new CopyOnWriteArrayList<>();
    protected List<TaskDTO> taskDTOs = new CopyOnWriteArrayList<>();

    protected Instant nextTaskDate;
    protected Instant endChoosingTaskPropsDate;
    protected Instant endAnsweringDate;

    protected Long answeredProfileId;
    protected Long markedAnswerId;

    protected Boolean draw;
    protected Profile winner;
    protected Profile looser;

    protected Boolean resigned;

    protected Category chosenCategory;
    protected DifficultyLevel chosenDifficulty;
    protected Boolean isChosenCategory;
    protected Boolean isChosenDifficulty;

    protected RivalStatus status = RivalStatus.OPEN;

    public abstract RivalTeams getTeams();

    protected RivalModel(RivalInit init, RivalTeams teams) {
        this.type = init.getType();
        this.importance = init.getImportance();
        if (init.getPlayer() == RivalPlayer.TWO) {
            RivalTwoPlayerInit c = (RivalTwoPlayerInit) init;
            this.creatorProfile = c.getCreatorProfile();
            this.opponentProfile = c.getOpponentProfile();
            teams.getOpponentMap().put(creatorProfile.getId(), opponentProfile.getId());
            teams.getOpponentMap().put(opponentProfile.getId(), creatorProfile.getId());
        } else if (init.getPlayer() == RivalPlayer.ONE) {
            RivalOnePlayerInit c = (RivalOnePlayerInit) init;
            this.creatorProfile = c.getCreatorProfile();
        }
    }

    public boolean isOpponent() {
        return opponentProfile != null;
    }

    public void setWinnerLooser(Profile winner) {
        this.draw = false;
        this.winner = winner;
        this.looser = getTeams().opponentTeam(winner.getId()).getProfile();
    }

    public boolean isRanking() {
        return importance == RivalImportance.RANKING;
    }

    public Long findCorrectAnswerId(int taskIndex) {
        if (taskIndex >= questions.size()) {
            throw new IllegalArgumentException("taskIndex outside of questions");
        }
        return questions.get(taskIndex).getAnswers().stream().filter(Answer::getCorrect).findFirst().get().getId();
    }

    public Long findCurrentCorrectAnswerId() {
        return findCorrectAnswerId(currentTaskIndex);
    }

    public abstract String findChoosingTaskPropsTag();

    public abstract Optional<Profile> findWinner();

    public boolean randomChooseTaskProps() {
        return findChoosingTaskPropsTag() == null;
    }

    public int getCurrentTaskPoints() {
        return taskDTOs.get(currentTaskIndex).getPoints();
    }

    public void increaseCurrentTaskIndex() {
        currentTaskIndex++;
    }

    public void addTask(Question question, TaskDTO taskDTO) {
        questions.add(question);
        taskDTOs.add(taskDTO);
    }
}
