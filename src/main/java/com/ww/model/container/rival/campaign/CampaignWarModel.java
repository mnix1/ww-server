package com.ww.model.container.rival.campaign;

import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.container.rival.war.WarModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignWarModel extends WarModel {

    public CampaignWarModel(RivalInit container, RivalTeams teamsContainer) {
        super(container, teamsContainer);
    }

    public String findChoosingTaskPropsTag() {
        return null;
    }
}
