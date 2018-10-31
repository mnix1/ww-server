package com.ww.game.play.command;

import com.ww.game.play.container.PlayContainer;

public class PlayAnswerCommand extends PlayCommand {
    private Long answeredProfileId;
    private Long chosenAnswerId;

    public PlayAnswerCommand(PlayContainer container, Long answeredProfileId, Long chosenAnswerId) {
        super(container);
        this.answeredProfileId = answeredProfileId;
        this.chosenAnswerId = chosenAnswerId;
    }

    @Override
    public void execute() {
        container.getDecisions().answered(answeredProfileId, chosenAnswerId);
    }
}
