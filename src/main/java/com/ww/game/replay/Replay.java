package com.ww.game.replay;

import com.ww.service.social.ConnectionService;
import io.reactivex.Flowable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.ww.websocket.message.Message.RIVAL_CONTENT;

public class Replay {
    private final ConnectionService connectionService;
    private final List<Map<String, Object>> allModels;
    private final Long profileId;

    public Replay(ConnectionService connectionService, List<Map<String, Object>> allModels, Long profileId) {
        this.connectionService = connectionService;
        this.profileId = profileId;
        this.allModels = allModels;
    }

    public void play() {
        Map<String, Object> firstModel = allModels.get(0);
        run(firstModel, findNextState(firstModel).orElse(null));
    }

    public void run(Map<String, Object> model, Map<String, Object> nextModel) {
        connectionService.send(profileId, model, RIVAL_CONTENT);
        if (nextModel == null) {
            return;
        }
        long interval = getModelNow(nextModel) - getModelNow(model);
        if (interval <= 0) {
            run(nextModel, findNextState(nextModel).orElse(null));
        } else {
            Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                run(nextModel, findNextState(nextModel).orElse(null));
            });
        }
    }

    private long getModelNow(Map<String, Object> model) {
        return (long) model.get("now");
    }

    private Optional<Map<String, Object>> findNextState(Map<String, Object> model) {
        for (int i = 0; i < allModels.size(); i++) {
            if (allModels.get(i) == model && i + 1 < allModels.size()) {
                return Optional.of(allModels.get(i + 1));
            }
        }
        return Optional.empty();
    }
}
