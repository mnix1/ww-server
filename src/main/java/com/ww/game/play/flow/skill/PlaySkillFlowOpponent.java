package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import lombok.Getter;

@Getter
public abstract class PlaySkillFlowOpponent extends PlaySkillFlow {

    protected MemberWisieManager opponentManager;
    protected Long opponentProfileId;

    public PlaySkillFlowOpponent(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId);
        this.opponentProfileId = opponentProfileId;
        this.opponentManager = flowContainer.getWisieManager(opponentProfileId);
    }
}
