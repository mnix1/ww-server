package com.ww.play.communication;

import com.ww.play.PlayWarManager;
import com.ww.play.command.PlayChooseWhoAnswerCommand;
import com.ww.websocket.message.Message;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

public class PlayWarCommunication extends PlayCommunication {

    public PlayWarCommunication(PlayWarManager manager) {
        super(manager);
    }

    @Override
    protected void initCommandMap() {
        super.initCommandMap();
        commandMap.put(CHOOSE_WHO_ANSWER, new PlayChooseWhoAnswerCommand(manager));
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

}
