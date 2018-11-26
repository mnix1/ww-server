package com.ww.game.play.state.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

public class PlaySkillOpponentState extends PlaySkillState {
    protected MemberWisieManager opponentManager;
    @Getter
    private WarWisie opponentWisie;
    protected WarTeam opponentWarTeam;

    public PlaySkillOpponentState(PlaySkillFlowOpponent flow) {
        super(flow);
        this.opponentManager = flow.getOpponentManager();
        this.opponentWisie = opponentManager.getContainer().getMember().getContent();
        this.opponentWarTeam = opponentManager.getContainer().getTeam();
    }

    @Override
    public String toString() {
        return super.toString() + ", " + manager.getContainer().getMember();
    }
}
