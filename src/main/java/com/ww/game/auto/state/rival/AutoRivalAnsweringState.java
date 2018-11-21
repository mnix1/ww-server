package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.flow.AutoWisieFlow;
import com.ww.game.auto.flow.AutoWisorFlow;
import com.ww.model.constant.wisie.HeroType;

import static com.ww.model.constant.wisie.HeroType.isWisie;

public class AutoRivalAnsweringState extends AutoRivalState {

    public AutoRivalAnsweringState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        HeroType heroType = container.activeMemberType();
        if (isWisie(heroType)) {
            container.setFlow(new AutoWisieFlow(manager));
        } else {
            container.setFlow(new AutoWisorFlow(manager));
        }
        container.getFlow().start();
    }
}
