package com.ww.play.command;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayFlow;
import com.ww.play.PlayModel;

import java.util.Map;

public class PlayAnswerCommand extends PlayCommand {

    public PlayAnswerCommand(PlayModel model, PlayFlow flow) {
        super(model, flow);
    }

    @Override
    public void execute(Long profileId, Map<String, Object> content) {
        if (!model.isStatusEquals(RivalStatus.ANSWERING)) {
            return;
        }
        flow.answeredAction(profileId, content);
    }
}
