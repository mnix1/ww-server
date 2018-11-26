package com.ww.game.play.flow.skill;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import lombok.Getter;
@Getter
public abstract class PlaySkillFlow extends GameFlow {

    protected MemberWisieManager manager;
    protected PlayWarAnsweringFlowContainer flowContainer;
    protected Long creatorProfileId;

    public PlaySkillFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId) {
        super();
        this.flowContainer = flowContainer;
        this.creatorProfileId = creatorProfileId;
        this.manager = flowContainer.getWisieManager(creatorProfileId);
    }
}
