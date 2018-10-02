package com.ww.manager.rival;

import com.ww.manager.rival.state.StateSurrender;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Getter
public abstract class RivalFlow {
    protected static final Logger logger = LoggerFactory.getLogger(RivalFlow.class);
    protected Disposable activeFlowable;

    protected abstract RivalManager getManager();

    public void disposeFlowable() {
        if (activeFlowable != null) {
            activeFlowable.dispose();
            activeFlowable = null;
        }
    }

    public abstract void start();

    public abstract void answer(Long profileId, Map<String, Object> content);

    public abstract void chosenTaskProps(Long profileId, Map<String, Object> content);

    public synchronized void surrender(Long profileId) {
        new StateSurrender(getManager(), profileId).startVoid();
    }
}
