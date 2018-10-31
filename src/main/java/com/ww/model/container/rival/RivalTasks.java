package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.dto.rival.task.TaskMetaDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.service.RivalService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RivalTasks {
    protected RivalService rivalService;
    protected int currentTaskIndex = -1;
    protected int nextTaskIndex = 0;
    protected List<Question> questions = new CopyOnWriteArrayList<>();
    protected List<TaskDTO> taskDTOs = new CopyOnWriteArrayList<>();

    public RivalTasks(RivalService rivalService) {
        this.rivalService = rivalService;
    }

    public Question question() {
        return questions.get(currentTaskIndex);
    }

    public Long correctAnswerId() {
        return question().findCorrectAnswerId();
    }

    public TaskDTO task() {
        return taskDTOs.get(currentTaskIndex);
    }

    public int nextTaskIndex() {
        return nextTaskIndex;
    }

    public void increaseIndexes() {
        currentTaskIndex++;
        nextTaskIndex++;
    }

    public void prepareNext(Category category, DifficultyLevel difficulty, Language language) {
        increaseIndexes();
        Question question = rivalService.prepareQuestion(category, difficulty, language);
        question.setId((long) nextTaskIndex);
        question.initAnswerIds();
        questions.add(question);
        taskDTOs.add(rivalService.prepareTaskDTO(question));
    }

    public TaskMetaDTO simpleNextTaskMeta() {
        return new TaskMetaDTO(nextTaskIndex + 1);
    }

    public TaskMetaDTO taskMeta() {
        return new TaskMetaDTO(task());
    }
}
