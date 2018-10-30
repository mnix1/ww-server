package com.ww.play.modelfiller;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.social.RivalProfileSeasonDTO;
import com.ww.play.container.PlayContainer;

import java.util.Map;

public class PlayModelFiller {
    public static void fillModelStatus(Map<String, Object> model, RivalStatus status) {
        model.put("status", status);
    }

    public static void fillModelImportanceType(Map<String, Object> model, PlayContainer container) {
        model.put("importance", container.getInit().getImportance().name());
        model.put("type", container.getInit().getType().name());
    }

    public static void fillModelProfiles(Map<String, Object> model, RivalTeam team, RivalTeam opponentTeam) {
        model.put("profile", new ExtendedProfileDTO(team.getProfile()));
        model.put("opponent", new ExtendedProfileDTO(opponentTeam.getProfile()));
    }

    public static void fillModelSeasons(Map<String, Object> model, PlayContainer container, RivalTeam team, RivalTeam opponentTeam) {
        if (container.isRanking()) {
            RivalTwoInit init = container.getInit();
            model.put("profileSeason", new RivalProfileSeasonDTO(init.getProfileSeasons(team.getProfileId())));
            model.put("opponentSeason", new RivalProfileSeasonDTO(init.getProfileSeasons(opponentTeam.getProfileId())));
        }
    }

    public static void fillModelNextInterval(Map<String, Object> model, PlayContainer container) {
        model.put("nextInterval", container.getTimeouts().nextInterval());
    }

    public static void fillModelSimpleNextTaskMeta(Map<String, Object> model, PlayContainer container) {
        model.put("task", container.getTasks().simpleNextTaskMeta());
    }

    public static void fillModelTaskMeta(Map<String, Object> model, PlayContainer container) {
        model.put("task", container.getTasks().taskMeta());
    }

    public static void fillModelTask(Map<String, Object> model, PlayContainer container) {
        model.put("task", container.getTasks().task());
    }

    public static void fillModelChoosingTaskPropsTag(Map<String, Object> model, PlayContainer container) {
        model.put("choosingTaskPropsTag", container.findChoosingTaskPropsTag());
    }
}
