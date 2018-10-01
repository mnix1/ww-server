package com.ww.model.container.rival.campaign;

import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarTeamsContainer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignWarContainer extends WarContainer {


    public CampaignWarContainer(RivalInitContainer container, WarTeamsContainer teamsContainer) {
        super(container, teamsContainer);
    }

    public String findChoosingTaskPropsTag() {
        return null;
    }
}
