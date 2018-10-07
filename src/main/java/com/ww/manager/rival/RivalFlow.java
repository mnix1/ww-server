package com.ww.manager.rival;

import com.ww.manager.AbstractFlow;
import com.ww.manager.AbstractState;
import com.ww.manager.rival.state.StateSurrender;
import com.ww.model.constant.rival.RivalStatus;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.service.rival.global.RivalMessageService.*;

@Getter
public abstract class RivalFlow extends AbstractFlow  {
    protected abstract RivalManager getManager();

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        RivalStatus status = getManager().getModel().getStatus();
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
        dispose();
        addState(new StateSurrender(getManager(), profileId)).startVoid();
    }
}
