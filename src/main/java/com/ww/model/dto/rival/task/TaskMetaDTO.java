package com.ww.model.dto.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskMetaDTO {

    private Long id;
    private Category category;
    private DifficultyLevel difficultyLevel;

    public TaskMetaDTO(TaskDTO taskDTO) {
        this.id = taskDTO.getId();
        this.category = taskDTO.getCategory();
        this.difficultyLevel = taskDTO.getDifficultyLevel();
    }

    public TaskMetaDTO(long id) {
        this.id = id;
    }
}
