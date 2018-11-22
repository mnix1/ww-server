package com.ww.game;

import com.ww.game.member.state.wisie.interval.MemberWisieIntervalState;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    public static Logger logger = LoggerFactory.getLogger(MemberWisieIntervalState.class);

    protected List<GameCommand> commands = new CopyOnWriteArrayList<>();
    @Getter
    protected Instant date = Instant.now();
    @Setter
    protected Map<String, Object> params;

    public void initProps() {
    }

    protected void initCommands() {
    }

    public void logBeforeExecute() {
        logger.trace("execute " + toString());
    }

    public void execute() {
//        logBeforeExecute();
        if (commands.isEmpty()) {
            initCommands();
        }
        for (GameCommand command : commands) {
            command.logAndExecute();
        }
    }

    public void updateNotify() {
    }

    public boolean afterReady() {
        return true;
    }

    public long afterInterval() {
        return 0;
    }

    public void after() {
    }

    public boolean stopAfter() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                " date=" + date +
                '}';
    }
}
