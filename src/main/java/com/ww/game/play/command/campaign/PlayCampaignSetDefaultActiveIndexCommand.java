package com.ww.game.play.command.campaign;

import com.ww.model.constant.Category;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.GameCommand;
import com.ww.game.play.container.PlayContainer;

import static com.ww.helper.TeamHelper.findIndexOfWisieWithHobby;
import static com.ww.helper.TeamHelper.isBotProfile;

public class PlayCampaignSetDefaultActiveIndexCommand extends GameCommand {

    public PlayCampaignSetDefaultActiveIndexCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            if (isBotProfile(warTeam.getProfile())) {
                Category category = container.getTasks().question().getType().getCategory();
                warTeam.setActiveIndex(findIndexOfWisieWithHobby(warTeam, category));
                warTeam.setChosenActiveIndex(true);
            } else {
                warTeam.setActiveIndex(warTeam.getPresentIndexes().get(0));
                warTeam.setChosenActiveIndex(false);
            }
        }
    }
}
