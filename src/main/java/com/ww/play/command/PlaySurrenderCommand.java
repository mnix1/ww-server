package com.ww.play.command;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlaySurrenderCommand extends PlayCommand {

    public PlaySurrenderCommand(PlayManager manager) {
        super(manager);
    }

    @Override
    public void execute(Long profileId, Map<String, Object> content) {
        if (container.isStatusEquals(RivalStatus.CLOSED) || container.isStatusEquals(RivalStatus.DISPOSED)) {
            return;
        }
        flow.surrenderAction(profileId);
    }
}
