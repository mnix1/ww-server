package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillDisqualificationState extends PlaySkillState {
    private boolean scareSuccess;

    public PlaySkillDisqualificationState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.DISQUALIFICATION));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.JUDGE));
        commands.add(new PlayWarDisableActiveTeamMemberCommand(manager.getContainer().getTeam()));
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    private void init() {
        scareSuccess = (boolean) params.get("scareSuccess");
    }


    @Override
    protected double prepareInterval() {
        if (scareSuccess) {
            return 3;
        }
        return 0;
    }

    @Override
    public void after() {
        if (scareSuccess) {
            ((PlayWarFlow) manager.getPlayManager().getFlow()).wisiesWontAnswer();
        }
    }
}
