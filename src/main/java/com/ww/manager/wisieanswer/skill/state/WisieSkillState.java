package com.ww.manager.wisieanswer.skill.state;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;

public abstract class WisieSkillState extends WisieState {
    private AbstractState prevState;

    protected WisieSkillState(WisieAnswerManager manager, String type) {
        super(manager, type);
    }

    @Override
    public String describe() {
        return "WisieSkillState " + super.describe() + ", warManager=" + manager.describe();
    }

    public WisieSkillState setPrevState(AbstractState prevState){
        this.prevState = prevState;
        return this;
    }

    public boolean hasPrevState(){
        return prevState != null;
    }

    @Override
    public boolean isRunning() {
        return manager.isRunning();
    }

    public void runPrevState(){
        prevState.startFlowableEndListeners();
    }
}
