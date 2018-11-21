package com.ww.game.auto.command;

import com.ww.game.GameCommand;
import com.ww.game.auto.AutoManager;
import com.ww.model.container.MapModel;
import lombok.ToString;

import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.ANSWER;

@ToString
public class AutoWisorAnswerCommand extends GameCommand {
    private AutoManager manager;
    private Long answerId;

    public AutoWisorAnswerCommand(AutoManager manager, Long answerId) {
        this.manager = manager;
        this.answerId = answerId;
    }

    @Override
    public void execute() {
        Map<String, Object> model = new MapModel("id", ANSWER).put("answerId", answerId.intValue()).get();
        manager.getAutoPlayContainer().getManager().processMessage(manager.getProfile().getId(), model);
    }
}
