package com.ww.websocket.message;

import com.ww.model.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageDTO extends DTO {
    private Message id;
    private String content;
}
