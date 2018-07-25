package com.ww.model.container;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private TaskColor backgroundColor;

    public void writeToObjectNode(ObjectNode objectNode){
        objectNode.put("key", key)
                .put("shape", shape.getPath())
                .put("backgroundColor", backgroundColor.getHex());
    }
}
