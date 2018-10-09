package com.ww.manager.wisieanswer.skill.state;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import lombok.Getter;

@Getter
public class WisieSkillState extends WisieState {
    private AbstractState prevState;
    protected WisieSkillState(WisieAnswerManager manager, String type) {
        super(manager, type);
    }

    public WisieSkillState setPrevState(AbstractState prevState){
        this.prevState = prevState;
        return this;
    }

    public void runPrevState(){
        prevState.startFlowableEndListeners();
    }
}
