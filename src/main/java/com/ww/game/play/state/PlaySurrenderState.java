package com.ww.game.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.PlaySetResultCommand;
import com.ww.game.play.command.PlayUpdateEloCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.service.RivalService;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelEnd;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelSeasons;

@Getter
public class PlaySurrenderState extends PlayState {
    private Long profileId;
    private PlayManager manager;

    public PlaySurrenderState(PlayContainer container, Long profileId, PlayManager manager) {
        super(container, RivalStatus.CLOSED);
        this.profileId = profileId;
        this.manager = manager;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetResultCommand(container, profileId));
        commands.add(new PlayUpdateEloCommand(manager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelEnd(model, container);
        fillModelSeasons(model, container, team, opponentTeam);
        return model;
    }
}
