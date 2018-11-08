package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.ANSWER;

public class MemberWisieAnswerCommand extends GameCommand {
    private MemberWisieManager manager;
    private Long answerId;

    public MemberWisieAnswerCommand(MemberWisieManager manager, Long answerId) {
        this.manager = manager;
        this.answerId = answerId;
    }

    @Override
    public void execute() {
        Map<String, Object> model = new HashMap<>();
        model.put("answerId", answerId.intValue());
        model.put("id", ANSWER);
        manager.getPlayManager().getCommunication().processMessage(manager.getContainer().getTeam().getProfileId(), model);
    }
}
