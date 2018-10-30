package com.ww.play.communication;

import com.ww.play.PlayWarManager;
import com.ww.play.action.PlayChooseWhoAnswerAction;
import com.ww.websocket.message.Message;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

public class PlayWarCommunication extends PlayCommunication {

    public PlayWarCommunication(PlayWarManager manager) {
        super(manager);
    }

    @Override
    protected void initActionMap() {
        super.initActionMap();
        actionMap.put(CHOOSE_WHO_ANSWER, new PlayChooseWhoAnswerAction(manager));
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

}
