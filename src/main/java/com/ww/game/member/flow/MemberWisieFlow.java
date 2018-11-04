package com.ww.game.member.flow;

import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.member.state.wisie.interval.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemberWisieFlow extends GameFlow {
    protected MemberWisieManager manager;
    protected Map<String, MemberWisieState> stateMap = new ConcurrentHashMap<>();
    protected List<MemberWisieState> states = new CopyOnWriteArrayList<>();

    public MemberWisieFlow(MemberWisieManager manager) {
        this.manager = manager;
        initStateMap();
    }

    private void initStateMap() {
        stateMap.put("WAITING_FOR_QUESTION", new MemberWisieWaitingForQuestionState(manager));
        stateMap.put("RECOGNIZING_QUESTION", new MemberWisieRecognizingQuestionState(manager));
        stateMap.put("THINKING", new MemberWisieThinkingState(manager));
        stateMap.put("THINK_KNOW_ANSWER", new MemberWisieThinkKnowAnswerState(manager));
        stateMap.put("LOOKING_FOR_ANSWER", new MemberWisieLookingForAnswerState(manager));
        stateMap.put("FOUND_ANSWER_LOOKING_FOR", new MemberWisieFoundAnswerLookingForState(manager));
    }

    public synchronized void run(String stateName) {
        MemberWisieState state = stateMap.get(stateName);
        states.add(state);
        state.initCommands();
        state.execute();
    }
}
