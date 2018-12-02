package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.model.constant.Category;
import com.ww.model.container.MapModel;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.service.rival.global.RivalMessageService.CHOOSE_TASK_CATEGORY;

public class AutoRivalChoosingTaskCategoryState extends AutoRivalState {

    public AutoRivalChoosingTaskCategoryState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        if (!container.isMeChoosingTaskProps()) {
            return;
        }
        Category category = Category.random();
        if (container.isWarLike()) {
            WarTeam team = (WarTeam) container.team();
            category = team.getTeamMembers().stream().filter(teamMember -> teamMember.isWisie() && teamMember.isPresent()).findFirst().map(teamMember -> {
                List<Category> wisieHobbies = new ArrayList<>(((WisieTeamMember) teamMember).getContent().getWisie().getHobbies());
                return randomElement(wisieHobbies);
            }).orElse(category);
        }
        long interval = (long) (container.interval().getChoosingTaskCategoryInterval() * randomDouble(0.1, 0.35));
        sendAfter(interval, CHOOSE_TASK_CATEGORY, new MapModel("category", category).get());
    }
}
