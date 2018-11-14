package com.ww.game.member.flow;

import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.member.state.wisie.answer.MemberWisieAnsweredState;
import com.ww.game.member.state.wisie.interval.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemberWisieFlow extends GameFlow {
    protected MemberWisieManager manager;

    public MemberWisieFlow(MemberWisieManager manager) {
        this.manager = manager;
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("LOST_CONCENTRATION", new MemberWisieLostConcentrationState(manager));
        stateMap.put("WAITING_FOR_QUESTION", new MemberWisieWaitingForQuestionState(manager));
        stateMap.put("RECOGNIZING_QUESTION", new MemberWisieRecognizingQuestionState(manager));
        stateMap.put("THINKING", new MemberWisieThinkingState(manager));
        stateMap.put("THINK_KNOW_ANSWER", new MemberWisieThinkKnowAnswerState(manager));
        stateMap.put("LOOKING_FOR_ANSWER", new MemberWisieLookingForAnswerState(manager));
        stateMap.put("FOUND_ANSWER_LOOKING_FOR", new MemberWisieFoundAnswerLookingForState(manager));
        stateMap.put("NO_FOUND_ANSWER_LOOKING_FOR", new MemberWisieNoFoundAnswerLookingForState(manager));
        stateMap.put("ANSWERED", new MemberWisieAnsweredState(manager));
        stateMap.put("NOT_SURE_OF_ANSWER", new MemberWisieNotSureOfAnswerState(manager));
        stateMap.put("RECOGNIZING_ANSWERS", new MemberWisieRecognizingAnswersState(manager));
        stateMap.put("THINKING_WHICH_ANSWER_MATCH", new MemberWisieThinkingWhichAnswerMatchState(manager));
        stateMap.put("NOW_KNOW_ANSWER", new MemberWisieNowKnowAnswerState(manager));
        stateMap.put("DOESNT_KNOW_ANSWER", new MemberWisieDoesntKnowAnswerState(manager));
        stateMap.put("THINKING_GIVE_RANDOM_ANSWER", new MemberWisieThinkingGiveRandomAnswerState(manager));
        stateMap.put("SURRENDER", new MemberWisieSurrenderState(manager));
    }
}
