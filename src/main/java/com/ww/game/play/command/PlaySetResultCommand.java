package com.ww.game.play.command;

import com.ww.model.entity.outside.social.Profile;
import com.ww.game.play.container.PlayContainer;

import java.util.Optional;

public class PlaySetResultCommand extends PlayCommand {
    private Long resignedProfileId;

    public PlaySetResultCommand(PlayContainer container, Long resignedProfileId) {
        super(container);
        this.resignedProfileId = resignedProfileId;
    }

    public PlaySetResultCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        if (resignedProfileId != null) {
            Profile winner = container.getTeams().opponent(resignedProfileId).getProfile();
            container.getResult().winnerOpponentResigned(winner);
            return;
        }
        Optional<Profile> optionalWinner = container.findWinner();
        if (optionalWinner.isPresent()) {
            container.getResult().winner(optionalWinner.get());
        } else {
            container.getResult().draw();
        }
    }
}
