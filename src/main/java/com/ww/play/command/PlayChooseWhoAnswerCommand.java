package com.ww.play.command;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;
import com.ww.play.flow.PlayWarFlow;

import java.util.Map;

public class PlayChooseWhoAnswerCommand extends PlayCommand {

    public PlayChooseWhoAnswerCommand(PlayManager manager) {
        super(manager);
    }

    @Override
    public void execute(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.CHOOSING_WHO_ANSWER)) {
            return;
        }
        ((PlayWarFlow) flow).choosingWhoAnswerPhase();
    }
}
