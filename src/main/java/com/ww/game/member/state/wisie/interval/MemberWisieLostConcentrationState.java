package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomElement;

public class MemberWisieLostConcentrationState extends MemberWisieIntervalState {
    private String afterStateName;

    public MemberWisieLostConcentrationState(MemberWisieManager manager) {
        super(manager, randomElement(MemberWisieStatus.getNoConcentrationActions()));
    }

    @Override
    protected double minInterval() {
        return 3 - 3 * getWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 6 - 6 * getWisie().getConcentrationF1();
    }

    private void init() {
        afterStateName = (String) params.get("afterStateName");
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    public void after() {
        manager.getFlow().run(afterStateName);
    }
}
