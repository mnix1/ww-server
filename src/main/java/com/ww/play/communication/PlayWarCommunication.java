package com.ww.play.communication;

import com.ww.play.PlayManager;
import com.ww.play.PlayWarManager;
import com.ww.play.action.PlayChooseWhoAnswerAction;
import com.ww.websocket.message.Message;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

public class PlayWarCommunication extends PlayCommunication {

    public PlayWarCommunication(PlayManager manager) {
        this(manager, Message.WAR_CONTENT);
    }

    public PlayWarCommunication(PlayManager manager, Message messageContent) {
        super(manager, messageContent);
    }

    @Override
    protected void initActionMap() {
        super.initActionMap();
        actionMap.put(CHOOSE_WHO_ANSWER, new PlayChooseWhoAnswerAction(manager));
    }

}
