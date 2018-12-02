package com.ww.game.member.command;

import com.ww.game.GameCommand;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.flow.PlayWarFlow;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.ANSWER;

@ToString
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
        new Thread(() -> ((PlayWarFlow) manager.getPlayManager().getFlow()).wisieAnswered(manager.getContainer().getTeam().getProfileId(), answerId)).run();
    }
}
