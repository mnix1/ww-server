package com.ww.game.play.state;

import com.ww.game.GameState;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState extends GameState {
    protected PlayContainer container;
    protected Map<Long, Map<String, Object>> models = new ConcurrentHashMap<>();

    protected RivalStatus status;

    public PlayState(PlayContainer container, RivalStatus status) {
        this.container = container;
        this.status = status;
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelStatus(model, status);
        return model;
    }

    public Map<String, Object> prepareAndStoreModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = prepareModel(team, opponentTeam);
        storeModel(team.getProfileId(), model);
        return model;
    }

    public void storeModel(Long profileId, Map<String, Object> model) {
        models.put(profileId, model);
    }

    public Optional<Map<String, Object>> getStoredModel(Long profileId) {
        if (models.containsKey(profileId)) {
            return Optional.of(models.get(profileId));
        }
        return Optional.empty();
    }
}
