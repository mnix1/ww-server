package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlaySetResultCommand;
import com.ww.game.play.command.PlayUpdateEloCommand;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelEnd;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelSeasons;

@Getter
public class PlaySurrenderState extends PlayState {
    private Long profileId;
    private PlayManager manager;

    public PlaySurrenderState(PlayManager manager, Long profileId) {
        super(manager, RivalStatus.CLOSED);
        this.profileId = profileId;
        this.manager = manager;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetResultCommand(getContainer(), profileId));
        commands.add(new PlayUpdateEloCommand(manager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelEnd(model, getContainer());
        fillModelSeasons(model, getContainer(), team, opponentTeam);
        return model;
    }

    @Override
    public void after() {
        manager.dispose();
    }
}
