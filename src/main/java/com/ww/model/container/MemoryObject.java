package com.ww.model.container;

import com.ww.model.entity.rival.task.MemoryShape;
import com.ww.model.entity.rival.task.TaskColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemoryObject {
    private String key;
    private MemoryShape shape;
    private TaskColor fontColor;
    private TaskColor backgroundColor;
    private TaskColor borderColor;
}
