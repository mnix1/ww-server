package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlayPrepareNextTaskCommand;
import com.ww.play.command.PlaySetResultCommand;
import com.ww.play.container.PlayContainer;
import lombok.Getter;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelEnd;

@Getter
public class PlaySurrenderState extends PlayState {
    private Long profileId;

    public PlaySurrenderState(PlayContainer container, Long profileId) {
        super(container, RivalStatus.CLOSED);
        this.profileId = profileId;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetResultCommand(container, profileId));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelEnd(model, container);
        return model;
    }
}
