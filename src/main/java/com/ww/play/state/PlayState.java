package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayContainer;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState {
    protected PlayContainer container;
    protected List<PlayCommand> commands = new CopyOnWriteArrayList<>();
    protected Map<Long, Map<String, Object>> models = new ConcurrentHashMap<>();

    protected RivalStatus status;
    protected Instant date = Instant.now();

    public PlayState(PlayContainer container, RivalStatus status) {
        this.container = container;
        this.status = status;
    }

    public void initCommands(){
    }

    public void execute() {
        for (PlayCommand command : commands) {
            command.execute();
        }
    }

    public void revoke() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).revoke();
        }
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
