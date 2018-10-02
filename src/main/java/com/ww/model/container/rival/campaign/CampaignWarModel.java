package com.ww.model.container.rival.campaign;

import com.ww.model.container.rival.init.RivalInit;
import com.ww.model.container.rival.war.WarModel;
import com.ww.model.container.rival.war.WarTeams;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignWarModel extends WarModel {


    public CampaignWarModel(RivalInit container, WarTeams teamsContainer) {
        super(container, teamsContainer);
    }

    public String findChoosingTaskPropsTag() {
        return null;
    }
}
