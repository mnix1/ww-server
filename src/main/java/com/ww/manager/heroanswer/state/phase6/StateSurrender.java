package com.ww.manager.heroanswer.state.phase6;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.entity.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomElement;

public class StateSurrender extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateSurrender.class);

    public StateSurrender(HeroAnswerManager manager) {
        super(manager);
    }

    protected void processVoid() {
        manager.addAndSendAction(HeroAnswerAction.SURRENDER);
    }
}
