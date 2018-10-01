package com.ww.model.container.rival.task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ww.model.entity.inside.task.MemoryShape;
import com.ww.model.entity.inside.task.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MemoryObject {
    private String key;
    private MemoryShape shape;
    private Color backgroundColor;

    public void writeToObjectNode(ObjectNode objectNode){
        objectNode.put("key", key)
                .put("shape", shape.getPath())
                .put("backgroundColor", backgroundColor.getHex());
    }
}
