package com.ww.model.entity.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskRenderer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Category category;
    private String value;
    private TaskRenderer renderer;
    private Integer difficulty;

    public TaskType(Category category, String value, TaskRenderer renderer, Integer difficulty) {
        this.category = category;
        this.value = value;
        this.renderer = renderer;
        this.difficulty = difficulty;
    }
}
