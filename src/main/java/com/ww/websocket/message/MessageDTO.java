package com.ww.websocket.message;

import com.ww.helper.JSONHelper;
import com.ww.model.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MessageDTO extends DTO {
    private Message id;
    private String content;


    public static String message(Message message, Map<String, Object> model) {
        return new MessageDTO(message, JSONHelper.toJSON(model)).toString();
    }

    public static String rivalContentMessage(Map<String, Object> model) {
        return message(Message.RIVAL_CONTENT, model);
    }
}
