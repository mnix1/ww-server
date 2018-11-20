package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.helper.RandomHelper;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;

import static com.ww.helper.TeamHelper.findIndexOfWisieWithHobby;
import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

public class AutoRivalChoosingWhoAnswerState extends AutoRivalState {

    public AutoRivalChoosingWhoAnswerState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        WarTeam team = (WarTeam) container.myTeam();
        int index = findIndexOfWisieWithHobby(team, container.question().getType().getCategory());
        long interval = RandomHelper.randomLong(1, (long) (((WarInterval) container.interval()).getChoosingWhoAnswerInterval() * 0.75));
        sendAfter(interval, CHOOSE_WHO_ANSWER, newModel("activeIndex", index));
    }
}
