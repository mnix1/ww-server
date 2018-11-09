package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillRemovingGhostState extends PlaySkillState {
    private boolean scareSuccess;
    private double value;
    private boolean disqualification;
    private double noDisqualificationChance;

    public PlaySkillRemovingGhostState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
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
        value = (getWisie().getConfidenceF1() + getWisie().getCunningF1() + getWisie().getSpeedF1()) / 3;
        if (scareSuccess) {
            noDisqualificationChance = value + 0.2;
        } else {
            noDisqualificationChance = value - 0.2;
        }
        disqualification = noDisqualificationChance <= randomDouble();
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
        if (disqualification) {
            Map<String,Object> params = new HashMap<>();
            params.put("scareSuccess", scareSuccess);
            flow.run("DISQUALIFICATION", params);
        } else {
            flow.run("NO_DISQUALIFICATION");
        }
    }
}
