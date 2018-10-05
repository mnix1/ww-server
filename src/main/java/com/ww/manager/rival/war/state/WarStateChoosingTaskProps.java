package com.ww.manager.rival.war.state;

import com.ww.manager.rival.state.StateChoosingTaskProps;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

public class WarStateChoosingTaskProps extends StateChoosingTaskProps {

    public WarStateChoosingTaskProps(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        ((WarManager) manager).sendNewSkillsModel((m, wT) -> {
            WarTeam warTeam = (WarTeam) wT;
            warTeam.resetDisguises();
            warTeam.getTeamSkills().resetUsedAll();
            ((WarManager) manager).getModelFactory().fillModelActiveMember(m, wT);
        });
        return super.processFlowable();
    }
}
