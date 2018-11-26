package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.hint.*;

public class PlaySkillHintFlow extends PlaySkillFlow {
    protected Long answerId;

    public PlaySkillHintFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long answerId) {
        super(flowContainer, creatorProfileId);
        this.answerId = answerId;
    }

    @Override
    protected void initStateMap() {
        stateMap.put("HINT_RECEIVED", () -> new PlaySkillHintReceivedState(this));
        stateMap.put("THINKING_IF_USE_HINT", () -> new PlaySkillThinkingIfUseHintState(this, answerId));
        stateMap.put("WILL_USE_HINT", () -> new PlaySkillWillUseHintState(this));
        stateMap.put("WONT_USE_HINT", () -> new PlaySkillWontUseHintState(this));
        stateMap.put("ANSWERING_WITH_HINT", () -> new PlaySkillAnsweringWithHintState(this, answerId));
        stateMap.put("THINKING", () -> new PlaySkillThinkingState(this));
        stateMap.put("ANSWERING_WITHOUT_HINT", () -> new PlaySkillAnsweringWithoutHintState(this, answerId));
    }

    @Override
    public void start() {
        super.start();
        run("HINT_RECEIVED");
    }
}
