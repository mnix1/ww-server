package com.ww.game.replay;

import com.ww.service.ReplayService;
import io.reactivex.Flowable;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.ww.websocket.message.Message.RIVAL_CONTENT;

public class Replay {
    private final ReplayService replayService;
    private final List<Map<String, Object>> allModels;
    @Getter
    private final Long profileId;
    private boolean inProgress = false;

    public Replay(ReplayService replayService, List<Map<String, Object>> allModels, Long profileId) {
        this.replayService = replayService;
        this.profileId = profileId;
        this.allModels = allModels;
    }

    public void play(double speed) {
        inProgress = true;
        Map<String, Object> firstModel = allModels.get(0);
        run(firstModel, findNextState(firstModel).orElse(null), speed);
    }

    public void stop() {
        inProgress = false;
    }

    public void run(Map<String, Object> model, Map<String, Object> nextModel, double speed) {
        if (!inProgress) {
            return;
        }
        replayService.getConnectionService().send(profileId, model, RIVAL_CONTENT);
        if (nextModel == null) {
            replayService.disposeReplay(this);
            return;
        }
        long interval = (long) ((getModelNow(nextModel) - getModelNow(model)) / speed);
        if (interval <= 0) {
            run(nextModel, findNextState(nextModel).orElse(null), speed);
        } else {
            Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                run(nextModel, findNextState(nextModel).orElse(null), speed);
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
