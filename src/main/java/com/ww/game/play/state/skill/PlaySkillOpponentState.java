package com.ww.game.play.state.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

public class PlaySkillOpponentState extends PlaySkillState {
    protected MemberWisieManager opponentManager;
    @Getter
    private WarWisie opponentWisie;
    protected long interval;

    public PlaySkillOpponentState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager);
        this.opponentManager = opponentManager;
        this.opponentWisie = opponentManager.getContainer().getMember().getContent();
    }
}
