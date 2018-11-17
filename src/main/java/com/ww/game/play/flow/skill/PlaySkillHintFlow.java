package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.hint.*;

public class PlaySkillHintFlow extends PlaySkillFlow {
    protected Long answerId;

    public PlaySkillHintFlow(MemberWisieManager manager, Long answerId) {
        super(manager);
        this.answerId = answerId;
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("HINT_RECEIVED", () -> new PlaySkillHintReceivedState(this, manager));
        stateMap.put("THINKING_IF_USE_HINT", () -> new PlaySkillThinkingIfUseHintState(this, manager, answerId));
        stateMap.put("WILL_USE_HINT", () -> new PlaySkillWillUseHintState(this, manager));
        stateMap.put("WONT_USE_HINT", () -> new PlaySkillWontUseHintState(this, manager));
        stateMap.put("ANSWERING_WITH_HINT", () -> new PlaySkillAnsweringWithHintState(this, manager, answerId));
        stateMap.put("THINKING", () -> new PlaySkillThinkingState(this, manager));
        stateMap.put("ANSWERING_WITHOUT_HINT", () -> new PlaySkillAnsweringWithoutHintState(this, manager, answerId));
    }

    public void start() {
        run("HINT_RECEIVED");
    }
}
