package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillRemovingGhostState extends PlaySkillState {
    private boolean scareSuccess;

    public PlaySkillRemovingGhostState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.REMOVING_GHOST));
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
    protected double minInterval() {
        return 2 - getWisie().getSpeedF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - 2 * getWisie().getSpeedF1() - getWisie().getCunningF1();
    }

    @Override
    public void after() {
        if (scareSuccess) {
            flow.run("NO_DISQUALIFICATION");
        } else {
            flow.run("DISQUALIFICATION");
        }
    }
}
