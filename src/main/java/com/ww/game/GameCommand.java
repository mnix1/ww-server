package com.ww.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameCommand {
    private static Logger logger = LoggerFactory.getLogger(GameCommand.class);


    public void execute() {
    }

    public void logAndExecute() {
//        logger.trace("execute " + toString());
        execute();
    }
}
