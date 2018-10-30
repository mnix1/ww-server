package com.ww.play.command;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayChooseTaskPropsCommand extends PlayCommand {

    public PlayChooseTaskPropsCommand(PlayManager manager) {
        super(manager);
    }

    @Override
    public void execute(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.CHOOSING_TASK_PROPS)) {
            return;
        }
        flow.chosenTaskPropsAction(profileId, content);
    }
}
