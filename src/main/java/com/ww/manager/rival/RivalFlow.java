package com.ww.manager.rival;

import com.ww.manager.rival.state.StateSurrender;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.*;

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

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        RivalStatus status = getManager().getContainer().getStatus();
        if (id.equals(ANSWER) && status == RivalStatus.ANSWERING) {
            answer(profileId, content);
        } else if (id.equals(CHOOSE_TASK_PROPS) && status == RivalStatus.CHOOSING_TASK_PROPS) {
            chosenTaskProps(profileId, content);
        } else if (id.equals(SURRENDER) && status != RivalStatus.CLOSED && status != RivalStatus.DISPOSED) {
            surrender(profileId);
        } else {
            return false;
        }
        return true;
    }

    public abstract void start();

    public abstract void answer(Long profileId, Map<String, Object> content);

    public abstract void chosenTaskProps(Long profileId, Map<String, Object> content);

    public synchronized void surrender(Long profileId) {
        new StateSurrender(getManager(), profileId).startVoid();
    }
}
