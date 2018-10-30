package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.social.RivalProfileSeasonDTO;
import com.ww.play.container.PlayContainer;

import java.util.Map;

public class PlayIntroState extends PlayState {
    public PlayIntroState(PlayContainer container) {
        super(container, RivalStatus.INTRO);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        model.put("importance", container.getInit().getImportance().name());
        model.put("type", container.getInit().getType().name());
        model.put("profile", new ExtendedProfileDTO(team.getProfile()));
        model.put("opponent", new ExtendedProfileDTO(opponentTeam.getProfile()));
        if (container.isRanking()) {
            model.put("profileSeason", new RivalProfileSeasonDTO(container.getInit().getCreatorProfileSeason()));
            model.put("opponentSeason", new RivalProfileSeasonDTO(container.getInit().getOpponentProfileSeason()));
        }
        return model;
    }
}
