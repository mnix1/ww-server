package com.ww.game.play.communication;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayChooseWhoAnswerAction;
import com.ww.game.play.action.skill.PlayGhostSkillAction;
import com.ww.game.play.action.skill.PlayHintSkillAction;
import com.ww.game.play.action.skill.PlayNinjaSkillAction;
import com.ww.game.play.action.skill.PlayLifebuoySkillAction;
import com.ww.websocket.message.Message;

import static com.ww.service.rival.global.RivalMessageService.*;

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
        actionMap.put(HINT, new PlayHintSkillAction(manager));
        actionMap.put(LIFEBUOY, new PlayLifebuoySkillAction(manager));
        actionMap.put(GHOST, new PlayGhostSkillAction(manager));
        actionMap.put(NINJA, new PlayNinjaSkillAction(manager));
    }

}
