package com.ww.model.container;

import com.ww.game.training.TrainingManager;
import com.ww.helper.TagHelper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.game.GameFlow.prepareFlowable;
import static com.ww.helper.ModelHelper.parseMessage;
import static com.ww.helper.RandomHelper.randomLong;

@Getter
public class TrainingProfileConnection implements InsideConnection {
    private static final Logger logger = LoggerFactory.getLogger(TrainingProfileConnection.class);

    private final TrainingManager manager;
    private final String sessionId;

    public TrainingProfileConnection(TrainingManager manager) {
        this.manager = manager;
        this.sessionId = TagHelper.randomUUID();
    }

    @Override
    public Long getProfileId() {
        return manager.getProfile().getId();
    }

    @Override
    public String getProfileTag() {
        return manager.getProfile().getTag();
    }

    public void close() {
    }

    public void sendMessage(String msg) {
//        logger.trace(toString() + ", " + msg);
        prepareFlowable(randomLong(10, 100)).subscribe(aLong -> {
            manager.getCommunication().handleMessage(parseMessage(msg));
        });
    }

    public void handleMessage(String msg) {
        manager.getPlayManager().processMessage(getProfileId(), parseMessage(msg));
    }

    @Override
    public boolean equals(Object obj) {
        return getProfileId().equals(((Connection) obj).getProfileId());
    }

    @Override
    public String toString() {
        return "TrainingProfileConnection{" +
                "profileId='" + getProfileId() + '\'' +
                "sessionId='" + sessionId + '\'' +
                super.toString() +
                '}';
    }
}